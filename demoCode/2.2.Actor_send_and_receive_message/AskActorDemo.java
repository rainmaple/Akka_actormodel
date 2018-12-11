package Actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/***
 * tell方法发完立即返回，异步的向actor发送消息
 * ask方法：用于向actor询问（请求）执行的结果
 * 将返回结果包装在Scala.concurrent.Future里面
 * 我们通过异步回调来获取这个结果
 */

public class AskActorDemo extends UntypedActor {


    //为了创建消息的发送者也方便能够得到返回的消息我们需要引用Sender

    @Override
    public void onReceive(Object msg) throws Exception{
        System.out.println("Sender is "+getSender());
        getSender().tell("hello "+msg,getSelf());
    }


    public static void main(String args[]) {
        ActorSystem system = ActorSystem.create("sys");//较为重量级所以一个应用程序中只需要创建一个
        ActorRef ask_ar = system.actorOf(Props.create(AskActorDemo.class), "askActorDemo");

        Timeout timeout = new Timeout(Duration.create(2, "seconds"));//传入超时时间，以便进行超时返回处理
        //patterns提供ask方法返回一个Future对象，提供onSuccess和onFailure两个回调函数用来处理返回数据以及异常信息
        //pattern.ask(actorRef,message need to send,timeout)
        Future<Object> f = Patterns.ask(ask_ar, "messge content in Actor which Akka ask for", timeout);
        System.out.println("asking...");
        f.onSuccess(new OnSuccess<Object>(){
            @Override
            public void onSuccess(Object result){
                System.out.println("received message:"+result);
            }

        },system.dispatcher());
        System.out.println("continue..");
    }

}

