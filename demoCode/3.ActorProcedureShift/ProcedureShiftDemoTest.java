ProcedureShiftDemo
import Entity.EmpEntity;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * ������
 * ������Ϊ������л�
 */
public class ProcedureShiftDemoTest extends UntypedActor{
    public static void main(String args[]){
        ActorSystem system =ActorSystem.create("sys");
        ActorRef ref =system.actorOf(Props.create(ProcedureShiftDemo.class),
                "ProcedureShiftDemo");
        ref.tell("1",ActorRef.noSender());
        ref.tell(new EmpEntity("С��",10000),ActorRef.noSender());
        ref.tell(new EmpEntity("С��",20000),ActorRef.noSender());
        ref.tell("end",ActorRef.noSender());
        ref.tell("2",ActorRef.noSender());
        ref.tell(new EmpEntity("С��",10000),ActorRef.noSender());
        ref.tell(new EmpEntity("С��",20000),ActorRef.noSender());
       // ref.tell("end",ActorRef.noSender());
    }

    @Override
    public void onReceive(Object message) throws Throwable {

    }
}