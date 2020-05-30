package concurrent_list;/*
name: demo01
user: ly
Date: 2020/5/30
Time: 11:11
*/

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

//   CopyAndWriteArrayList的迭代弱一致性
public class demo01 {
    private static CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<String>();


    public static void main(String []args) throws InterruptedException{
        copyOnWriteArrayList.add("a");
        copyOnWriteArrayList.add("b");
        copyOnWriteArrayList.add("c");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                copyOnWriteArrayList.remove("c");
                copyOnWriteArrayList.set(1,"changed");
            }
        });

        //在启动线程之前获取迭代器
        Iterator<String> iterable = copyOnWriteArrayList.iterator();
        thread.start();  //现在才启动
        thread.join();
        while (iterable.hasNext()){
            System.out.println(iterable.next());
        }
        System.out.println("actual:");
        iterable = copyOnWriteArrayList.iterator();
        while (iterable.hasNext()){
            System.out.println(iterable.next());
        }

    }
}
