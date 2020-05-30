package multiConcurrentprogram;/*
name: demo03
user: ly
Date: 2020/5/30
Time: 9:54
*/

import org.omg.CORBA.INITIALIZE;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicLong;

//  实例：AtomicLong类使用
public class demo03 {

    private static AtomicLong atomicLong = new AtomicLong();

    private static Integer []array1 = new Integer[]{0,1,5,0,6,0,8,7,0,6};
    private static Integer []array2 = new Integer[]{8,5,0,4,6,3,8,9,1,0,0};

    public static void main(String []args) throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                for(int i = 0;i < array1.length;i++){
                    if(array1[i] == 0){
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for(int j = 0;j < array2.length;j++){
                    if(array2[j] == 0){
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("the result is :"+atomicLong.get());
    }
}
