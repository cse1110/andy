package unit.execution.metatest;

import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.metatest.externalprocess.ExternalProcessMetaTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ExternalProcessMetaTestTest {

    private ExternalProcess mockExternalProcess;
    private ExternalProcessMetaTest externalProcessMetaTest;

    @BeforeEach
    public void setup() {
        this.mockExternalProcess = mock(ExternalProcess.class);
        this.externalProcessMetaTest = new ExternalProcessMetaTest(1, "test", this.mockExternalProcess);
    }

    @AfterEach
    public void assertNoMoreExternalProcessInteractions() {
        verifyNoMoreInteractions(this.mockExternalProcess);
    }

    @Test
    public void startExternalProcessTest() throws IOException {
        // Act
        this.externalProcessMetaTest.startExternalProcess();

        // Assert
        InOrder order = inOrder(mockExternalProcess);
        order.verify(this.mockExternalProcess).launch();
        order.verify(this.mockExternalProcess).awaitInitialization();
    }

    @Test
    public void killExternalProcessTest() {
        // Act
        this.externalProcessMetaTest.killExternalProcess();

        // Assert
        verify(this.mockExternalProcess).kill();
    }
}
