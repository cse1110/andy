package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.externalprocess.EmptyExternalProcess;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.writer.weblab.SubmissionMetaData;

import java.util.List;

public class Context {

    private final ClassLoader cleanClassloader;
    private DirectoryConfiguration directoryConfiguration = null;
    private RunConfiguration runConfiguration;
    private List<String> fullClassNames;
    private ExecutionFlow flow;
    private Action action;
    private ModeActionSelector modeActionSelector;
    private ExternalProcess externalProcess;
    private List<String> librariesToBeIncluded;
    private SubmissionMetaData submissionMetaData;
    private boolean securityEnabled = true;

    public Context(Action action) {
        this.cleanClassloader = Thread.currentThread().getContextClassLoader();
        this.action = action;
        this.externalProcess = new EmptyExternalProcess();
    }

    public void setFlow(ExecutionFlow flow) {this.flow = flow;}

    public ExecutionFlow getFlow() {
        return flow;
    }

    public DirectoryConfiguration getDirectoryConfiguration() {
        return directoryConfiguration;
    }

    public RunConfiguration getRunConfiguration() {
        return runConfiguration;
    }

    public void setDirectoryConfiguration(DirectoryConfiguration directoryConfiguration) {
        this.directoryConfiguration = directoryConfiguration;
    }

    public void setRunConfiguration(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration;
    }

    public ClassLoader getCleanClassloader() {
        return cleanClassloader;
    }

    public void setNewClassNames(List<String> fullClassNames) {
        this.fullClassNames = fullClassNames;
    }

    public List<String> getNewClassNames() {
        return fullClassNames;
    }

    public Action getAction() {
        return action;
    }

    public void setModeSelector(ModeActionSelector modeActionSelector) {
        this.modeActionSelector = modeActionSelector;
    }

    public ModeActionSelector getModeActionSelector() {
        return modeActionSelector;
    }

    public ExternalProcess getExternalProcess() {
        return externalProcess;
    }

    public void setExternalProcess(ExternalProcess externalProcess) {
        this.externalProcess = externalProcess;
    }

    public void killExternalProcess() {
        // Retrieve error messages before killing process
        externalProcess.extractErrorMessages();

        externalProcess.kill();
    }

    public void setLibrariesToBeIncluded(List<String> librariesToBeIncluded) {
        this.librariesToBeIncluded = librariesToBeIncluded;
    }

    public List<String> getLibrariesToBeIncluded() {
        return librariesToBeIncluded;
    }

    public boolean hasLibrariesToBeIncluded() {
        return librariesToBeIncluded!=null && !librariesToBeIncluded.isEmpty();
    }

    public SubmissionMetaData getSubmissionMetaData() {
        return submissionMetaData;
    }

    public void setSubmissionMetaData(SubmissionMetaData submissionMetaData) {
        this.submissionMetaData = submissionMetaData;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    public void disableSecurity() {
        this.securityEnabled = false;
    }
}
