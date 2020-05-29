package multiConcurrentprogram;/*
name: demo01
user: ly
Date: 2020/5/29
Time: 22:11
*/
/////此示例用于：说明volatile关键字在解决共享变量数据不一致问题上的用法
public class demo01 {

    static class ThreadNotSafted{
        private int value;
        private int time;
        public void setValue(int value){
            this.value = value;
        }
        public int getValue(){
            return this.value;
        }
        public int getTime(){
            return time;
        }
        public void addValue(int add){
            this.value += add;
        }
        public void increaseSelf(){
            this.time++;
        }
    }
    static class ThreadSafted{
        private volatile  int value;
        private volatile int time;
        public void setValue(int value){
            this.value = value;
        }
        public int getValue(){
            return this.value;
        }
        public int getTime(){
            return time;
        }
        public void addValue(int add){
            this.value += add;
        }
        public void increaseSelf(){
            this.time++;
        }
    }

    public static void test1() throws InterruptedException{
        int time = 10;
        final ThreadNotSafted threadNotSafted = new ThreadNotSafted();
        threadNotSafted.setValue(0);
        for(int i = 0;i < time;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    threadNotSafted.increaseSelf();

                    int j = ((int)(Math.random()*10));
                    threadNotSafted.setValue(threadNotSafted.getTime());
////                    threadNotSafted.setValue(j);
//                    System.out.println("this time is "+threadNotSafted.getTime()+", the add number is "+j);
//                    threadNotSafted.addValue(j);
                    System.out.println(Thread.currentThread()+"after :"+threadNotSafted.getValue());
                }
            });
//            thread.join();
            int ran = (int)(Math.random()*10);
            if(ran%2 == 0){
                Thread.sleep(1000);
            }
            thread.start();

        }

//        System.out.println(threadNotSafted.getValue());
    }

    public static void test2(){
        int time = 10;
        final ThreadSafted threadSafted = new ThreadSafted();
        threadSafted.setValue(0);
        for(int i = 0;i < time;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    synchronized (threadSafted){
                        threadSafted.increaseSelf();
                        threadSafted.setValue(threadSafted.getTime());
                        System.out.println(Thread.currentThread()+"after :"+threadSafted.getValue());
                    }
                }
            });
            thread.start();

        }

    }
    public static void main(String []args) throws InterruptedException{
            test1();
    }
}
