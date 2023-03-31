import java.util.List;
import java.util.Map;

public class NASAContentExtractor implements ContentExtractor {
    @Override
    public List<Content> extractContent(String json) {
        JSONParser jsonParser = new JSONParser();
        List<Map<String, String>> attributesList = jsonParser.parse(json);

        return attributesList.stream()
                .map((attribute) -> new Content(attribute.get("title"), attribute.get("url")))
                .toList();
    }
}
