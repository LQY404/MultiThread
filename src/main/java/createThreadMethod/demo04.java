package createThreadMethod;/*
name: demo04
user: ly
Date: 2020/5/29
Time: 16:47
*/
//   本实例用于：展示普通的ThreadLocal变量与 inheritanceThreadLcoal之间的区别
public class demo04 {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    private static InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
    public static void main(String []args){
//        ordinaryThreadLocal();

        inheritanceThreadLocal();
    }

    public static void ordinaryThreadLocal(){
        threadLocal.set("the mainThread value");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                //尝试在子线程中获取父线程中的、不可继承ThreadLocal线程变量
                System.out.println(Thread.currentThread()+":"+threadLocal.get());
            }
        });
        thread.start();
        //再尝试在主线程中获取。以做对比
        System.out.println(Thread.currentThread()+":"+threadLocal.get());
    }
    public static void inheritanceThreadLocal(){


        inheritableThreadLocal.set("the mainThread value");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try{

                    Thread.sleep(2000);
                    //尝试在子线程中获取父线程中的、不可继承ThreadLocal线程变量
                    System.out.println(Thread.currentThread()+":"+inheritableThreadLocal.get());
                }catch(InterruptedException e){

                }
            }
        });
        thread.start();
        //再尝试在主线程中获取。以做对比
//        inheritableThreadLocal.set("another main value");    //用这个语句可以看出：这个复制是值的复制，而非引用。
        System.out.println(Thread.currentThread()+":"+inheritableThreadLocal.get());
    }
}
