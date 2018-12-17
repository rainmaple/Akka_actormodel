package com.akka.simpledemo;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

public class Actordemo extends UntypedActor {
    private LoggingAdapter log  = Logging.getLogger(this.getContext().system(),this);
    @Override
        public void onReceive(Object msg) throws Exception{
        if(msg instanceof String){
            log.info(msg.toString());
        }else{
            unhandled(msg);
        }
    }

}

class PropsDemoActor extends UntypedActor{
    @Override
    public void onReceive(Object msg) throws Exception{
    }
    public static Props createProps(){
        //实现creator工厂接口传入props.create方法
        return Props.create(new Creator<Actor>() {
            @Override
            public PropsDemoActor create() throws Exception {
                return new PropsDemoActor();
            }
        });
    }
}