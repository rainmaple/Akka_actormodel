import Entity.EmpEntity;
import akka.actor.UntypedActor;
import akka.japi.Procedure;

/***
 * ��װ����������Ϊ�����ùؼ����ݵı仯���л�������Ϊ
 * ʹ��Procedure<Object>������Ϊ���з�װ����ȡ��Ϻ��ʹ��become/unbecome ������Ϊ�����л�
 * unbecome Ĭ���л�����һ����Ϊ
 * EmpEntityΪʵ����ķ�װBean
 * ����Demo�Ĳ�����ΪProcedureShiftDemoTest
 */
public class ProcedureShiftDemo extends UntypedActor {

    Procedure<Object> LEVEL1 =new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if(message instanceof String){
                if(message.equals("end")){
                    getContext().unbecome();
                }
            }
            else{
                EmpEntity emp = (EmpEntity) message;
                //System.out.println("����нˮ��"+emp.getName()+"  "+emp.getSalary());
                double result = emp.getSalary()*1.8;
                System.out.println("Ա��"+emp.getName()+"�Ľ���Ϊ��"+result);

            }
        }
    };
    Procedure<Object> LEVEL2 =new Procedure<Object>() {
        @Override
        public void apply(Object message) throws Exception {
            if(message instanceof String){
                if(message.equals("end")){
                    getContext().unbecome();
                }
            }
            else{
                EmpEntity emp = (EmpEntity) message;
                double result = emp.getSalary()*1.5;
                System.out.println("Ա��"+emp.getName()+"�Ľ���Ϊ��"+result);

            }
        }
    };
    @Override
    public void onReceive(Object message) throws Exception {
        String level = (String) message;
        if(level.equals("1")){
            getContext().become(LEVEL1);
        }else if(level.equals("2")){
            getContext().become(LEVEL2);
        }

    }
}