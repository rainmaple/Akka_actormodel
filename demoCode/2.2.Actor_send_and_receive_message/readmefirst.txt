运行Demo之前首先你需要导入akka的maven依赖

需要明确的还有对于已经存在的Actor我们可以根据路径进行查找
API 为
ActorSelection =[ActorSystem/ActorContext].actorSelection([path]);

在调用的时候需要指定绝对或者相对亦或是远程路径
/user/parent/childActor
../parentActor/childActor
akka.tcp://mysys@127.0.0.1:2554/user/parentActor/childActor