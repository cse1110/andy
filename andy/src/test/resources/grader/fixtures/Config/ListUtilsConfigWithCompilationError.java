package delft;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {


    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.5f);
            put("mutation", 0.5f);
            put("meta", 0.0f);
            put("codechecks", 0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ListUtils");
    }

    @Override
    public Mode mode() {
        return Mode.GRADINGx;
    }
}