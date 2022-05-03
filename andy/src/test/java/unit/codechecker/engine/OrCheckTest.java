package unit.codechecker.engine;

import nl.tudelft.cse1110.andy.codechecker.engine.AndCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.OrCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrCheckTest {

    @ParameterizedTest
    @CsvSource({"true,true,true","true,false,true","false,true,true", "false,false,false"})
    void trueIfBothAreTrue(boolean c1, boolean c2, boolean expected) {
        SingleCheck check1 = mock(SingleCheck.class);
        SingleCheck check2 = mock(SingleCheck.class);

        when(check1.getFinalResult()).thenReturn(c1);
        when(check2.getFinalResult()).thenReturn(c2);

        OrCheck orCheck = new OrCheck(1, "orDescription", List.of(check1, check2));

        CompilationUnit cu = mock(CompilationUnit.class);
        orCheck.runCheck(cu);

        verify(check1).runCheck(cu);
        verify(check2).runCheck(cu);
        assertThat(orCheck.getFinalResult()).isEqualTo(expected);
    }

}
