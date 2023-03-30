public record Content(String title, String imageUrl, String rating) {
    Content(String title, String imageUrl) {
        this(title, imageUrl, null);
    }
}
