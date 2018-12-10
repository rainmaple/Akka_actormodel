import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

//创建执行任务，被勒令停止的workerActor
class WorkerActor extends UntypedActor{

    LoggingAdapter log = Logging.getLogger(this.getContext().system(),this);
    @Override
    public void onReceive(Object message) throws Throwable {
        log.info("Have received the message: "+message);
    }
    @Override
    public void postStop() throws  Exception{
        log.info("worker postStop");
    }
}
/***
 * 创建监控子级Actor的Actor
 */
class WatchActor extends UntypedActor{
    LoggingAdapter log = Logging.getLogger(this.getContext().system(),this);
    ActorRef child = null;

    @Override
    public void preStart() throws Exception{
        //创建子级Actor,该方法启动后自动调用
        child =getContext().actorOf(Props.create(WorkerActor.class),"child-workerActor");
        //实现监控
        getContext().watch(child);
    }
    @Override
    public void postStop() throws Exception{
        log.info("WatchActor postStop");
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String){
            if (message.equals("stopChild")){
                getContext().stop(child);
            }
        }else if(message instanceof Terminated){
	//当Actor真正停止了会收到Terminated类型的消息
            Terminated t = (Terminated) message;
            log.info("监控到"+t.getActor()+"停止了");
        }else {
            unhandled(message);
        }
    }
}
public class StopActorDemo {
    public static void  main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef ref =system.actorOf(Props.create(WatchActor.class),
                "workerActor");
        //实现关闭的三种方式
        //system.stop(ref);
        //ref.tell(PoisonPill.getInstance(),ActorRef.noSender());
       // ref.tell(Kill.getInstance(),ActorRef.noSender());
        //Kill的方式会抛出异常，一般被父级Supervisor处理，默认处理就是停掉Actor
        ref.tell("stopChild",ActorRef.noSender());
    }
}
