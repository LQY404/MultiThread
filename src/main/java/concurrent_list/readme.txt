java中的List类型中，只有CopyOnWriteArrayList是线程安全的ArrayList。

在copyOnWriteList中，基本底层还是不变：由数组构成的链表，有一个Object[]数组域。
但是在其内部有一个ReentrantLock独占锁，在增删改的时候都是先上锁再操作。
所以它是并发安全的。
①
在实现的时候，都是先先将数组域复制到一个新数组中，然后对这个新数组进行增删改，最后将新数组赋给旧数组。
②
在进行迭代iterator时，实际上是对内部数组域快照的一个迭代，如果这个数组域被修改，迭代还是按照先前的值进行迭代。
详情见demo01.java
