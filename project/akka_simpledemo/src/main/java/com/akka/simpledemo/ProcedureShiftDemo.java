package com.akka.simpledemo;

import Entity.EmpEntity;
import akka.actor.UntypedActor;
import akka.japi.Procedure;

/***
 * 封装两个处理行为，利用关键数据的变化来切换处理行为
 * 使用Procedure<Object>来对行为进行封装，抽取完毕后可使用become/unbecome 来对行为进行切换
 * unbecome 默认切换回上一个行为
 * EmpEntity为实体类的封装Bean
 * 对于Demo的测试类为ProcedureShiftDemoTest
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
                //System.out.println("测试薪水点"+emp.getName()+"  "+emp.getSalary());
                double result = emp.getSalary()*1.8;
                System.out.println("员工"+emp.getName()+"的奖金为："+result);

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
                System.out.println("员工"+emp.getName()+"的奖金为："+result);

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

