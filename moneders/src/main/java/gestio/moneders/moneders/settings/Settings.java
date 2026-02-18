package gestio.moneders.moneders.settings;

public final class Settings {
    private static volatile String path;

    private Settings() {
        // utility class
    }

    public static void setPath(String p) {
        path = p;
    }

    public static String getPath() {
        return path;
    }
}
