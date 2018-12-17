package Mailbox;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import com.typesafe.config.Config;

/**
*@Description  通过配置邮箱类型来进行消息优先级的定义
*@Param
*@Return
*@Author Rainmaple
*@Date 2018/12/17
*@Time 20:29
*/
public class MsgPriorityMailBox extends UnboundedStablePriorityMailbox {
    public MsgPriorityMailBox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message.equals("a")) {
                    return 0;
                } else if (message.equals("b")) {
                    return 1;
                } else if (message.equals("c")) {
                    return 2;
                } else {
                    return 3;
                }
            }
        });
    }

    public static void main(String args[]) {
        ActorSystem system = ActorSystem.create("sys");
        ActorRef ref = system.actorOf(Props.create(MsgPriorityActor.class)
                .withMailbox("msgprio-mailbox"), "priorityActor" );
        Object[] messages ={"b","a","c"};
        for(Object msg:messages){
            ref.tell(msg,ActorRef.noSender());
        }
    }
}
class MsgPriorityActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("message is "+message.toString());
    }
}
