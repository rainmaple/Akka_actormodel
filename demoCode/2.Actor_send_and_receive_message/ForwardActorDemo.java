import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * ����Ŀ�����Actor
 */
class TargetActor extends UntypedActor{
    @Override
    public void onReceive(Object msg) throws Exception{
        System.out.println("Target received: "+msg+ "Sender is "+getSender());
    }
}

/**
 * ����ת��Actor
 * ����actor˽�������ڻص��������첽���շ�������������Ϣ��������ת��
 */
class ForwardActor extends UntypedActor{
    private ActorRef target =getContext().actorOf(Props.create(TargetActor.class),"tagetActor");
    @Override
    public void onReceive(Object msg) throws Exception {
        target.forward(msg,getContext());
    }
}

/**
 * ������Ϣ������
 *���sender������
 *���ת���ߵ�����
 * ʹ��ת����������sender����Ϣ
 */
public class ForwardActorDemo extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        //getSender().tell("hello "+msg,getSelf());
        //System.out.println("Sending message");
    }
    public static void main(String args[]){
        ActorSystem system =ActorSystem .create("sys");
        ActorRef sender = system.actorOf(Props.create(ForwardActorDemo.class),"Sender");
        ActorRef ForwardA =system.actorOf(Props.create(ForwardActor.class),"tagetActor");
        ForwardA.tell("hello you have got message which was forwarded",sender);
    }
}
