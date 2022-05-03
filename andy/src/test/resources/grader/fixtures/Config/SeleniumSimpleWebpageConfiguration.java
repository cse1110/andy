package delft;

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
}
