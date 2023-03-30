import HTTPClientException.HTTPClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPClient {
    public String request(String url) {
        try {
            URI endpoint = URI.create(url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(endpoint).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException exception) {
            String httpClientExceptionMessage = "Error when trying to request url " + url;

            if(exception.getMessage() != null) {
                httpClientExceptionMessage = httpClientExceptionMessage + ". " + exception.getMessage();
            }

            throw new HTTPClientException(httpClientExceptionMessage);
        }
    }
}
