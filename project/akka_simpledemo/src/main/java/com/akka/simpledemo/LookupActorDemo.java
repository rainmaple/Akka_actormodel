package com.akka.simpledemo;

import akka.actor.*;
        import akka.event.Logging;
        import akka.event.LoggingAdapter;

/**
 * 建立所要索引的目标Actor
 */
class TargetActorf extends UntypedActor{
    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("target received : "+message);
    }
}

/**
 * 建立索引 使用的Actor（索引器）
 * 在其中使用构造快建立索引目标的引用
 * 回调函数中使用口令匹配方式进入功能模块
 * 想要使用目标的ActorRef需要先向ActorSelection发送一个Identity验证消息
 * 验证通过对方自动返回Identity对象包含ActorRef
 */
class LookupActor extends UntypedActor{
    private ActorRef target =null;
    //编写构造块每实例化一次就会运行一次
    {
        target =getContext().actorOf(Props.create(TargetActorf.class),"targetActor");
    }
    @Override
    public void onReceive(Object message) throws Exception{
        if(message instanceof String){
            if("find".equals(message)){
                ActorSelection as =getContext().actorSelection("targetActor");
                as.tell(new Identify("A001"),getSelf());
                // System.out.println("message received by lookuper");
            }
        }
        else if (message instanceof ActorIdentity){
            ActorIdentity ai =(ActorIdentity) message;
            if(ai.correlationId().equals("A001")){
                ActorRef ref =ai.getRef();
                if(ref!=null){
                    System.out.println("ActorIdentity: "+ai.correlationId()+" "+ref);
                    ref.tell("hello i am target",getSelf());
                }
            }
            else{
                unhandled(message);
            }
        }
    }
}


public class LookupActorDemo extends UntypedActor{
    private LoggingAdapter log = Logging.getLogger(this.getContext().system(),this);
    public static void main(String args[]){
        ActorSystem system = ActorSystem.create("sys");
        ActorRef sender= system.actorOf(Props.create(LookupActorDemo.class),"sender");
        ActorRef lookup= system.actorOf(Props.create(LookupActor.class),"lookup");
        lookup.tell("find",sender);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            log.info(message.toString());
        }else{
            unhandled(message);
        }
    }
}
