package nl.tudelft.cse1110.andy.result;

import java.util.Optional;

public class GenericFailure {
    private final String genericFailureMessage;
    private final String stepName;
    private final String exceptionMessage;
    private final Integer externalProcessExitCode;
    private final String externalProcessErrorMessages;

    private GenericFailure(String genericFailureMessage, String stepName, String exceptionMessage, Integer externalProcessExitCode, String externalProcessErrorMessages) {
        this.genericFailureMessage = genericFailureMessage;
        this.stepName = stepName;
        this.exceptionMessage = exceptionMessage;
        this.externalProcessExitCode = externalProcessExitCode;
        this.externalProcessErrorMessages = externalProcessErrorMessages;
    }

    public static GenericFailure build(String genericFailure, String stepName, String exceptionMessage, Integer externalProcessExitCode, String externalProcessErrorMessages) {
        return new GenericFailure(genericFailure, stepName, exceptionMessage, externalProcessExitCode, externalProcessErrorMessages);
    }

    public static GenericFailure noFailure() {
        return new GenericFailure(null, null, null, null, null);
    }

    public boolean hasFailure() {
        boolean genericFailureHappened = genericFailureMessage != null || stepName != null || exceptionMessage != null;
        boolean externalProcessFailed = externalProcessExitCode != null || externalProcessErrorMessages != null;
        return genericFailureHappened || externalProcessFailed;
    }

    public Optional<String> getGenericFailureMessage() {
        return Optional.ofNullable(genericFailureMessage);
    }

    public Optional<String> getStepName() {
        return Optional.ofNullable(stepName);
    }

    public Optional<String> getExceptionMessage() {
        return Optional.ofNullable(exceptionMessage);
    }

    public Optional<Integer> getExternalProcessExitCode() {
        return Optional.ofNullable(externalProcessExitCode);
    }

    public Optional<String> getExternalProcessErrorMessages() {
        return Optional.ofNullable(externalProcessErrorMessages);
    }

    @Override
    public String toString() {
        return "GenericFailure{" +
               "genericFailureMessage='" + genericFailureMessage + '\'' +
               ", stepName='" + stepName + '\'' +
               ", exceptionMessage='" + exceptionMessage + '\'' +
               ", externalProcessExitCode=" + externalProcessExitCode +
               ", externalProcessErrorMessages='" + externalProcessErrorMessages + '\'' +
               '}';
    }
}
