package junit.extensions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * A TestSuite for active Tests. It runs each
 * 测试套件对于激活的任务。他会在一个单独的线程中运行每一个
 * 测试用例。等待直到所有的线程已经终止。
 * test in a separate thread and waits until all
 * threads have terminated.
 * -- Aarhus Radisson Scandinavian Center 11th floor
 */
//继承了父类的TestSuite
public class ActiveTestSuite extends TestSuite {
    //维护了一个成员变量fActiveTestDeathCount  记录当前已经执行
    //完成的任务的数量
    private volatile int fActiveTestDeathCount;

    //对应的是一个空的构造函数
    public ActiveTestSuite() {
    }

    //对应的是一个构造函数
    //对应的参数是一个Class的类型
    //同时限定Class内部的泛型必须是TestCase的子类
    public ActiveTestSuite(Class<? extends TestCase> theClass) {
        super(theClass);
    }

    //对应的是一个构造函数
    //参数是字符串的名称  猜测是对应的类的名称
    public ActiveTestSuite(String name) {
        super(name);
    }

    //同时指定两个参数  分别是Class以及字符串的类型
    public ActiveTestSuite(Class<? extends TestCase> theClass, String name) {
        super(theClass, name);
    }

    //对父类中的run方法进行复写
    //其中的参数是TestResult的类型
    @Override
    public void run(TestResult result) {
        //在运行之前的时刻将当前的激活的任务的数量设置为0
        fActiveTestDeathCount = 0;
        super.run(result);
        //等待直到任务完成
        waitUntilFinished();
    }

    //对父类中的run方法进行复写
    //参数分别是Test类型与TestResult的类型
    @Override
    public void runTest(final Test test, final TestResult result) {
        //创建一个线程
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    // inlined due to limitation in VA/Java
                    //ActiveTestSuite.super.runTest(test, result);
                    //调用Test对象的run方法 其中的参数是TestResult的对象
                    test.run(result);
                } finally {
                    //当前的任务完成 回调结束
                    ActiveTestSuite.this.runFinished();
                }
            }
        };
        //启动线程在合适的时候运行
        t.start();
    }

    synchronized void waitUntilFinished() {
        //如果当前已经完成的任务的数目小于整个的单元测试用例的数目
        //锁等待
        while (fActiveTestDeathCount < testCount()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return; // ignore
            }
        }
    }

    synchronized public void runFinished() {
        //每当一个任务完成结束 当前的
        //fActiveTestDeathCount的成员变量递增一次
        fActiveTestDeathCount++;
        notifyAll();
    }
}