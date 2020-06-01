package ReentrantLock;/*
name: demo01
user: ly
Date: 2020/6/1
Time: 9:00
*/
//        实例：使用ReentrantLock来保证ArrayList的线程安全
import java.util.ArrayList;
import java.util.Random;

import java.util.concurrent.locks.ReentrantLock;

//    使用ReentrantLock与线程不安全的ArrayList结合使用，
public class demo01 {
    private final static ArrayList<String> array = new ArrayList<String>();
    private static volatile ReentrantLock reentrantLock = new ReentrantLock();   //非公平锁

    public static void main(String []args) throws InterruptedException{
        Thread mainThread = new Thread(new Runnable() {
            public void run() {
                for(int i = 0;i < 10;i++){
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            int j = (int)(Math.random()*3);
                            if(j == 0){
                                add("elem");
                            }else if(j == 1){
                                remove("elem");
                            }else{
                                int size = Size();
                                int index = (int)(Math.random()*size);
                                String elem = get(index);
                                System.out.println("the array size is "+size+",and get the "+elem+" this time at index :"+index);
                            }
                        }
                    });
                    thread.start();
                }
            }
        });
        mainThread.start();

        mainThread.join();
//        Thread.sleep(3000);
        System.out.println("over....the array size is "+Size()+" finally");
    }

    //在这里不能直接使用ArrayList内部的增删元素的方法
    public static void add(String elem){
        reentrantLock.lock();
        try{
            array.add(elem);
        }finally {
            reentrantLock.unlock();
        }
    }
    public static void remove(String elem){
        reentrantLock.lock();
        try{
            array.remove(elem);
        }finally {
            reentrantLock.unlock();
        }
    }
    public static String get(int index){
        reentrantLock.lock();
        try{
            int size = Size();
            if(index >= size){
                return null;
            }
            return array.get(index);
        }finally {
            reentrantLock.unlock();
        }
    }
    public static int Size(){
        reentrantLock.lock();
        try{
            return array.size();
        }finally {
            reentrantLock.unlock();
        }
    }
}
