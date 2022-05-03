package delft;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.3f);
            put("meta", 0.4f);
            put("codechecks", 0.2f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.NumberUtils");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withStringReplacement(3, "AppliesMultipleCarriesWrongly",
                """
                int sum = leftDigit + rightDigit + carry;

                result.addFirst(sum % 10);
                carry = sum / 10;
                """,
                """
                int sum;

                if (leftDigit + rightDigit >= 10) {
                    sum = leftDigit + rightDigit;
                    carry = 1;
                }
                else {
                    sum = leftDigit + rightDigit + carry;
                    carry = 0;
                }
                result.addFirst(sum % 10);
                """),
            MetaTest.withLineReplacement("DoesNotApplyCarryAtAll", 47, 68,
                """
                for (int i = 0; i < Math.max(reversedLeft.size(), reversedRight.size()); i++) {

                    int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
                    int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;

                    if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                        throw new IllegalArgumentException();

                    int sum = leftDigit + rightDigit;

                    result.addFirst(sum % 10);
                }

                // remove leading zeroes from the result
                while (result.size() > 1 && result.get(0) == 0)
                    result.remove(0);

                if (result.isEmpty()) {
                    result.addFirst(0);
                }

                return result;
                """),
            MetaTest.withStringReplacement("BadMetaTest",
                "something that doesn't exist",
                    ""),
            MetaTest.withLineReplacement(2, "DoesNotCheckNumbersOutOfRange", 52, 53, "")
        );
    }
}