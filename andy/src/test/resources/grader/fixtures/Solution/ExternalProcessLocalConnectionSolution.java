package delft;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ExternalProcessLocalConnectionTests {

    @Test
    void responseTest() throws IOException, InterruptedException {

        // Give the external process time to initialise.

        // This would not be necessary outside of Andy integration tests since Selenium is much sloer than HttpClient,
        // and Spring applications provide output when they start while the Python web server used in this test does not
        // properly indicate that it has started.
        Thread.sleep(1000);

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create("http://localhost:8086/")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response).isNotNull();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("hello");
    }
}
