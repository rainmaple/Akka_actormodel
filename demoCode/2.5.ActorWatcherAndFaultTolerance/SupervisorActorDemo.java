import akka.actor.*;

import akka.japi.Function;
import scala.Option;
import scala.concurrent.duration.Duration;


import java.io.IOException;
import java.sql.SQLException;


/**
 * Actor ���ø��ල��ģʽ�����мල������ල��Actor���쳣���
 * ����Ĭ�ϻ���Ԥ�����úõĴ����߼���ȷ��
 * 1���ָ�
 * 2��ֹͣ
 * 3��������������������
 *���demo������ʾ�����ල���ԵĹ���
 */
public class SuperVisorActorDemo extends UntypedActor {

    //Ĭ�ϻ����һ����������ҵ����˵Ĳ���oneforone
    //����strategy��Ҫ�ĵ���������decider��Ҫ����Function����ʹ��apply���ؼලָ��
    private SupervisorStrategy strategy =new OneForOneStrategy(3,
            Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>(){
        @Override
        public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof IOException) {
                System.out.println("============IO Exception============");
                //�ָ�����
                return SupervisorStrategy.resume();
            } else if (t instanceof IndexOutOfBoundsException) {
                System.out.println("============IndexOutOfBoundsException============");
                //����
                return SupervisorStrategy.restart();
            } else if (t instanceof SQLException) {
                System.out.println("============SQLException============");
                //ֹͣ
                return SupervisorStrategy.stop();
            } else {
                System.out.println("============escalate============");
                //����ʧ��
                return SupervisorStrategy.escalate();
            }

        }
    });

    //���ؼල����ʹ��������õ�
    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Terminated) {
            Terminated t = (Terminated) message;
            System.out.println("��ص�" + t.getActor() + "�Ѿ�ֹͣ��");
        } else {
            //���մ�����ѯ����Ϣ
            System.out.println("stateCount:" + message);
        }
    }
    ///���Թ���
    //��дpreStart()����������Actor
    @Override
    public void preStart(){
        ActorRef workerActor =getContext().actorOf(Props.create(WorkerActorinSV.class),"workerActorInSV");

        getContext().watch(workerActor);
        //workerActor.tell(new IOException(),getSelf());
        //System.out.println("=========have send message to get value=============");
    }



    //��д����������
    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef superref =system.actorOf(Props.create(SuperVisorActorDemo.class),"super");
        //superref.notify();
    }

}

//��ָ֤���Ƿ���������������Actor

/***
 * ��дpreStart/postStop,preRestart,postRestart
 * �۲첻ͬ��������������
 */
class WorkerActorinSV extends UntypedActor{
    //״̬����
    private int stateCount = 1;
    public void preStart() throws Exception{
        //��ʼ��ʱ�ᱻ����
        super.preStart();
        System.out.println("worker actor preStart");
    }
    @Override
    public void postStop() throws Exception{
        //ֹͣʱ����ø÷���������һ��Terminated��Ϣ�������
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
        //����������ʵ������ø÷�����Ĭ�ϵ���preStart����
        System.out.println("worker actor postRestart begin "+this.stateCount);
        super.postRestart(reason);
        System.out.println("worker actor postRestart end "+this.stateCount);
    }
    @Override
    public void onReceive(Object message) throws Exception {
        //ģ�ⴴ����������
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
