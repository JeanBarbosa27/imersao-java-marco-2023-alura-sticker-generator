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

        return attributesList.stream().map((attribute -> {
            String imageUrl = attribute.get("image");
            Matcher imageMatcher = imageRegex.matcher(imageUrl);

            if(imageMatcher.find()) {
                // This is necessary to get the bigger version of the image
                imageUrl = imageUrl.replace(imageMatcher.group(1), "");
            }

            return new Content(attribute.get("title"), imageUrl, attribute.get("imDbRating"));
        })).toList();
    }
}
