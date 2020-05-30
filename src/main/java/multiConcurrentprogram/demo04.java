package multiConcurrentprogram;/*
name: demo04
user: ly
Date: 2020/5/30
Time: 10:42
*/

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

//  比较Atomic*与*Adder的性能差距:
//   test1()测试Atomic*
//   test2()测试*Adder
public class demo04 {
    private static AtomicLong atomicLong = new AtomicLong();
    @SuppressWarnings("unchecking")
    private static LongAdder longAdder = new LongAdder();

    public static void test1(){
        for(int i = 0;i < 1000000;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    atomicLong.incrementAndGet();
                }
            });
            thread.start();
        }
    }
    public static void test2(){
        for(int i = 0;i < 1000000;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    longAdder.increment();
                }
            });
        }
    }
    public static void main(String []args) throws InterruptedException{
        long start = System.currentTimeMillis();
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                test1();
            }
        });

//        Thread thread2 = new Thread(new Runnable() {
//            public void run() {
//                test2();
//            }
//        });
        thread1.start();
//        thread2.start();
        thread1.join();
        long end1 = System.currentTimeMillis();
//        thread2.join();
//        long end2 = System.currentTimeMillis();
        System.out.println("Atomic* :"+(end1-start));
//        System.out.println("*Adder :"+(end2-start));
        System.out.println("main thread is over");
    }
}
