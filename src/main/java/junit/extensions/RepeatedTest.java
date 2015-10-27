package junit.extensions;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * A Decorator that runs a test repeatedly.
 * 一个装饰器，可以重复的运行一个单元测试的用例
 */
public class RepeatedTest extends TestDecorator {
    //当前的这个用例运行的次数
    private int fTimesRepeat;

    //对应的是构造函数
    //对应的参数是Test的对象以及用例重复执行的次数
    public RepeatedTest(Test test, int repeat) {
        super(test);
        if (repeat < 0) {
            throw new IllegalArgumentException("Repetition count must be >= 0");
        }
        //给用例重复的次数进行赋值
        fTimesRepeat = repeat;
    }

    //当前的单元测试的用例的次数
    @Override
    public int countTestCases() {
        return super.countTestCases() * fTimesRepeat;
    }

    @Override
    public void run(TestResult result) {
        //循环迭代次数运行任务
        for (int i = 0; i < fTimesRepeat; i++) {
            //判断当前的运行的结果是否是需要停止
            if (result.shouldStop()) {
                break;
            }
            //调用父类的run方法
            super.run(result);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "(repeated)";
    }
}