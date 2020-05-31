package concurrentLinkQueue;/*
name: demo01
user: ly
Date: 2020/5/30
Time: 16:48
*/

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

//   :比较concurrentLinkedQueue与linkedBlockingQueue差别
public class demo01 {
    private static ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<Integer>();
    private static LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<Integer>();

    public static void test1(){
        for(int i = 0;i < 100000;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    int j = (int)(Math.random()*10);
                    concurrentLinkedQueue.add(j);
                }
            });
            thread.start();
        }

    }

    public static void test2(){
        for(int i = 0;i < 100000;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    int j = (int)(Math.random()*10);
                    linkedBlockingQueue.add(j);
                }
            });
            thread.start();
        }
    }

    public static void main(String []args) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            public void run() {
//                test1();
                test2();
            }
        });
        long start = System.currentTimeMillis();
        thread.start();
        thread.join();
        long end = System.currentTimeMillis();
//        System.out.println("the concurrentLinkedQueue consume time is :"+(end-start));
        System.out.println("the LinkedBlockingQueue consume time is :"+(end-start));
    }

}
