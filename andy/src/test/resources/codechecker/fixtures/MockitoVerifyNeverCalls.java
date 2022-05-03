package delft;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

public class MockitoVerifyNeverCalls {

    @Test
    void t2() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList, never()).remove("2");
    }

    @Test
    void t3() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList, Mockito.never()).contains("2");
        Mockito.verify(mockedList, Mockito.never()).contains("3");
        Mockito.verify(mockedList).contains("4");
    }

    @Test
    void t4() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList, Mockito.never()).get(any(Integer.class));
    }

    @Test
    void t5() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList, Mockito.times(0)).isEmpty();
        Mockito.verify(mockedList, times(0)).clear();
    }

}
