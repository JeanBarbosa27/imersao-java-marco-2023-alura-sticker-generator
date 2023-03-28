import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Do an HTTP connection to search for top 250 movies
        // creates a unique point of request
        String imdbApiKey = System.getenv("IMDB_API_KEY");

        /*
            should be interesting:
                - move this part of requesting to IMDB to a service
                - create a class or service to handle http requests
        */

        String baseUrl = "https://imdb-api.com/";
        String lang = "pt";
        String apiNamespace = "/API/";
        String top250MoviesPath = "Top250Movies/";
        String mostPopularMoviesPath = "MostPopularMovies/";
        String url = baseUrl + lang + apiNamespace + mostPopularMoviesPath + imdbApiKey;
        URI endpoint = URI.create(url);

        // creates a HTTP client to send requests
        HttpClient client = HttpClient.newHttpClient();

        // creates the HTTP request builder
        HttpRequest request = HttpRequest.newBuilder(endpoint).GET().build();

        try {
            // do the request itself and save the response in a variable
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            //get the body from the response
            String body = response.body();

            JSONParser jsonParser = new JSONParser();
            // parse the response to get only the title, poster and imdbRating keys
            List<Map<String, String>> moviesList = jsonParser.parse(body);

            String starColor = "\u001B[33m";
            String resetColor = "\u001B[0m";
            String emptyStar = "\u2606";
            String filledStar = "\u2605";

            // handle and show however I want
            for (Map<String, String> movieItem : moviesList) {
                int rating = 0;
                String imdbRating = movieItem.get("imDbRating");

                if (imdbRating != "") {
                    rating = Math.round(Float.parseFloat(imdbRating));
                }

                String emoji = switch(rating) {
                    case 1,2 -> "🙁";
                    case 4,5 -> "😐";
                    case 6,7 -> "🙂";
                    case 8,9 -> "😀";
                    case 10 -> "😍";
                    default -> "😩";
                };

                System.out.println("Title: " + movieItem.get("title") + " " + emoji);
                System.out.println("Image: " + movieItem.get("image"));

                int totalStars = 10;
                System.out.print("Rating: ");
                for(int i = 1; i <= rating; i++) {
                    System.out.print(starColor + filledStar + resetColor);
                }

                for(int i = 1; i <= totalStars - rating; i++) {
                    System.out.print(starColor + emptyStar + resetColor);
                }

                System.out.println();
                System.out.println("---------------------------------------------------------------------------------");
            }
        } catch(IOException exception) {
            System.out.println("Exception: " + exception);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}