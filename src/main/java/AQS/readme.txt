
AQS：抽象同步队列，这是所有锁实现的基础。
该数据结构和操作系统中关于PV操作时的所需的数据结构一样。
首先，其内部有一个状态变量state，用于记录当前的锁是否有线程在是否，并且还有一个Thread变量thread来记录当前使用该锁的线程本身。
可以看出，如果该锁是独占锁，就可以通过state与thread变量的搭配使用达到锁的互斥使用以及锁的可重入性。
同时，其内部还有几个Condition类型的变量，用于记录因为某个condition而阻塞的线程，此时这些线程都挂在相应的condition上。

对于state变量的修改一般都是通过CAS实现。

所以，一个AQS中有两种队列：阻塞队列queue1与条件阻塞队列queues2。
其中的阻塞队列中挂的是在获取锁的时候没有获取到的一些线程；
条件阻塞队列又可以分为多条条件阻塞队列，每一条条件阻塞队列中挂的都是调用 本阻塞队列对应的condition的await()方法的线程。

如果一个线程调用了某个condition的signal()方法，那么会将本condition对应的条件queue2（注意不是queues）中的某一个线程抽出，并将其挂到queue1上。
如果是signalAll()，则将对应的queue2中所有的线程挂到queue1上。

所以，只有当一个线程位于queue1上时（不是初次尝试获取锁），才有机会去获取lock；
否则如果位于queue2上，则除非有线程调用signal()/signalAll()，不然一直挂起。

也可以得到的结论：一把锁只有一个AQS队列queue1，但是可以有多个条件变量，也就对应可以有多个条件阻塞队列queue2。


对于一个锁Lock（比如ReentrantLock锁）而已，每一个锁都可以创建多个Condition变量，
当获得锁（即调用lack()方法）之后，如果调用某个condition的await()方法，就会将该线程挂起，直到有其他的线程调用了该condition的signal()方法。

以synchronized为例，synchronized一个对象的时候，其实就是相当于内部调用了一个lock，将该对象锁住了。
在synchronized语句块中调用wait()方法时，就相当于调用condition的await()方法，同样，调用notify/notifyAll方法相当于调用signal()方法。

