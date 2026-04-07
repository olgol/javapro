package org.example.testrunner.demo;

import org.example.testrunner.annotations.AfterEach;
import org.example.testrunner.annotations.AfterSuite;
import org.example.testrunner.annotations.BeforeEach;
import org.example.testrunner.annotations.BeforeSuite;
import org.example.testrunner.annotations.Disabled;
import org.example.testrunner.annotations.Order;
import org.example.testrunner.annotations.Test;
import org.example.testrunner.errors.TestAssertionError;

public class DemoTests {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("BeforeSuite");
    }

    @AfterSuite
    public static void afterSuite() {
        System.out.println("AfterSuite");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("BeforeEach");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("AfterEach");
        if ("true".equals(System.getProperty("demo.afterEach.fail"))) {
            throw new RuntimeException("afterEach boom");
        }
    }

    @Test("Успешный тест")
    @Order(1)
    public void successTest() {
        // no-op
    }

    @Test("Проваленный тест")
    @Order(2)
    public void failedTest() {
        throw new TestAssertionError("assert failed");
    }

    @Test("Тест с ошибкой")
    @Order(3)
    public void errorTest() {
        throw new RuntimeException("boom");
    }

    @Test("Пропущенный тест")
    @Disabled
    @Order(4)
    public void skippedTest() {
        // no-op
    }
}
