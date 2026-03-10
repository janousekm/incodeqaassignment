package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading the data.properties file.
 * Implemented as a static helper for simplicity, since the project currently uses only one .properties file.
 * Can be refactored later for multiple files or more flexible usage if needed.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("data.properties")) {

            if (input == null) {
                throw new RuntimeException("data.properties not found in classpath");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }
}
