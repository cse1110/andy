package delft;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

interface RequestService {

    /**
     * Returns a map with courses as keys and a list of requests as values. The
     * requests in each list are ordered chronologically in ascending order.
     *
     * @return the requests grouped by course and ordered chronologically
     */
    Map<String, Queue<String>> getRequestsByCourse();
}

class TheQueue {

    private final RequestService requestService;

    public TheQueue(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Gets the next request in the Queue for a specific course.
     *
     * @param course
     *            the course for which to get the next request
     * @return the next request
     */
    String getNext(String course) {
        Map<String, Queue<String>> requests = requestService.getRequestsByCourse();
        if (!requests.containsKey(course))
            throw new IllegalArgumentException("Course does not exist.");
        if (requests.get(course).isEmpty())
            throw new NoSuchElementException("There are no new requests.");
        return requests.get(course).poll();
    }
}
