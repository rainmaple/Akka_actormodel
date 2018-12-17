package Mailbox;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Envelope;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import com.typesafe.config.Config;
import scala.Option;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BussinessMsgQueue implements MessageQueue {
    /**
    *@Description 自定义邮箱
    *@Param
    *@Return
    *@Author Rainmaple
    *@Date 2018/12/17
    *@Time 22:10
    */
    private Queue<Envelope> queue =new ConcurrentLinkedDeque<Envelope>();

    @Override
    public void enqueue(ActorRef receiver, Envelope e) {
        queue.offer(e);
    }

    @Override
    public Envelope dequeue() {
        return queue.poll();
    }

    @Override
    public int numberOfMessages() {
        return queue.size();
    }

    @Override
    public boolean hasMessages() {
        return !queue.isEmpty();
    }

    @Override
    public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
        for(Envelope e:queue){
            //投递消息入队
            deadLetters.enqueue(owner,e);
        }
    }
}
class BussinessMailBoxType implements MailboxType, ProducesMessageQueue<BussinessMsgQueue>{
    public BussinessMailBoxType(ActorSystem.Settings settings, Config config){

    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new BussinessMsgQueue();
    }

    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef ref =system.actorOf(Props.create(diymailActor.class).
                withMailbox("business-mail-box"),"business");
        Object[] messages ={"b","a",new ControlMsg("control key")};
        for(Object msg:messages){
            ref.tell(msg,ActorRef.noSender());
        }
    }

}
class diymailActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("message is "+message.toString());
    }
}