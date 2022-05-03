package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.externalprocess.CommandExternalProcess;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.EmptyLibrary");
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.0f);
            put("mutation", 0.0f);
            put("meta", 1.0f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public ExternalProcess externalProcess() {
        String tmp = System.getProperty("java.io.tmpdir");
        return new CommandExternalProcess(
                "python3 -m http.server 8086 -d " + tmp + "/andy_test_external_process_local_connection",
                null);
    }

    @Override
    public List<MetaTest> metaTests() {
        String tmp = System.getProperty("java.io.tmpdir");
        return List.of(
                MetaTest.withExternalProcess(2, "example of a passing meta test",
                        new CommandExternalProcess(
                                "python3 -m http.server 8086 -d " + tmp + "/andy_test_external_process_local_connection_meta_test_pass_1",
                                null)
                ),
                MetaTest.withExternalProcess(2, "example of a failing meta test",
                        new CommandExternalProcess(
                                "python3 -m http.server 8086 -d " + tmp + "/andy_test_external_process_local_connection",
                                null)
                ),
                MetaTest.withExternalProcess("example of another passing meta test",
                        new CommandExternalProcess(
                                "python3 -m http.server 8086 -d " + tmp + "/andy_test_external_process_local_connection_meta_test_pass_2",
                                null)
                )
        );
    }

    @Override
    public boolean skipJacoco() {
        return true;
    }

    @Override
    public boolean skipPitest() {
        return true;
    }
}
