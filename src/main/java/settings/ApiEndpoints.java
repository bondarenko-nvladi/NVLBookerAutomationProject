package settings;

public enum ApiEndpoints {
    PING("/ping"),
    BOOKING("/booking"),
    BOOKING_BY_ID("/booking/"),
    AUTH("/auth");

    private final String path;

    ApiEndpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
