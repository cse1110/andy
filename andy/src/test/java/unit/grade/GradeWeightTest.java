package unit.grade;

import nl.tudelft.cse1110.andy.grade.GradeWeight;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class GradeWeightTest {

    @Test
    void preCondition() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                        new GradeWeight(0.1f, 0.1f, 0.1f, 0.1f)
        ).withMessageContaining("weight configuration is wrong");

        new GradeWeight(0.25f, 0.25f, 0.25f, 0.25f);
        new GradeWeight(0.33f, 0.33f, 0.33f, 0.01f);
        new GradeWeight(0.33f, 0.33f, 0.34f, 0f);
    }
}
