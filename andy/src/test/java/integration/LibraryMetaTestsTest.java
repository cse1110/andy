package integration;

import nl.tudelft.cse1110.andy.execution.step.RunMetaTestsStep;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class LibraryMetaTestsTest extends BaseMetaTestsTest {

    @Test
    void allMetaTestsPassing() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(4);
        assertThat(result.getMetaTests())
                .has(passedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(passedMetaTest("DoesNotApplyCarryAtAll"))
                .has(passedMetaTest("DoesNotApplyLastCarry"))
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"));
    }

    @Test
    void someMetaTestFailing() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(1);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void someMetaTestFailingWithWeights() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithWeight");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(7);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(2);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void testMetaWhenMultipleClassesInLibrary() {
        Result result = run("SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("BoundaryCheck"))
                .has(passedMetaTest("DoesNotCheckCapacity"))
                .has(passedMetaTest("DoesNotCheckSave"))
                .has(failedMetaTest("DoesNotCheckInvalidTripId"));
    }

    @Test
    void testMetaWhenMultipleClassesInSolution() {
        Result result = run("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOfJQWikConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(3);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("AlwaysReturnsNotFound"))
                .has(passedMetaTest("AlwaysReturnsStartIndex"))
                .has(passedMetaTest("DoesNotUseStartIndex"));
    }

    @Test
    void metaTestInternalFailure() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithMetaTestInternalFailure");

        assertThat(result.hasGenericFailure()).isTrue();
        assertThat(result.getGenericFailure().getStepName())
                .isPresent()
                .get()
                .isEqualTo(RunMetaTestsStep.class.getSimpleName());
    }

}