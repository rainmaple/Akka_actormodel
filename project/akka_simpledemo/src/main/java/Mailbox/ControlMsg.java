package Mailbox;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.ControlMessage;
import scala.sys.Prop;

public class ControlMsg implements ControlMessage {
    private final String status;

    public ControlMsg(String status) {
        this.status = status;
    }
    @Override
    public String toString(){

        return this.status;
    }
    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef ref =system.actorOf(Props.create(ControlAwareActor.class).
                withMailbox("control-aware-mailbox"),"controlAware");
        Object[] messages ={"b","a",new ControlMsg("control key")};
        for(Object msg:messages){
            ref.tell(msg,ActorRef.noSender());
        }
    }
}
class ControlAwareActor extends UntypedActor{

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("message is "+message.toString());
    }
}
