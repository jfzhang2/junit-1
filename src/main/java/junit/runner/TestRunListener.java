package junit.runner;

/**
 * A listener interface for observing the
 * execution of a test run. Unlike TestListener,
 * this interface using only primitive objects,
 * making it suitable for remote test execution.
 * 一个文本的接口去监视这个Test的运行。
 * 与TestListener不同的是，这个接口使用仅有的原子对象
 * 是的其能够对于远程的Test的执行
 */
public interface TestRunListener {
    /* test status constants*/
    //状态常量
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_FAILURE = 2;

    public void testRunStarted(String testSuiteName, int testCount);

    public void testRunEnded(long elapsedTime);

    public void testRunStopped(long elapsedTime);

    public void testStarted(String testName);

    public void testEnded(String testName);

    public void testFailed(int status, String testName, String trace);
}
