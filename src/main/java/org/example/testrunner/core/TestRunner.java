package org.example.testrunner.core;

import org.example.testrunner.annotations.AfterEach;
import org.example.testrunner.annotations.AfterSuite;
import org.example.testrunner.annotations.BeforeEach;
import org.example.testrunner.annotations.BeforeSuite;
import org.example.testrunner.annotations.Disabled;
import org.example.testrunner.annotations.Order;
import org.example.testrunner.errors.BadTestClassError;
import org.example.testrunner.errors.TestAssertionError;
import org.example.testrunner.model.Test;
import org.example.testrunner.model.TestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class TestRunner {

    private TestRunner() {
    }

    public static Map<TestResult, List<Test>> runTests(Class<?> c) {
        if (c == null) {
            throw new BadTestClassError("Передан null вместо класса с тестами");
        }

        Object testInstance = createInstance(c);
        Method[] declaredMethods = c.getDeclaredMethods();

        List<Method> beforeEachMethods = new ArrayList<>();
        List<Method> afterEachMethods = new ArrayList<>();
        List<Method> beforeSuiteMethods = new ArrayList<>();
        List<Method> afterSuiteMethods = new ArrayList<>();
        List<TestMethodMeta> testMethods = new ArrayList<>();

        for (Method method : declaredMethods) {
            collectMethod(c, method, beforeEachMethods, afterEachMethods, beforeSuiteMethods, afterSuiteMethods, testMethods);
        }

        Comparator<Method> byName = Comparator.comparing(Method::getName);
        beforeEachMethods.sort(byName);
        afterEachMethods.sort(byName);
        beforeSuiteMethods.sort(byName);
        afterSuiteMethods.sort(byName);
        testMethods.sort(
                Comparator.comparingInt(TestMethodMeta::order)
                        .thenComparing(Comparator.comparingInt(TestMethodMeta::priority).reversed())
                        .thenComparing(TestMethodMeta::name)
        );

        Map<TestResult, List<Test>> results = createResultMap();

        invokeSuiteMethods(beforeSuiteMethods);

        for (TestMethodMeta testMethod : testMethods) {
            if (testMethod.disabled()) {
                addResult(results, new Test(TestResult.Skipped, testMethod.name(), null));
                continue;
            }
            addResult(results, executeOneTest(testInstance, testMethod, beforeEachMethods, afterEachMethods));
        }

        invokeSuiteMethods(afterSuiteMethods);
        return results;
    }

    private static void collectMethod(
            Class<?> testClass,
            Method method,
            List<Method> beforeEachMethods,
            List<Method> afterEachMethods,
            List<Method> beforeSuiteMethods,
            List<Method> afterSuiteMethods,
            List<TestMethodMeta> testMethods
    ) {
        method.setAccessible(true);
        boolean isStatic = Modifier.isStatic(method.getModifiers());

        if (method.isAnnotationPresent(BeforeEach.class)) {
            validateNotStatic(testClass, method, "@BeforeEach");
            beforeEachMethods.add(method);
        }
        if (method.isAnnotationPresent(AfterEach.class)) {
            validateNotStatic(testClass, method, "@AfterEach");
            afterEachMethods.add(method);
        }
        if (method.isAnnotationPresent(BeforeSuite.class)) {
            validateStatic(testClass, method, "@BeforeSuite");
            beforeSuiteMethods.add(method);
        }
        if (method.isAnnotationPresent(AfterSuite.class)) {
            validateStatic(testClass, method, "@AfterSuite");
            afterSuiteMethods.add(method);
        }
        if (method.isAnnotationPresent(org.example.testrunner.annotations.Test.class)) {
            if (isStatic) {
                throw new BadTestClassError(
                        "Некорректный класс " + testClass.getName() + ": @Test нельзя ставить на static метод " + method.getName()
                );
            }
            org.example.testrunner.annotations.Test testAnn = method.getAnnotation(org.example.testrunner.annotations.Test.class);
            int priority = testAnn.priority();
            validatePriority(testClass, method, priority);

            Order orderAnn = method.getAnnotation(Order.class);
            int order = orderAnn != null ? orderAnn.value() : 5;
            validateOrder(testClass, method, order);

            String displayName = testAnn.value().isBlank() ? method.getName() : testAnn.value();
            boolean disabled = method.isAnnotationPresent(Disabled.class);
            testMethods.add(new TestMethodMeta(method, displayName, priority, order, disabled));
        }
    }

    private static void validateNotStatic(Class<?> testClass, Method method, String annotationName) {
        if (Modifier.isStatic(method.getModifiers())) {
            throw new BadTestClassError(
                    "Некорректный класс " + testClass.getName() + ": " + annotationName
                            + " нельзя ставить на static метод " + method.getName()
            );
        }
    }

    private static void validateStatic(Class<?> testClass, Method method, String annotationName) {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new BadTestClassError(
                    "Некорректный класс " + testClass.getName() + ": " + annotationName
                            + " должен быть только на static методе " + method.getName()
            );
        }
    }

    private static void validatePriority(Class<?> testClass, Method method, int priority) {
        if (priority < 0 || priority > 10) {
            throw new BadTestClassError(
                    "Некорректный класс " + testClass.getName() + ": @Test.priority должен быть от 0 до 10 у метода "
                            + method.getName()
            );
        }
    }

    private static void validateOrder(Class<?> testClass, Method method, int order) {
        if (order < 1 || order > 10) {
            throw new BadTestClassError(
                    "Некорректный класс " + testClass.getName() + ": @Order.value должен быть от 1 до 10 у метода "
                            + method.getName()
            );
        }
    }

    private static Object createInstance(Class<?> c) {
        try {
            Constructor<?> constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new BadTestClassError(
                    "Невозможно создать объект класса " + c.getName() + ": отсутствует конструктор без аргументов",
                    e
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BadTestClassError(
                    "Невозможно создать объект класса " + c.getName() + ": " + e.getMessage(),
                    e
            );
        }
    }

    private static void invokeSuiteMethods(List<Method> methods) {
        for (Method method : methods) {
            try {
                method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BadTestClassError("Ошибка выполнения suite-метода " + method.getName(), unwrap(e));
            }
        }
    }

    private static Test executeOneTest(
            Object instance,
            TestMethodMeta testMethod,
            List<Method> beforeEachMethods,
            List<Method> afterEachMethods
    ) {
        Throwable rootCause = null;

        try {
            invokeInstanceMethods(instance, beforeEachMethods);
            testMethod.method().invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            rootCause = unwrap(e);
        } finally {
            try {
                invokeInstanceMethods(instance, afterEachMethods);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Throwable afterEachCause = unwrap(e);
                if (rootCause == null) {
                    rootCause = afterEachCause;
                } else {
                    rootCause.addSuppressed(afterEachCause);
                }
            }
        }

        if (rootCause == null) {
            return new Test(TestResult.Success, testMethod.name(), null);
        }
        if (rootCause instanceof TestAssertionError) {
            return new Test(TestResult.Failed, testMethod.name(), rootCause);
        }
        return new Test(TestResult.Error, testMethod.name(), rootCause);
    }

    private static void invokeInstanceMethods(Object instance, List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    private static Throwable unwrap(Exception exception) {
        if (exception instanceof InvocationTargetException) {
            Throwable targetException = ((InvocationTargetException) exception).getTargetException();
            if (targetException != null) {
                return targetException;
            }
        }
        return exception;
    }

    private static Map<TestResult, List<Test>> createResultMap() {
        Map<TestResult, List<Test>> results = new EnumMap<>(TestResult.class);
        for (TestResult value : TestResult.values()) {
            results.put(value, new ArrayList<>());
        }
        return results;
    }

    private static void addResult(Map<TestResult, List<Test>> results, Test test) {
        results.get(test.getResult()).add(test);
    }

    private record TestMethodMeta(
            Method method,
            String name,
            int priority,
            int order,
            boolean disabled
    ) {
    }
}
