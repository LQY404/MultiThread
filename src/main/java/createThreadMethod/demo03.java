package createThreadMethod;/*
name: demo03
user: ly
Date: 2020/5/29
Time: 16:10
*/


import org.omg.CORBA.INITIALIZE;

//  该demo用于示例： 死锁。
public class demo03 {
    private static Object object1 = new Object();
    private static Object object2 = new Object();

    public static void main(String []args){
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                try{
                    System.out.println("threadA is getting the object1...");
                    synchronized (object1){
                        System.out.println("threadA get the object1!");
                        System.out.println("threadA is getting the object2...");
                        Thread.sleep(2000);  //睡眠，以便在当前线程获取object2之前，另一个线程就将其占据
                        //睡眠后获取object2
                        synchronized (object2){
                            System.out.println("threadA get the object2!");
                        }
                    }
                }catch (InterruptedException e){
                    System.out.println("threadA is interrupted when it is sleeping or waitting...");
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            public void run() {
                try{
                    System.out.println("threadB is getting the object2...");
                    synchronized (object2){
                        System.out.println("threadB get the object2!");
                        System.out.println("threadB is getting the object1...");
                        Thread.sleep(2000);  //睡眠，
                        //睡眠后获取object1
                        synchronized (object1){
                            System.out.println("threadB get the object1!");
                        }
                    }
                }catch (InterruptedException e){
                    System.out.println("threadB is interrupted when it is sleeping or waitting...");
                }
            }
        });

        threadA.start();
        threadB.start();

        System.out.println("the main thread is over when it is not use the function join");


    }

}
