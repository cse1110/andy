package nl.tudelft.cse1110.andy.utils;

public class ResourceUtils {

    public static String resourceFolder(String path) {
        try {
            return ResourceUtils.class.getResource((path.startsWith("/") ? "" : "/") + path).getPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
