package createThreadMethod;/*
name: TwoThread
user: ly
Date: 2020/5/29
Time: 11:16
*/
//使用实现Runnable接口来创建多线程
//优点为：线程本身与线程执行的任务彻底分离。
//缺点也很明显：无返回值。

public class TwoThread {

    public static class RunnableTask implements Runnable{
        public void run() {
            System.out.println("the second method that create the multiThread.....");
        }
    }

    public static void main(String []args){
        Runnable task1 = new RunnableTask();
        Runnable task2 = new RunnableTask();

        new Thread(task1).start();
        new Thread(task2).start();
    }

}
