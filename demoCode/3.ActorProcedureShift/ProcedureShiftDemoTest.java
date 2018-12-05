ProcedureShiftDemo
import Entity.EmpEntity;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * 测试类
 * 测试行为组件的切换
 */
public class ProcedureShiftDemoTest extends UntypedActor{
    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef ref =system.actorOf(Props.create(ProcedureShiftDemo.class),
                "ProcedureShiftDemo");
        ref.tell("1",ActorRef.noSender());
        ref.tell(new EmpEntity("小李",10000),ActorRef.noSender());
        ref.tell(new EmpEntity("小张",20000),ActorRef.noSender());
        ref.tell("end",ActorRef.noSender());
        ref.tell("2",ActorRef.noSender());
        ref.tell(new EmpEntity("小陶",10000),ActorRef.noSender());
        ref.tell(new EmpEntity("小彭",20000),ActorRef.noSender());
       // ref.tell("end",ActorRef.noSender());
    }

    @Override
    public void onReceive(Object message) throws Throwable {

    }
}