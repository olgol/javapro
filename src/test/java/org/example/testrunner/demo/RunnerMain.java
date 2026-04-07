package org.example.testrunner.demo;

import org.example.testrunner.core.TestRunner;
import org.example.testrunner.model.Test;
import org.example.testrunner.model.TestResult;

import java.util.List;
import java.util.Map;

public class RunnerMain {
    public static void main(String[] args) {
        Map<TestResult, List<Test>> results = TestRunner.runTests(DemoTests.class);

        for (TestResult result : TestResult.values()) {
            System.out.println();
            System.out.println(result + ":");
            for (Test test : results.get(result)) {
                String msg = test.getThrowable() == null ? "" : " | " + test.getThrowable().getMessage();
                System.out.println(" - " + test.getName() + msg);
                if (test.getThrowable() != null) {
                    Throwable[] suppressed = test.getThrowable().getSuppressed();
                    for (Throwable suppressedThrowable : suppressed) {
                        System.out.println("   suppressed: " + suppressedThrowable.getClass().getSimpleName()
                                + " | " + suppressedThrowable.getMessage());
                    }
                }
            }
        }
    }
}
