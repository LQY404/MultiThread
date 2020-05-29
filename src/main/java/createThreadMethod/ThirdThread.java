package createThreadMethod;/*
name: ThirdThread
user: ly
Date: 2020/5/29
Time: 11:36
*/

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThirdThread{
    public static class MyCallableTask implements Callable{
        public Object call() throws Exception {   //注意第三种方法与前面两种的差别：重写run方法 VS 重写call方法
            return "\"the third method that create the multiThread.....\"";
        }
    }
    public static void main(String []args){
        //使用FutureTask(与第二种方法--使用Runnable的不同
        FutureTask<String> futureTask = new FutureTask<String>(new MyCallableTask());

        //先启动。但是此时该线程不一定立即执行
        new Thread(futureTask).start();
        try{
            //此时等待执行,并且从线程执行的任务：MycallableTask中可以看到，线程执行后会返回一个Object，这里定义为String
            String result = futureTask.get();
            System.out.println(result);
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
