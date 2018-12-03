import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * 定义目标接收Actor
 */
class TargetActor extends UntypedActor{
    @Override
    public void onReceive(Object msg) throws Exception{
        System.out.println("Target received: "+msg+ "Sender is "+getSender());
    }
}

/**
 * 定义转发Actor
 * 建立actor私有引用在回调函数中异步接收发送者所传递消息，并进行转发
 */
class ForwardActor extends UntypedActor{
    private ActorRef target =getContext().actorOf(Props.create(TargetActor.class),"tagetActor");
    @Override
    public void onReceive(Object msg) throws Exception {
        target.forward(msg,getContext());
    }
}

/**
 * 定义消息发送类
 *获得sender的引用
 *获得转发者的引用
 * 使用转发者来发送sender的消息
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
