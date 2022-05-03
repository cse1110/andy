package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

class TheQueueTest {

    private RequestService requestService;

    private TheQueue q;

    @BeforeEach
    void setup() {
        requestService = Mockito.mock(RequestService.class);
        q = new TheQueue(requestService);
    }


    @Test
    void getNextThrowsOnAbsent() {
        Mockito.when(requestService.getRequestsByCourse())
                .thenReturn(Map.of("CN", create("student42", "student5"), "ProbStat", create("student13")));
        assertThrows(IllegalArgumentException.class, () -> q.getNext("SQT"));
    }

    @Test
    void getNextThrowsOnEmpty() {
        Mockito.when(requestService.getRequestsByCourse()).thenReturn(
                Map.of("CN", create("student42", "student5"), "ProbStat", create("student13"), "SQT", create()));
        assertThrows(NoSuchElementException.class, () -> q.getNext("SQT"));
    }


    // Student is misusing Mockito stubs in line 48: TheQueue q is not a mock, thus its methods cannot be stubbed!
    @Test
    void getNextReturnsFirst() {
        Mockito.when(requestService.getRequestsByCourse()).thenReturn(Map.of("CN", create("student42", "student5"),
                "ProbStat", create("student13"), "SQT", create("student11", "student3")));
        Mockito.when(q.getNext("SQT")).thenReturn("student11");
        assertThat(q.getNext("SQT")).isEqualTo("student11");
    }

    private Queue<String> create(String... requests) {
        return new LinkedList<>(Arrays.asList(requests));
    }
}
