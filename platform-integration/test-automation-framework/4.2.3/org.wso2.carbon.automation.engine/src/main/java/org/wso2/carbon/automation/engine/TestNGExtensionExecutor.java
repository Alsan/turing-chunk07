package org.wso2.carbon.automation.engine;

import org.wso2.carbon.automation.engine.context.extensions.TestClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestNGExtensionExecutor {
    public void executeServices(List<TestClass> classList)
            throws InstantiationException, ClassNotFoundException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (classList.size() > 0) {
            for (TestClass userTestClass : classList) {
                Class cls;
                TestClass testClass = userTestClass;
                cls = Class.forName(testClass.getClassPath());
                Object object = cls.newInstance();
                Method initMethod = cls.getDeclaredMethod(FrameworkConstants.LISTENER_INIT_METHOD);
                initMethod.invoke(object);
                Method exec = cls.getDeclaredMethod(FrameworkConstants.LISTENER_EXECUTE_METHOD);
                exec.invoke(object);
            }
        }
    }
}