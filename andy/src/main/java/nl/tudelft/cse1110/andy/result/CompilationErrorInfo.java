package nl.tudelft.cse1110.andy.result;

public class CompilationErrorInfo {
    private final String fileName;
    private final long lineNumber;
    private final String message;

    public CompilationErrorInfo(String fileName, long lineNumber, String message) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CompilationErrorInfo{" +
                "fileName='" + fileName + '\'' +
                ", lineNumber=" + lineNumber +
                ", message='" + message + '\'' +
                '}';
    }
}
