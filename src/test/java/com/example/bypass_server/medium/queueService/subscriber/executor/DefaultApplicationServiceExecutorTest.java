package com.example.bypass_server.medium.queueService.subscriber.executor;

import com.example.bypass_server.queueService.subscriber.executor.DefaultApplicationServiceExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;


public class DefaultApplicationServiceExecutorTest {

    DefaultApplicationServiceExecutor serviceExecutor = new DefaultApplicationServiceExecutor(null);

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
    void 여러_파라미터() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        int a = 1;
        int b = 2;
        int c = 3;
        // when
        Object testMultipleParam2 = serviceExecutor.execute(testClass, "testMultipleParam2", a, b, c);
        // then
        Assertions.assertTrue((boolean)testMultipleParam2);
    }

    @Test
    void 객체_파라미터() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        TestClass.InnerClass innerClass = new TestClass.InnerClass(1);
        // when
        Object result = serviceExecutor.execute(testClass, "testObjectParam", innerClass);
        // then
        Assertions.assertEquals(((TestClass.InnerClass)result).anInt, innerClass.anInt);
    }

    @Test
    void 복합_파라미터() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        TestClass.InnerClass innerClass = new TestClass.InnerClass(0);
        // when
        Object result = serviceExecutor.execute(testClass, "testEtcParam", innerClass, 1, 3);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    void 복합_파라미터2() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        TestClass.InnerClass innerClass = new TestClass.InnerClass(0);
        // when
        Long a = 1L;
        Long b = 2L;
        int c = 3;
        boolean d = true;
        Object result = serviceExecutor.execute(testClass, "testMultipleParam2", a, b, c, d);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    void 존재하지_않는_함수() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // given
        TestClass testClass = new TestClass();
        TestClass.InnerClass innerClass = new TestClass.InnerClass(0);
        // when, then
        Assertions.assertThrows(NoSuchMethodException.class ,() -> {
            serviceExecutor.execute(testClass, "testEtcParam", innerClass, 1, 3, 5, 6);
        });
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
        public Long testMultipleParam2(Long a, Long b, int c, boolean d) {
            return a + b + c + (d ? 1: 0);
        }

        public boolean testMultipleParam2(Integer i1, Integer i2, int i3) { return true; }
        public InnerClass testObjectParam(InnerClass innerClass) {
            return innerClass;
        }
        public InnerClass testEtcParam(InnerClass innerClass, int a, Integer b) {
            int c = a+b;
            return new InnerClass(c);
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
