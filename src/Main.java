import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // request contents
        String url = "https://imdb-api.com/pt/API/Top250Movies/" + System.getenv("IMDB_API_KEY");
//        String url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&start_date=2022-06-12&end_date=2022-06-14";
        HTTPClient httpClient = new HTTPClient();
        String json = httpClient.request(url);

        // extract content
        ContentExtractor contentExtractor = new IMDBContentExtractor();
//        ContentExtractor contentExtractor = new NASAContentExtractor();
        List<Content> contents = contentExtractor.extractContent(json);

        /*
        * TODO:
        *  - [ ] Create a service to call IMDB API
        *  - [ ] Create a service to call NASA API
        *  */

        // setup console settings to have a nice ratings print
        String starColor = "\u001B[33m";
        String resetColor = "\u001B[0m";
        String emptyStar = "☆";
        String filledStar = "★";

        // handle and show however I want
        StickerGenerator stickerGenerator = new StickerGenerator();
        for (int index = 0; index < 3; index++) {
            int rating = 0;
            String imageUrl = contents.get(index).imageUrl();
            String imdbRating = contents.get(index).rating();
            String title = contents.get(index).title();

            if (imdbRating != null && !imdbRating.isEmpty()) {
                rating = Math.round(Float.parseFloat(imdbRating));
            }

            /*
            * This can be separated in a IMDBAPIRequestService, which can hold this logic of getting the rating and also
            * the setup to have a nice ratings print
            * */
            String[] emoji = switch(rating) {
                case 1,2 -> new String[]{"🙁", "NÃO VALE A PENA"};
                case 4,5 -> new String[]{"😐", "NÃO RECOMENDO"};
                case 6,7 -> new String[]{"🙂", "LEGAL"};
                case 8,9 -> new String[]{"😀", "BOM"};
                case 10 -> new String[]{"😍", "TOPZERA"};
                default -> new String[]{"😩", "UMA PORCARIA"};
            };

            System.out.println("Title: " + title + " " + emoji[0]);
            System.out.println("Image: " + imageUrl);

            int totalStars = 10;
            System.out.print("Rating: ");
            for(int i = 1; i <= rating; i++) {
                System.out.print(starColor + filledStar + resetColor);
            }
            for(int i = 1; i <= totalStars - rating; i++) {
                System.out.print(starColor + emptyStar + resetColor);
            }
            System.out.println();

            try {
                String fileName = title.replace(":", "-").replace(" ","_");
                System.out.println("Generating sticker");
                InputStream imageInputStream = new URL(imageUrl).openStream();
                stickerGenerator.create(
                        imageInputStream,
                        emoji[1],
                        Color.YELLOW,
                        fileName
                );
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            System.out.println("---------------------------------------------------------------------------------");
        }
    }
}