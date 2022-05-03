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
            put("coverage", 0.25f);
            put("mutation", 0.25f);
            put("meta", 0.25f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SoftWhere");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withStringReplacement("BoundaryCheck",
                """
                if (capacityLeft(trip) < people.size())
                    return false;
                """,
                """
                if (capacityLeft(trip) <= people.size())
                    return false;
                """),
            MetaTest.withStringReplacement("DoesNotCheckCapacity",
                """
                if (capacityLeft(trip) < people.size())
                    return false;
                """,""),
            MetaTest.withLineReplacement("DoesNotCheckInvalidTripId", 150, 158,
                """
                try {
                    Trip trip = tRepository.getTripById(tripId);
                    if (capacityLeft(trip) < people.size()) return false;
                    rRepository.save(new Reservation(trip, people));
                    return true;
                } catch (ElementNotFoundException e) {
                    throw new RuntimeException("killed the mutant");
                }
                """),
            MetaTest.withLineReplacement("DoesNotCheckSave", 154, 154, "")
        );
    }
}