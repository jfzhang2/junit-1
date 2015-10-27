package junit.extensions;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * A Decorator for Tests. Use TestDecorator as the base class for defining new
 * 一个针对Test的装饰器，使用TestDecorator作为这个基类，为的是定义新的Test的装饰器
 * test decorators. Test decorator subclasses can be introduced to add behaviour
 * 测试的装饰器的子类可以被引进去增加行为在一个Test运行之前或者是运行之后。
 * before or after a test is run.
 */
//继承的是Assert的类  实现的是Test的接口
@SuppressWarnings("deprecation")
public class TestDecorator extends Assert implements Test {
    //维护成员变量Test
    protected Test fTest;

    //在构造方法中 Test作为参数
    public TestDecorator(Test test) {
        fTest = test;
    }

    /**
     * The basic run behaviour.
     * 基本的run行为方法
     */
    public void basicRun(TestResult result) {
        //然后调用Test的run方法 
        //其中的TestResult作为运行的参数
        fTest.run(result);
    }

    //对当前的单元测试进行计数
    public int countTestCases() {
        //实际上调用的是Test的计数的方法
        return fTest.countTestCases();
    }

    public void run(TestResult result) {
        //实际的运行的方法是basicRun的方法
        basicRun(result);
    }

    @Override
    public String toString() {
        return fTest.toString();
    }

    public Test getTest() {
        //获取当前的实际运行的对象
        return fTest;
    }
}