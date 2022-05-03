package delft;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoSpyCalls {


    @Test
    void t1() {
        List<String> mockedList = spy(List.class);

        // ...
    }

}
