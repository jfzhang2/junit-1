package junit.extensions;

import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * A Decorator to set up and tear down additional fixture state. Subclass
 * 首先，这是一个装饰器，去建立与销毁额外的固定的状态。
 * TestSetup and insert it into your tests when you want to set up additional
 * TestUp的子类，并且将其插入到你的测试中，当你希望建立额外的状态，一旦在测试运行事前
 * state once before the tests are run.
 */
public class TestSetup extends TestDecorator {

    //构造函数是以Test为对象
    public TestSetup(Test test) {
        super(test);
    }

    //run方法是TestResult为对象
    @Override
    public void run(final TestResult result) {
        //创建一个Protectable的对象
        //内部对protect的方法
        Protectable p = new Protectable() {
            public void protect() throws Exception {
                //建立状态
                setUp();
                //开始运行任务
                basicRun(result);
                //对状态进行销毁
                tearDown();
            }
        };
        //调用TestResult的runProtected方法 
        //触发protect的方法
        result.runProtected(this, p);
    }

    /**
     * Sets up the fixture. Override to set up additional fixture state.
     * 建立固定的状态，重写去建立额外的固定的状态
     */
    protected void setUp() throws Exception {
    }

    /**
     * Tears down the fixture. Override to tear down the additional fixture
     * 销毁固定的状态，重写去销毁这个额外的固定的状态
     * state.
     */
    protected void tearDown() throws Exception {
    }
}