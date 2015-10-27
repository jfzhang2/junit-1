package junit.framework;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * A {@code TestFailure} collects a failed test together with
 * the caught exception.
 * 伴随着捕捉的异常，收集一个失败的Test的对象
 * @see TestResult
 */
public class TestFailure {
    //指定的失败的Test的对象
    protected Test fFailedTest;
    //抛出的异常
    protected Throwable fThrownException;

    /**
     * Constructs a TestFailure with the given test and exception.
     */
    public TestFailure(Test failedTest, Throwable thrownException) {
        fFailedTest = failedTest;
        fThrownException = thrownException;
    }

    /**
     * Gets the failed test.
     * 获取失败的Test的对象
     */
    public Test failedTest() {
        return fFailedTest;
    }

    /**
     * Gets the thrown exception.
     * 获取抛出的异常
     */
    public Throwable thrownException() {
        return fThrownException;
    }

    /**
     * Returns a short description of the failure.
     */
    @Override
    public String toString() {
        return fFailedTest + ": " + fThrownException.getMessage();
    }
    
    /**
     * Returns a String containing the stack trace of the error
     * thrown by TestFailure.
     * 返回一个字符串  包含错误的堆栈跟踪的信息
     */
    public String trace() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        thrownException().printStackTrace(writer);
        return stringWriter.toString();
    }

    /**
     * Returns a String containing the message from the thrown exception.
     */
    public String exceptionMessage() {
        return thrownException().getMessage();
    }

    /**
     * Returns {@code true} if the error is considered a failure
     * (i.e. if it is an instance of {@code AssertionFailedError}),
     * {@code false} otherwise.
     */
    public boolean isFailure() {
        return thrownException() instanceof AssertionFailedError;
    }
}