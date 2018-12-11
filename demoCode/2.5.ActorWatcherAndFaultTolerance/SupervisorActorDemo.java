import akka.actor.*;

import akka.japi.Function;
import scala.Option;
import scala.concurrent.duration.Duration;


import java.io.IOException;
import java.sql.SQLException;


/**
 * Actor 采用父监督的模式来进行监督管理即会监督子Actor的异常情况
 * 根据默认或者预先设置好的处理逻辑来确定
 * 1）恢复
 * 2）停止
 * 3）重启或者上溯至父级
 *这个demo用来演示整个监督策略的过程
 */
public class SuperVisorActorDemo extends UntypedActor {

    //默认会采用一个出问题就找当事人的策略oneforone
    //策略strategy需要的第三个对象decider需要定义Function对象，使用apply返回监督指令
    private SupervisorStrategy strategy =new OneForOneStrategy(3,
            Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>(){
        @Override
        public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof IOException) {
                System.out.println("============IO Exception============");
                //恢复运行
                return SupervisorStrategy.resume();
            } else if (t instanceof IndexOutOfBoundsException) {
                System.out.println("============IndexOutOfBoundsException============");
                //重启
                return SupervisorStrategy.restart();
            } else if (t instanceof SQLException) {
                System.out.println("============SQLException============");
                //停止
                return SupervisorStrategy.stop();
            } else {
                System.out.println("============escalate============");
                //升级失败
                return SupervisorStrategy.escalate();
            }

        }
    });

    //返回监督策略使得这个被用到
    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Terminated) {
            Terminated t = (Terminated) message;
            System.out.println("监控到" + t.getActor() + "已经停止了");
        } else {
            //接收传来查询的信息
            System.out.println("stateCount:" + message);
        }
    }
    ///测试过程
    //重写preStart()方法创建子Actor
    @Override
    public void preStart(){
        ActorRef workerActor =getContext().actorOf(Props.create(WorkerActorinSV.class),"workerActorInSV");

        getContext().watch(workerActor);
        //workerActor.tell(new IOException(),getSelf());
        //System.out.println("=========have send message to get value=============");
    }



    //编写测试主方法
    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef superref =system.actorOf(Props.create(SuperVisorActorDemo.class),"super");
        //superref.notify();
    }

}

//验证指令是否正常工作建立子Actor

/***
 * 重写preStart/postStop,preRestart,postRestart
 * 观察不同情况生命周期情况
 */
class WorkerActorinSV extends UntypedActor{
    //状态数据
    private int stateCount = 1;
    public void preStart() throws Exception{
        //初始化时会被调用
        super.preStart();
        System.out.println("worker actor preStart");
    }
    @Override
    public void postStop() throws Exception{
        //停止时会调用该方法，发送一个Terminated信息给监控者
        super.postStop();
        System.out.println("worker actor preStart");
    }
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception{
        //
        System.out.println("worker actor preReStart begin "+this.stateCount);
        super.preRestart(reason,message);
        System.out.println("worker actor preReStart end "+this.stateCount);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        //重启后在新实例会调用该方法，默认调用preStart方法
        System.out.println("worker actor postRestart begin "+this.stateCount);
        super.postRestart(reason);
        System.out.println("worker actor postRestart end "+this.stateCount);
    }
    @Override
    public void onReceive(Object message) throws Exception {
        //模拟创建计算任务
        this.stateCount++;
        if(message instanceof Exception){
            throw (Exception) message;
        }else if ("getValue".equals(message)){
            getSender().tell(stateCount,getSelf());

            System.out.println("=========worker have sent the stateCount to watcher=============");
        }else{
            unhandled(message);
        }

    }
}
