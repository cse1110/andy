package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.1f);
            put("meta", 0.8f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.Splitting");
    }

    @Override
    public List<String> listOfMutants() {
        return STRONGER;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement(1, "array with odd length v1", 21, 22,
                        """
                        if (nums == null || nums.length == 0)
                            return false;
                        
                        if (nums.length % 2 == 1)
                            return true;
                        """),
                MetaTest.withLineReplacement(1, "array with odd length v2", 21, 22,
                        """
                        if (nums == null || nums.length == 0)
                            return false;
                        
                        if (nums.length % 2 == 1)
                            return false;
                        """),
                MetaTest.withStringReplacement(1, "no null check",
                        """
                        if (nums == null || nums.length <= 1)
                                    return false;
                        """,
                        """
                        if (nums == null) return true;
                        
                        if (nums.length <= 1)
                            return false;
                        """),
                MetaTest.withLineReplacement(2, "only considers sum", 26, 32,
                        "return (sum % 2 == 0);"),
                MetaTest.withStringReplacement(1, "only does one loop iteration",
                        "half > 0;",
                        "i < 1;"),
                MetaTest.withLineReplacement(2, "split in the middle", 23, 33,
                        """
                        return sum(nums, 0, nums.length / 2) == sum(nums, nums.length / 2, nums.length)
                                || sum(nums, 0, nums.length / 2 + 1) == sum(nums, nums.length / 2 + 1, nums.length);
                        }
                        
                        private static int sum(int[] nums, int lowerBound, int upperBound) {
                            int result = 0;
                            for (int i = lowerBound; i < upperBound; i++)
                                result += nums[i];
                            return result;
                        }
                        """),
                MetaTest.withLineReplacement(1, "two equal elements", 21, 32,
                        "return (nums != null && nums.length == 2 && nums[0] == nums[1]);"),
                MetaTest.withLineReplacement(1, "wrong result for one element", 21, 22,
                        """
                        if (nums == null || nums.length == 0)
                            return false;
                        
                        if (nums.length == 1)
                            return true;
                        """),
                MetaTest.withLineReplacement(1, "wrong result when empty", 21, 22,
                        """
                        if (nums == null || nums.length == 1)
                            return false;
                        
                        if (nums.length == 0)
                            return true;
                        """)
        );
    }

}