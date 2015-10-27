package junit.framework;

/**
 * A <em>Protectable</em> can be run and can throw a Throwable.
 * 一个Protectable的对象可以被运行，也可以抛出一个异常的信息
 * @see TestResult
 */
public interface Protectable {

    /**
     * Run the the following method protected.
     * 运行这紧接着的被保护的方法
     */
    public abstract void protect() throws Throwable;
}