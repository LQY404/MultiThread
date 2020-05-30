package LockSupport;/*
name: demo01
user: ly
Date: 2020/5/30
Time: 11:43
*/
//   实例：LockSupport的pack()与unPark()
import java.util.concurrent.locks.LockSupport;

public class demo01 {

    public static void main(String []args){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                System.out.println("the child thread is running...");
                //自己挂起自己
                LockSupport.park();
                System.out.println("the child thread is over...");
            }
        });
        thread.start();
        System.out.println("main thread is running...");
        //main线程唤醒child线程
        LockSupport.unpark(thread);    //如果注释掉，child thread就会永远挂起。
        System.out.println("main thread is over");
    }
}
