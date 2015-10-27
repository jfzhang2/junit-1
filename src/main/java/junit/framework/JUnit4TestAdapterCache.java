package junit.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
//测试用例的适配器的缓存
public class JUnit4TestAdapterCache extends HashMap<Description, Test> {
    private static final long serialVersionUID = 1L;
    //对应的是适配器缓存的单例
    private static final JUnit4TestAdapterCache fInstance = new JUnit4TestAdapterCache();

    //返回默认的单例的对象
    public static JUnit4TestAdapterCache getDefault() {
        return fInstance;
    }

    //将描述符经过一定的包装 返回的是一个Test的对象
    public Test asTest(Description description) {
        if (description.isSuite()) {
            return createTest(description);
        } else {
            //缓存策略
            if (!containsKey(description)) {
                put(description, createTest(description));
            }
            return get(description);
        }
    }

    //如何将一个Description对象进行包装  形成一个Test的对象
    Test createTest(Description description) {
        if (description.isTest()) {
            //用的是门面模式
            return new JUnit4TestCaseFacade(description);
        } else {
            //
            TestSuite suite = new TestSuite(description.getDisplayName());
            for (Description child : description.getChildren()) {
                suite.addTest(asTest(child));
            }
            return suite;
        }
    }

    public RunNotifier getNotifier(final TestResult result, final JUnit4TestAdapter adapter) {
        //构造一个RunNotifier的对象
        RunNotifier notifier = new RunNotifier();
        //添加观察者
        //出错 完成 开始的回调
        notifier.addListener(new RunListener() {
            @Override
            public void testFailure(Failure failure) throws Exception {
                result.addError(asTest(failure.getDescription()), failure.getException());
            }

            @Override
            public void testFinished(Description description) throws Exception {
                result.endTest(asTest(description));
            }

            @Override
            public void testStarted(Description description) throws Exception {
                result.startTest(asTest(description));
            }
        });
        return notifier;
    }

    //将描述符的对象转换为Test的对象
    public List<Test> asTestList(Description description) {
        //首先判断是否是Test的对象
        if (description.isTest()) {
            return Arrays.asList(asTest(description));
        } else {
            List<Test> returnThis = new ArrayList<Test>();
            for (Description child : description.getChildren()) {
                returnThis.add(asTest(child));
            }
            return returnThis;
        }
    }

}