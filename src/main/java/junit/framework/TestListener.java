package junit.framework;

/**
 * A Listener for test progress
 * 测试用例过程的观察者
 */
public interface TestListener {
    /**
     * An error occurred.
     * 当有一个错误发生的时候
     */
    public void addError(Test test, Throwable e);

    /**
     * A failure occurred.
     * 当有一个失败发生的时候
     */
    public void addFailure(Test test, AssertionFailedError e);

    /**
     * A test ended.
     * 当一个测试用例结束
     */
    public void endTest(Test test);

    /**
     * A test started.
     * 当一个测试开启
     */
    public void startTest(Test test);
}