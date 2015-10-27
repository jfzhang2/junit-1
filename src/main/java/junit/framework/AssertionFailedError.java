package junit.framework;

/**
 * Thrown when an assertion failed.
 * 当一个断言失败的时候抛出
 */
public class AssertionFailedError extends AssertionError {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new AssertionFailedError without a detail message.
     * 没有异常信息描述的断言异常的对象的构建
     */
    public AssertionFailedError() {
    }

    /**
     * Constructs a new AssertionFailedError with the specified detail message.
     * 用指定的详细的信息构造一个新的断言异常的对象
     * A null message is replaced by an empty String.
     * @param message the detail message. The detail message is saved for later 
     * retrieval by the {@code Throwable.getMessage()} method.
     */
    public AssertionFailedError(String message) {
        super(defaultString(message));
    }

    private static String defaultString(String message) {
        return message == null ? "" : message;
    }
}