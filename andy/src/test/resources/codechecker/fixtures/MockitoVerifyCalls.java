package delft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoVerifyCalls {

    List<String> mockedList2 = mock(List.class);

    @AfterEach
    void after() {
        verify(mockedList2).add("1");
    }


    @Test
    void t1() {
        List<String> mockedList = mock(List.class);

        when(mockedList.add("1")).thenReturn(true);
        verify(mockedList).add("1");
        verify(mockedList).remove("1");
    }

    @Test
    void t2() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList).remove("2");
    }

    @Test
    void t3() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList).contains("2");
        Mockito.verify(mockedList).contains("3");
        Mockito.verify(mockedList).contains("4");
    }

    @Test
    void t4() {
        List<String> mockedList = mock(List.class);

        Mockito.verify(mockedList, Mockito.times(1)).clear();
    }
}
