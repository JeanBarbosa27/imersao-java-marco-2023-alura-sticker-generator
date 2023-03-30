public enum API {
    IMDB_TOP_250_MOVIES(
            "IMDB",
            "https://imdb-api.com/pt/API/Top250Movies/" + System.getenv("IMDB_API_KEY"),
            new IMDBContentExtractor()
    ),
    IMDB_TOP_250_SERIES(
            "IMDB",
            "https://imdb-api.com/pt/API/Top250TVs/" + System.getenv("IMDB_API_KEY"),
            new IMDBContentExtractor()
    ),
    NASA(
            "NASA",
            "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&start_date=2022-06-12&end_date=2022-06-14",
            new NASAContentExtractor()
    );

    private ContentExtractor contentExtractor;
    private String source;
    private String url;

    API(String source, String url, ContentExtractor contentExtractor) {
        this.source = source;
        this.url = url;
        this.contentExtractor = contentExtractor;
    }

    public String getSource() {
        return this.source;
    }
    public String getUrl() {
        return this.url;
    }

    // TODO: Add an overload to support more query params, maybe receiving a List<Map<String String>>
    public String getUrl(String apiKey) {
        return this.url.replace("{{API_KEY}}", apiKey);
    }

    public ContentExtractor getContentExtractor() {
        return this.contentExtractor;
    }
}
