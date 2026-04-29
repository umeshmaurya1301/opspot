import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiTest {

    private static final String API_KEY = "AIzaSyA1cYleNjSWG0oNhsR8QrFsuvFxRuaFC3Y";
    private static final String MODEL   = "gemini-3.1-pro-previewcl";
    private static final String URL     = "https://generativelanguage.googleapis.com/v1beta/models/"
            + MODEL + ":generateContent?key=" + API_KEY;

    public static void main(String[] args) throws Exception {

        String requestBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        { "text": "Say hello and tell me which Gemini model you are." }
                      ]
                    }
                  ]
                }
                """;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code : " + response.statusCode());
        System.out.println("Response    : " + response.body());
    }
}