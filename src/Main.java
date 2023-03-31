import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // request contents
        API apiSettings = API.IMDB_TOP_250_MOVIES;
        String url = apiSettings.getUrl();
        ContentExtractor contentExtractor = apiSettings.getContentExtractor();

        HTTPClient httpClient = new HTTPClient();
        String json = httpClient.request(url);
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
        int iterationLimit = Math.min(5, contents.size());

        for (int index = 0; index < iterationLimit; index++) {
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
                System.out.println("Generating sticker");
                InputStream imageInputStream = new URL(imageUrl).openStream();
                String stickerText = emoji[1];
                Color stickerTextColor = Color.YELLOW;
                String fileName = title.replace(":", "-").replace(" ","_");

                if (apiSettings.getSource().equals("NASA")) {
                    stickerText = "Que tal?";
                    stickerTextColor = Color.WHITE;
                }

                stickerGenerator.create(
                        imageInputStream,
                        stickerText,
                        stickerTextColor,
                        fileName
                );
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            System.out.println("---------------------------------------------------------------------------------");
        }
    }
}