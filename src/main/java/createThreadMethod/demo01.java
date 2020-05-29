package createThreadMethod;/*
name: demo01
user: ly
Date: 2020/5/29
Time: 14:08
*/
/////////测试：synchronized与wait（即java中的PV操作）
public class demo01 {

    static volatile Object obj1 = new Object();
    static volatile Object obj2 = new Object();

    public static void main(String []args) throws InterruptedException{
        test4();
    }
    //会死锁  说明：即使obj2是在obj1内部获取的，当调用obj1.wait()使得obj1的锁释放时，obj2的锁不会也跟随释放，除非手动释放。
    public static void test1() throws InterruptedException{
        //创建两个线程
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                try{
                    //先获取obj1
                    synchronized (obj1){
                        System.out.println("threadA get the resource1's lock...");

                        //再尝试获取obj2
                        synchronized (obj2){
                            System.out.println("threadA get the resource2'lock...");

                            //再释放obj1的锁
                            System.out.println("threadA release the resource1' lock");
                            obj1.wait();

                        }
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        //创建另一个线程
        Thread threadB = new Thread(new Runnable() {
            public void run() {
                try{
                    //先休眠1s。让threadA先获取object
                    Thread.sleep(1000);
                    //再尝试去获取OBJ1
                    synchronized (obj1){
                        System.out.println("threadB get the resource1's lock...");
//                       拿到object1的锁后
                        //然后再尝试拿到object2的锁
                        synchronized (obj2){  //应该是拿不到的。因为threadA没有释放
                            System.out.println("threadB get the resource2's lock...");
                            //再释放object1
                            System.out.println("threadB release the resource1's lock");  //应该是放不了
                            obj1.wait();
                        }
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        //启动两个线程
        threadA.start();
        threadB.start();
        //等待线程结束
        threadA.join();
        threadB.join();
        System.out.println("main thread is over...");
    }

    //阻塞后中断
    public static void test2() throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try{
                    System.out.println("----begin-----");
                    synchronized (obj1){
                        obj1.wait();   //自己阻塞自己
                    }
                    System.out.println("----end----");
                }catch (InterruptedException e){
                    System.out.println("interrupt...");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        System.out.println("----begin interrupt sub thread");
        thread.interrupt();
        System.out.println("----end interrupt sub thread");
    }

    public static void test3() throws InterruptedException{
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                //先获取obj1
                try{
                    synchronized (obj1){
                        System.out.println("threadA get the obj1 lock");
                        Thread.sleep(1000);
                        synchronized (obj2){
                            System.out.println("threadA get the obj2 lock");

                        }
                        System.out.println("threadA release the obj2 lock");
//                        obj2.notify();
                    }
//                    obj1.notify();
                    System.out.println("threadA release the obj1 lock");
                }catch (Exception e){

                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            public void run() {
                //先获取obj1
                try {
//                    Thread.sleep(500);
                    synchronized (obj1) {
                        System.out.println("threadB get the obj1 lock");
                        synchronized (obj2) {
                            System.out.println("threadB get the obj2 lock");
//                            obj2.notify();
                        }
//                        obj2.notifyAll();
                        System.out.println("threadB release the obj2 lock");
                    }
                    System.out.println("threadB release the obj1 lock");
//                    obj1.notify();

                } catch (Exception e) {

                }

            }
        });

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        System.out.println("main thread is over...");
    }

    /*
    这个例子证明：当线程被wait()阻塞后，只是剩下wait()语句出现之后的代码段还未执行。
    当被唤醒时，线程会从wait()语句下一条语句开始执行。
    也就是说阻塞的时候，保存了执行点情况
    可也是为什么，notify/notifyAll需要搭配synchronized一起使用
    */
    public static void test4() throws InterruptedException{
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                try{
                    synchronized (obj1){
                        System.out.println("此时获得obj1的公用监视器锁");
                        System.out.println("我还要等别人叫我...");
                        //再将自己挂到obj1 的阻塞队列上。
//                        但是此时会自动释放公用监视器锁
                        obj1.wait();
                        System.out.println("我被唤醒了...");
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            public void run() {
                try{
                    synchronized (obj2){
                        System.out.println("此时获得obj2的公用监视器锁");
//                        obj1.notifyAll();   //这样肯定报错。因为此线程获取的并不是obj1的监视器锁
                    }
                    System.out.println("threadB is over");
                    synchronized (obj1){
                        System.out.println("唤醒阻塞在obj1上的所有阻塞线程");
                        obj1.notifyAll();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        System.out.println("over...");
    }
}
