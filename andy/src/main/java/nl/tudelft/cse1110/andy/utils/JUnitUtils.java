package nl.tudelft.cse1110.andy.utils;

import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class JUnitUtils {

    public static String simplifyTestErrorMessage(TestExecutionSummary.Failure failure) {
        if (failure.getException().toString()
                .contains("Cannot invoke non-static method")) {
            String failingMethod = getFailingMethod(failure);

            return "Make sure your corresponding method " + failingMethod + " is static!";
        } else if (failure.getException().toString()
                .contains("You must configure at least one set of arguments"))    {
            return "Make sure you have provided a @MethodSource for this @ParameterizedTest!";
        }
        return failure.getException().toString();
    }

    public static String getParameterizedMethodName(TestExecutionSummary.Failure failure) {
        int endIndex = failure.getTestIdentifier().getLegacyReportingName().indexOf('(');
        return failure.getTestIdentifier().getLegacyReportingName().substring(0, endIndex);
    }


    public static String getParameterizedTestCaseNumber(TestExecutionSummary.Failure failure) {
        int open = failure.getTestIdentifier().getLegacyReportingName().lastIndexOf('[');
        int close = failure.getTestIdentifier().getLegacyReportingName().lastIndexOf(']');

        return failure.getTestIdentifier().getLegacyReportingName().substring(open+1, close);
    }

    public static String getFailingMethod(TestExecutionSummary.Failure failure) {
        int open = failure.getException().toString().indexOf('>');
        int close = failure.getException().toString().indexOf(']');

        return failure.getException().toString().substring(open+2, close);
    }


}
