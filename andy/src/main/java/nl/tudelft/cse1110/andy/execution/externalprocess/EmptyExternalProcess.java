package nl.tudelft.cse1110.andy.execution.externalprocess;

public class EmptyExternalProcess implements ExternalProcess {
    @Override
    public void launch() {

    }

    @Override
    public void awaitInitialization() {

    }

    @Override
    public void kill() {

    }

    @Override
    public void extractErrorMessages() {

    }

    @Override
    public String getErrorMessages() {
        return null;
    }

    @Override
    public boolean hasExitedNormally() {
        return true;
    }

    @Override
    public int getExitCode() {
        return 0;
    }
}
