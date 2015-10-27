package junit.framework;

import org.junit.runner.Describable;
import org.junit.runner.Description;
//单元测试用例的门面模式
//实现的接口有Test Describable
public class JUnit4TestCaseFacade implements Test, Describable {
    //维护了一个Description的对象
    private final Description fDescription;

    JUnit4TestCaseFacade(Description description) {
        fDescription = description;
    }

    @Override
    public String toString() {
        return getDescription().toString();
    }

    //默认是一对一的关系
    public int countTestCases() {
        return 1;
    }

    public void run(TestResult result) {
        //这个对象的创建仅仅是信息描述的目的
        throw new RuntimeException(
                "This test stub created only for informational purposes.");
    }

    //获取描述的对象
    public Description getDescription() {
        return fDescription;
    }
}