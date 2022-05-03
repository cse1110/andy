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
    public CheckScript checkScript() {
        return new CheckScript(List.of());
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.3f);
            put("meta", 0.3f);
            put("codechecks", 0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withLineReplacement("AlwaysReturnsNotFound", 38, 48, ""),
            MetaTest.withLineReplacement("AlwaysReturnsStartIndex", 38, 49, "return startIndex;"),
            MetaTest.withStringReplacement("DoesNotUseStartIndex",
                """
                if (startIndex < 0) {
                    startIndex = 0;
                }
                for (int i = startIndex; i < array.length; i++) {
                    if (valueToFind == array[i]) {
                        return i;
                    }
                }
                """,
                """
                for (int i = 0; i < array.length; i++) {
                    if (valueToFind == array[i]) {
                        return i;
                    }
                }
                """)
        );
    }

    @Override
    public Mode mode() {
        return Mode.GRADING;
    }
}