package junit.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * A <code>TestResult</code> collects the results of executing
 * a test case. It is an instance of the Collecting Parameter pattern.
 * The test framework distinguishes between <i>failures</i> and <i>errors</i>.
 * A failure is anticipated and checked for with assertions. Errors are
 * unanticipated problems like an {@link ArrayIndexOutOfBoundsException}.
 * 一个TestResult的对象包含这个一个测试用例的执行的结果。他是一个收集参数的模式的对象。
 * 一个测试框架辨别在失败与错误之间。一个失败被理解与检查对于一些断言。
 * 错误是很难被理解的问题，比如数组的越界等。
 * @see Test
 */
public class TestResult {
    //测试失败的结合
    protected List<TestFailure> fFailures;
    //测试错误的集合
    protected List<TestFailure> fErrors;
    //测试监听的观察者
    protected List<TestListener> fListeners;
    //标记用例的运行
    protected int fRunTests;
    //判断当前的Test是否已经停止
    private boolean fStop;

    public TestResult() {
        //初始化成员变量
        fFailures = new ArrayList<TestFailure>();
        fErrors = new ArrayList<TestFailure>();
        fListeners = new ArrayList<TestListener>();
        fRunTests = 0;
        fStop = false;
    }

    /**
     * Adds an error to the list of errors. The passed in exception
     * caused the error.
     * 添加一个错误到错误的集合
     */
    public synchronized void addError(Test test, Throwable e) {
        fErrors.add(new TestFailure(test, e));
        for (TestListener each : cloneListeners()) {
            //同时观察者进行回调
            each.addError(test, e);
        }
    }

    /**
     * Adds a failure to the list of failures. The passed in exception
     * caused the failure.
     */
    public synchronized void addFailure(Test test, AssertionFailedError e) {
        fFailures.add(new TestFailure(test, e));
        for (TestListener each : cloneListeners()) {
            each.addFailure(test, e);
        }
    }

    /**
     * Registers a TestListener
     */
    public synchronized void addListener(TestListener listener) {
        fListeners.add(listener);
    }

    /**
     * Unregisters a TestListener
     */
    public synchronized void removeListener(TestListener listener) {
        fListeners.remove(listener);
    }

    /**
     * Returns a copy of the listeners.
     */
    private synchronized List<TestListener> cloneListeners() {
        List<TestListener> result = new ArrayList<TestListener>();
        result.addAll(fListeners);
        return result;
    }

    /**
     * Informs the result that a test was completed.
     */
    public void endTest(Test test) {
        for (TestListener each : cloneListeners()) {
            //观察者回调  Test运行完成
            each.endTest(test);
        }
    }

    /**
     * Gets the number of detected errors.
     */
    public synchronized int errorCount() {
        return fErrors.size();
    }

    /**
     * Returns an Enumeration for the errors
     */
    public synchronized Enumeration<TestFailure> errors() {
        return Collections.enumeration(fErrors);
    }


    /**
     * Gets the number of detected failures.
     */
    public synchronized int failureCount() {
        return fFailures.size();
    } 

    /**
     * Returns an Enumeration for the failures
     */
    public synchronized Enumeration<TestFailure> failures() {
        return Collections.enumeration(fFailures);
    }

    /**
     * Runs a TestCase.
     * 运行一个测试用例
     */
    protected void run(final TestCase test) {
        //启动当前的测试
        startTest(test);
        Protectable p = new Protectable() {
            public void protect() throws Throwable {
                //按序号进行运行
                test.runBare();
            }
        };
        //开始运行对象
        runProtected(test, p);
        //结束运行
        endTest(test);
    }

    /**
     * Gets the number of run tests.
     */
    public synchronized int runCount() {
        return fRunTests;
    }

    /**
     * Runs a TestCase.
     */
    public void runProtected(final Test test, Protectable p) {
        try {
            p.protect();
        } catch (AssertionFailedError e) {
            addFailure(test, e);
        } catch (ThreadDeath e) { // don't catch ThreadDeath by accident
            throw e;
        } catch (Throwable e) {
            addError(test, e);
        }
    }

    /**
     * Checks whether the test run should stop
     */
    public synchronized boolean shouldStop() {
        return fStop;
    }

    /**
     * Informs the result that a test will be started.
     * 通知结果  一个Test对象将会被启动
     */
    public void startTest(Test test) {
        final int count = test.countTestCases();
        synchronized (this) {
            fRunTests += count;
        }
        for (TestListener each : cloneListeners()) {
            each.startTest(test);
        }
    }

    /**
     * Marks that the test run should stop.
     */
    public synchronized void stop() {
        fStop = true;
    }

    /**
     * Returns whether the entire test was successful or not.
     */
    public synchronized boolean wasSuccessful() {
        return failureCount() == 0 && errorCount() == 0;
    }
}
