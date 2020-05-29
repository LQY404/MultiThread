package createThreadMethod;


//使用继承Thread类的方法实现创建线程的目的。
//优点是：比较简单，可以在自定义Thread子类中添加各种域以及方法。
//缺点是：①执行任务单一。线程本身与线程要执行的任务硬绑定。
//        ②无返回值
public class OneThread extends Thread{
    @Override
    public void run() {
        System.out.println("the first method that create the multiThread.....");
    }

    public static void main(String []args){

        Thread oneThread = new OneThread();
        oneThread.start();
    }
}
