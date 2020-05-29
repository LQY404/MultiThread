package createThreadMethod;/*
name: demo02
user: ly
Date: 2020/5/29
Time: 15:26
*/
//该demo用于说明：当线程sleep的时候并不会放弃锁
//tips:   yield方法与sleep方法类似。差别在于：
//    yield方法调用后，当前线程让出CPU，即使当前线程还有时间片未用完。
//    此时系统再次调度。此时可能会再次调度到刚刚使用yield方法的那个线程。
//    而使用sleep时，当前线程也放弃CPU处于就绪态，但是直到sleep时间结束前都不会被调度到
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class demo02 {
    static final Lock lock = new ReentrantLock();
    static final Object obj1 = new Object();

    public static void main(String []args){
        test1();
        test2();
    }

    //测试：sleep时是否会释放普通lock
    public static void test1(){
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                //先拿到锁
                System.out.println("threadA get the lock");
                lock.lock();
                try{
                    System.out.println("threadA is sleeping....");
                    Thread.sleep(10000);
                    System.out.println("threadA is awaking");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("threadA release the lock");
                    lock.unlock();
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            public void run() {
                System.out.println("threadB get the lock");
                //会等待10秒
                lock.lock();
                try{
                    System.out.println("threadB is sleeping....");
                    Thread.sleep(10000);
                    System.out.println("threadB is awaking");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("threadB release the lock");
                    lock.unlock();
                }
            }
        });

        threadA.start();
        threadB.start();
        System.out.println("haven't the function join, the main thread is over firstly");
    }

    //测试：sleep时是否会释放监视器锁
    public static void test2(){
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                System.out.println("threadA getting the monitor lock");
                try{
                    synchronized (obj1){
                        System.out.println("threadA is sleeping");
                        Thread.sleep(5000);
                        System.out.println("threadA is awaking");
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            public void run() {
                System.out.println("threadB getting the monitor lock");
                try{
                    synchronized (obj1){
                        System.out.println("threadBis sleeping");
//                        Thread.sleep(5000);
                        System.out.println("threadB is awaking");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        threadA.start();
        threadB.start();
        System.out.println("haven't the function join, the main thread is over firstly");
    }

}
