package AQS;/*
name: demo01
user: ly
Date: 2020/5/31
Time: 16:53
*/

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class demo01{

    final static NonReentrantLock lock = new NonReentrantLock();
    final static Condition notFull = lock.newCondition();
    final static Condition notEmpty = lock.newCondition();
    final static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();  //生产者消费者队列

    final static int queueSize = 10;
    public static void main(String []args){
        Thread producer = new Thread(new Runnable() {
            public void run() {
//                try{
//                    Thread.sleep(2000);
//                }catch (InterruptedException e){

//                }
                System.out.println("start work...");
                //拿锁
                lock.lock();   //                                        P   lock
                System.out.println("producer get the lock");
                try{
                    while(queue.size() == queueSize){   //注意是while而非if
                        //队满
                        System.out.println("is full...");
                        notEmpty.await();  //将本线程加入满条件队列      P   notEmpty
                    }
                    //不满
                    System.out.println("produce: elem");
                    queue.add("elem");
                   notFull.signalAll();       //  通知消费者      //     V   notFull
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("producer release the lock");

                    lock.unlock();  //必须要释放锁，不管队满还是不满     P   lock
                }
            }
        });
        //消费者线程差不多
        Thread consumer = new Thread(new Runnable() {
            public void run() {
                System.out.println("start consumer...");
                //同样，拿锁
                lock.lock();             //                             P  lock
                System.out.println("consumer get the lock");
                try{
                    while(queue.size() == 0){
                        //队空
                        System.out.println("is empty...");
                        notFull.await();                         //     P  notFull
                    }
                    //消费一下
                    String elem = queue.poll();
                    System.out.println("consumer: "+elem);
                    notEmpty.signalAll();   //通知生产者                V  notEmpty
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("consumer release the lock");
                    lock.unlock();              //                      V  lock
                }
            }
        });

        producer.start();
        consumer.start();

    }


}

//独占式、不可重入的自定义锁

class NonReentrantLock implements Lock, Serializable{
    //内部AQS类   作为实现锁的基础类
    private static class Sync extends AbstractQueuedSynchronizer{

        //当前锁是否已被占有
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
//

        @Override
        protected boolean tryAcquire(int arg) {
            assert arg == 1;  //断言
//            使用CAS修改state
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            //CAS失败
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            assert arg == 1;
            if(getState() == 0){
//                此时并没有线程占用锁，再调用释放锁就抛出异常
                throw new IllegalMonitorStateException();
            }
            //此时有线程占着锁
            setExclusiveOwnerThread(null);  //将占有锁的线程域改为null
            setState(0);   //并重置state
            return true;
        }
        //条件域
        Condition newCondition(){
            return new ConditionObject();
        }
    }
    private Sync sync = new Sync();  //创建一个AQS作为该自定义锁的载体
    public void lock() {
        sync.tryAcquire(1);
    }
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }
    public void unlock() {
        sync.tryRelease(1);
    }
    public Condition newCondition() {
        return sync.newCondition();
    }
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

}
