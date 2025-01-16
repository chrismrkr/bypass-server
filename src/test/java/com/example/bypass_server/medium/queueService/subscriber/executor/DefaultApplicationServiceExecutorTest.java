package com.example.bypass_server.medium.queueService.subscriber.executor;

import com.example.bypass_server.queueService.subscriber.executor.DefaultApplicationServiceExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;

@SpringBootTest
public class DefaultApplicationServiceExecutorTest {
    @Autowired
    DefaultApplicationServiceExecutor serviceExecutor;

    @Test
    void 빈_파라미터() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        // when
        Object test = serviceExecutor.execute(testClass, "test", null);
        // then
        Assertions.assertTrue((boolean)test);
    }

    @Test
    void 원시형_파라미터() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        int ret = 100;
        // when
        Object testPrimitive = serviceExecutor.execute(testClass, "testPrimitive", ret);
        // then
        Assertions.assertEquals(ret, (int)testPrimitive);
    }

    @Test
    void 래퍼형_파라미터() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        Integer ret = 100;
        // when
        Object testPrimitive = serviceExecutor.execute(testClass, "testWrapper", ret);
        // then
        Assertions.assertEquals(ret, (Integer) testPrimitive);
    }

    @Test
    void 여러_파라미터() {

    }

    @Test
    void 객체_파라미터() {

    }

    public static class TestClass {
        public boolean test() {
            return true;
        }

        public int testPrimitive(int i) {
            return i;
        }

        public int testWrapper(Integer i) {
            return i;
        }

        public boolean testMultipleParam(int i, String a) {
            return true;
        }

        public InnerClass testObjectParam(InnerClass innerClass) {
            return innerClass;
        }

        public TestClass() {

        }

        private static class InnerClass {
            public int anInt;
            public InnerClass(int anInt) {
                this.anInt = anInt;
            }
        }
    }
}
