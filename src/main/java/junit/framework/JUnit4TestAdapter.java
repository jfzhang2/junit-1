package junit.framework;

import java.util.List;

import org.junit.Ignore;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
//实现了Test Filterable Sortable Describable等相关的接口
public class JUnit4TestAdapter implements Test, Filterable, Sortable, Describable {
    //新的单元测试的Class的对象
    private final Class<?> fNewTestClass;
    //对应的是Runner的对象
    private final Runner fRunner;

    //对应的是缓存
    private final JUnit4TestAdapterCache fCache;

    public JUnit4TestAdapter(Class<?> newTestClass) {
        //缓存用的是默认的缓存的对象
        this(newTestClass, JUnit4TestAdapterCache.getDefault());
    }

    public JUnit4TestAdapter(final Class<?> newTestClass, JUnit4TestAdapterCache cache) {
        fCache = cache;
        fNewTestClass = newTestClass;
        //通过Class获取Runner的方法
        fRunner = Request.classWithoutSuiteMethod(newTestClass).getRunner();
    }

    public int countTestCases() {
        //计算测试用例额数量
        return fRunner.testCount();
    }

    public void run(TestResult result) {
        //从缓存中获取RunNotifier对象来运行
        fRunner.run(fCache.getNotifier(result, this));
    }

    // reflective interface for Eclipse
    public List<Test> getTests() {
        return fCache.asTestList(getDescription());
    }

    // reflective interface for Eclipse
    public Class<?> getTestClass() {
        return fNewTestClass;
    }

    //获取描述字段的对象
    public Description getDescription() {
        Description description = fRunner.getDescription();
        return removeIgnored(description);
    }

    private Description removeIgnored(Description description) {
        if (isIgnored(description)) {
            //返回空的描述的字段
            return Description.EMPTY;
        }
        Description result = description.childlessCopy();
        for (Description each : description.getChildren()) {
            Description child = removeIgnored(each);
            if (!child.isEmpty()) {
                result.addChild(child);
            }
        }
        return result;
    }

    private boolean isIgnored(Description description) {
        //判断注解是否不是空
        return description.getAnnotation(Ignore.class) != null;
    }

    @Override
    public String toString() {
        return fNewTestClass.getName();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        //对Runner进行过滤
        filter.apply(fRunner);
    }

    public void sort(Sorter sorter) {
        //对Runner进行排序
        sorter.apply(fRunner);
    }
}