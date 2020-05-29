类似于操作系统中的概念，每个线程都是共享其隶属的进程空间，如堆、方法区，
但是同时，每一个线程还是有一点属于自己的资源：程序计数器以及方法栈。

在java的线程设计中，start方法只是使得线程处于就绪状态，只有当执行了run方法才获取cpu控制器、才处于真正的执行状态。
但是一般来说，线程之间都是时间片调度，所以即使run（或者call）方法与start方法是绑定一起执行的，但是run方法一般都是滞后于start方法。


java中的PV操作：
最基本的是使用notify、synchronized以及wait方法来实现。
其中，synchronized关键字可以加在实体对象上（类似try）,也可以加在方法上：
①
synchronized(实体){
    if(条件不足)
        实体.wait();
}
②
synchronized void method(){
    do something
}
当一个实体处于wait时，一般情况下都是需要其他实体通过调用notifyAll方法通知而解除wait状态
在方法上加synchronized关键字时，不需要手动调用wait，也不需要其他实体调用notify，jvm自动调用。

以生产者消费者为例：
现在有一个生产者消费者公用队列queue
生产者：
synchronized(queue){
    while(queue是满的){
        //先放弃对queue的锁。然后：
        queue.wait();
    }
    //此时队列不满
    queue.add(obj);  //生产
    queue.notifyAll()  //发出通知。通知所有的被处于wait态的实体。相当于V操作
}

消费者：
synchronized(queue){
    while(queue是空的){
        //先放弃对queue的锁。然后线程本身等待
        queue.wait();
    }
    //队列不为空
    queue.get(obj);  //消费
    queue.notifyAll()  //发出通知
}


当线程被wait()阻塞后，只是剩下wait()语句出现之后的代码段还未执行。
    当被唤醒时，线程会从wait()语句下一条语句开始执行。
    也就是说线程被阻塞的时候，同时保存了执行点情况
    可也是为什么，notify/notifyAll需要搭配synchronized一起使用



线程的interrupt()方法与isInterrupted()方法：
首先，interrupt()方法内部是通过调用isInterrupted()方法实现，并且：
如果当前线程是阻塞的，调用isInterrupted()方法时理所当然是返回：true；反之false。
而如果当前线程是阻塞的，而且调用interrupt()方法，则会返回：false。
因为：interrupt()方法会改变线程阻塞状态：阻塞态调用则转为就绪唤醒态；反之亦然。即：
interrupt()方法会清除中断标志。


小技巧：
在命令行中使用：jps 命令查看当前在jvm中运行的进程（一般是用户进程）。


普通的ThreadLocal与inheritableThreadLocal之间的异同：
首先，两者都是线程内部的域，每一个线程都有，且私有。
这个私有、同级线程之间都不一样是通过设置值的时候实现的：
通过源码以及继承关系知道，不管是ThreadLocal，还是inheritableThreadLocal，都是一个ThreadLocals的映射，
而这个ThreadLocals底层是由一个特殊哈希函数的HashMap实现的。
而在设置值的时候，key是传入的Thread的引用，value是泛型T。
所以，通过由这个引用设置的key，从而达到私有的目的，这也是ThreadLocal的思路。

但是，如果想要使得子线程像共享父进程（线程）资源那样，共享父线程的ThreadLocals，则需要通过inheritableThreadLocal来实现。
其实inheritableThreadLocal与ThreadLocal的差距就在线程创建的本身：
通过阅读源码可以看到，创建子线程的时候，通过线程的构造函数，先检查父线程的inheritableThreadLocal的有无，
如果不为null，则将子线程的inheritableThreadLocal设置为：
this.inheritableThreadLocal = ThreadLocal.createInheritedMap(parent.inheritableThreadLocals)
其中，parent是父线程。
通过ThreadLocal的静态方法：createInheritedMap()来为子线程的inheritableThreadLocal域赋值，相当于复制。
createInheritedMap()方法：
createInheritedMap(ThreadLocalMap parentMap){
    return new ThreadLocalMap(parentMap);
}
可以看出，这种复制是值的复制，而不是引用复制。






