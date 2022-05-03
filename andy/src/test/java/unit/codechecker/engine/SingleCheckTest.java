package unit.codechecker.engine;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SingleCheckTest {

    private Check check;
    private CompilationUnit unit;

    @BeforeEach
    void setup() {
        check = mock(Check.class);
        unit = mock(CompilationUnit.class);
        when(check.result()).thenReturn(true);
    }

    @Test
    void getFinalResultDoesNotFlipTrue() {

        SingleCheck singleCheck = new SingleCheck(1, "rule", false, check);

        singleCheck.runCheck(null);
        assertThat(singleCheck.getFinalResult()).isTrue();
    }

    @Test
    void getFinalResultDoesNotFlipFalse() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", false, check);

        singleCheck.runCheck(null);
        when(check.result()).thenReturn(false);
        assertThat(singleCheck.getFinalResult()).isFalse();
    }

    @Test
    void getFinalResultFlips() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", true, check);

        singleCheck.runCheck(null);

        assertThat(singleCheck.getFinalResult()).isFalse();
    }

    @Test
    void getFinalResultFlipsFalse() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", true, check);

        singleCheck.runCheck(null);

        when(check.result()).thenReturn(false);
        assertThat(singleCheck.getFinalResult()).isTrue();
    }

    @Test
    void runCheckRunsTheCheck() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", false, check);

        singleCheck.runCheck(unit);

        verify(check).check(unit);
    }

    @Test
    void runWithoutParams() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", false, check);

        singleCheck.runCheck(unit);

        verify(check).check(unit);
    }

}
