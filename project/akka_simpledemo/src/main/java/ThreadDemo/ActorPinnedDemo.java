package ThreadDemo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class ActorPinnedDemo extends UntypedActor {
    /**
    *@Description  对于耗时很久的Actor建议使用withDispatcher("my-pinned-dispatcher")这个配置
    *@Param
    *@Return
    *@Author Rainmaple
    *@Date 2018/12/17
    *@Time 18:16
    */

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println(getSelf()+"---->"+message+" "+ Thread.currentThread().getName());
        Thread.sleep(5000);
    }
    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        for(int i=0;i<20;i++){
            ActorRef ref =system.actorOf(Props.create(ActorPinnedDemo.class)
                    .withDispatcher("my-pinned-dispatcher"),"actordemo"+i);
            ref.tell("hello pinned",ActorRef.noSender());
        }
    }
}
