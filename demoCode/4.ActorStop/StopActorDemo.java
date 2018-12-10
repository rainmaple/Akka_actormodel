import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

//����ִ�����񣬱�����ֹͣ��workerActor
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
 * ��������Ӽ�Actor��Actor
 */
class WatchActor extends UntypedActor{
    LoggingAdapter log = Logging.getLogger(this.getContext().system(),this);
    ActorRef child = null;

    @Override
    public void preStart() throws Exception{
        //�����Ӽ�Actor,�÷����������Զ�����
        child =getContext().actorOf(Props.create(WorkerActor.class),"child-workerActor");
        //ʵ�ּ��
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
	//��Actor����ֹͣ�˻��յ�Terminated���͵���Ϣ
            Terminated t = (Terminated) message;
            log.info("��ص�"+t.getActor()+"ֹͣ��");
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
        //ʵ�ֹرյ����ַ�ʽ
        //system.stop(ref);
        //ref.tell(PoisonPill.getInstance(),ActorRef.noSender());
       // ref.tell(Kill.getInstance(),ActorRef.noSender());
        //Kill�ķ�ʽ���׳��쳣��һ�㱻����Supervisor����Ĭ�ϴ������ͣ��Actor
        ref.tell("stopChild",ActorRef.noSender());
    }
}
