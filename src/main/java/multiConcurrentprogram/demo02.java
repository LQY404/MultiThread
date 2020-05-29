package multiConcurrentprogram;/*
name: demo02
user: ly
Date: 2020/5/29
Time: 23:53
*/



import sun.misc.Unsafe;

import java.lang.reflect.Field;

// 测试：Unsafe类
public class demo02 {

//    static final Unsafe unsafe = Unsafe.getUnsafe(); //获取UnSafe类实例   用于 test1()
    static final Unsafe unsafe;                        //用于test2()
    static final long stateOffSet;  //变量在demo02类中的偏移量。CAS方法中要用

    private volatile long state = 0;   //要执行CAS操作的变量
    static{
        try{
//            stateOffSet = unsafe.objectFieldOffset(demo02.class.getDeclaredField("state"));  //利用反射机制获取声明的域    //用于test1()
            //用于test2()
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);   //设置访问权限
            unsafe = (Unsafe)field.get(null);
            stateOffSet = unsafe.objectFieldOffset(demo02.class.getDeclaredField("state"));

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            throw new Error(e);
        }
    }
    //这种情况下会报错：SecurityException。报错位置是UnSafe实例获取的位置。原因可以查看源码。
    public static void test1(){
        demo02 demo02 = new demo02();
        boolean success = unsafe.compareAndSwapLong(demo02, stateOffSet, 0, 1);  //如果是0，就换成1
        System.out.println(success);
    }
    //要让不报错，不能使用UnSafe静态方法获取实例的方法，要使用反射。
    public static void test2(){
        demo02 demo02 = new demo02();
        boolean success = unsafe.compareAndSwapLong(demo02, stateOffSet, 0, 1);  //如果是0，就换成1
        System.out.println(success);
    }
    public static void main(String []args){
        test2();
    }
}
