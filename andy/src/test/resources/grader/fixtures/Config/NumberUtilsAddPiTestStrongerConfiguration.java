package delft;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of());
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.4f);
            put("meta", 0.3f);
            put("codechecks", 0.2f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.NumberUtils");
    }

    @Override
    public List<String> listOfMutants() {
        return STRONGER;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of();
    }
}