package junit.framework;

/**
 * A <em>Test</em> can be run and collect its results.
 * 一个Test可以被运行以及收集自己的结果
 * @see TestResult
 */
public interface Test {
    /**
     * Counts the number of test cases that will be run by this test.
     * 计数通过这个测试用例的数目将会通过这个测试运行
     */
    public abstract int countTestCases();

    /**
     * Runs a test and collects its result in a TestResult instance.
     * 运行一个测试并在一个TestResult的对象中收集结果
     */
    public abstract void run(TestResult result);
}