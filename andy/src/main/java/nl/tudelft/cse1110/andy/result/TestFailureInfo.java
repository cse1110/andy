package nl.tudelft.cse1110.andy.result;

public class TestFailureInfo {
    private final String testCase;
    private final String message;

    public TestFailureInfo(String testCase, String message) {
        this.testCase = testCase;
        this.message = message;
    }

    public String getTestCase() {
        return testCase;
    }

    public String getMessage() {
        return message;
    }


    public boolean hasMessage() {
        return message!=null && !message.isEmpty();
    }

    @Override
    public String toString() {
        return "TestFailureInfo{" +
                "testCase='" + testCase + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
