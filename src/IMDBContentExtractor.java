import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBContentExtractor implements ContentExtractor {
    private final Pattern imageRegex = Pattern.compile("(\\.[\\.\\w,]+)(\\.jpg|\\.jpeg|\\.png)$");

    @Override
    public List<Content> extractContent(String json) {
        JSONParser jsonParser = new JSONParser();
        List<Map<String, String>> attributesList = jsonParser.parse(json);
        List<Content> contents = new ArrayList<>();

        // parse the response to get only the title, imageUrl and rating
        for (Map<String, String> attributes : attributesList) {
            String title = attributes.get("title");
            String imageUrl = attributes.get("image");
            String rating = attributes.get("imDbRating");
            Matcher imageMatcher = imageRegex.matcher(imageUrl);

            if(imageMatcher.find()) {
                // This is necessary to get the bigger version of the image
                imageUrl = imageUrl.replace(imageMatcher.group(1), "");
                Content content = new Content(title, imageUrl, rating);
                contents.add(content);
            }
        }

        return contents;
    }
}
