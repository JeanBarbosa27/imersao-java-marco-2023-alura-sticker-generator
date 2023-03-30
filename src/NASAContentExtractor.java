import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class NASAContentExtractor implements ContentExtractor {
    @Override
    public List<Content> extractContent(String json) {
        JSONParser jsonParser = new JSONParser();
        List<Map<String, String>> attributesList = jsonParser.parse(json);
        List<Content> contents = new ArrayList<>();

        // parse the response to get only the title, imageUrl
        for (Map<String, String> attributes : attributesList) {
            String title = attributes.get("title");
            String imageUrl = attributes.get("url");

            Content content = new Content(title, imageUrl);
            contents.add(content);
        }

        return contents;
    }
}
