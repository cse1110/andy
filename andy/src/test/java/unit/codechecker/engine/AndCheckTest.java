package unit.codechecker.engine;

import nl.tudelft.cse1110.andy.codechecker.engine.AndCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.OrCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AndCheckTest {

    private SingleCheck check1;
    private SingleCheck check2;

    private AndCheck andCheck;

    @BeforeEach
    void setup() {
        check1 = mock(SingleCheck.class);
        check2 = mock(SingleCheck.class);

        when(check1.getFinalResult()).thenReturn(true);
        when(check2.getFinalResult()).thenReturn(true);

        andCheck = new AndCheck(1, "orDescription", List.of(check1, check2));
        andCheck.runCheck(null);
    }

    @Test
    void getFinalResultIsTrueIfAllChecksAreTrue() {
        assertThat(andCheck.getFinalResult()).isTrue();
    }

    @Test
    void getFinalResultIsFalseIfAtLeastOneFails() {
        when(check1.getFinalResult()).thenReturn(false);
        assertThat(andCheck.getFinalResult()).isFalse();
    }

    @Test
    void runCheckRunsAllChecks() {
        CompilationUnit unit = mock(CompilationUnit.class);

        andCheck.runCheck(unit);

        verify(check1).runCheck(unit);
        verify(check2).runCheck(unit);
    }

    @Test
    void testSameObjectEquals() {

        boolean result = andCheck.equals(andCheck);

        assertThat(result).isTrue();
    }


    @Test
    void testNotEqualsNull() {

        boolean result = andCheck.equals(null);

        assertThat(result).isFalse();
    }

    @Test
    void testNotEqualsAnotherClass() {

        boolean result = andCheck.equals(OrCheck.class);

        assertThat(result).isFalse();
    }


}
