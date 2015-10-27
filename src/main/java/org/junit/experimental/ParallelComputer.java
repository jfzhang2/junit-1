package org.junit.experimental;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runner.Computer;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;
//并行计算 继承了Computer这个类
public class ParallelComputer extends Computer {
    //是否是类
    private final boolean classes;
    //是否是方法
    private final boolean methods;

    public ParallelComputer(boolean classes, boolean methods) {
        this.classes = classes;
        this.methods = methods;
    }

    public static Computer classes() {
        //创建一个默认是类的ParallelComputer的对象
        return new ParallelComputer(true, false);
    }

    public static Computer methods() {
        return new ParallelComputer(false, true);
    }

    //开始进行并行运行
    private static Runner parallelize(Runner runner) {
        if (runner instanceof ParentRunner) {
            //设置调度器
            ((ParentRunner<?>) runner).setScheduler(new RunnerScheduler() {
                //创建线程池
                private final ExecutorService fService = Executors.newCachedThreadPool();

                //进行相关的调度
                public void schedule(Runnable childStatement) {
                    fService.submit(childStatement);
                }

                public void finished() {
                    try {
                        //关闭线程池的调度
                        fService.shutdown();
                        //等待终止
                        fService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
        return runner;
    }

    @Override
    public Runner getSuite(RunnerBuilder builder, java.lang.Class<?>[] classes)
            throws InitializationError {
        Runner suite = super.getSuite(builder, classes);
        return this.classes ? parallelize(suite) : suite;
    }

    @Override
    protected Runner getRunner(RunnerBuilder builder, Class<?> testClass)
            throws Throwable {
        Runner runner = super.getRunner(builder, testClass);
        return methods ? parallelize(runner) : runner;
    }
}
