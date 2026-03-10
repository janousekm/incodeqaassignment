package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading configuration with the following priority:
 * 1. Environment variables  (for CI/CD secrets)
 * 2. System properties      (for -D flags)
 * 3. data.local.properties  (for local dev overrides, gitignored)
 * 4. data.properties        (committed defaults, no secrets)
 */
public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final Properties localProperties = new Properties();

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

        try (InputStream localInput = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("data.local.properties")) {
            if (localInput != null) {
                localProperties.load(localInput);
            }
        } catch (IOException e) {
            // local overrides are optional — continue silently
        }
    }

    public static String get(String key) {
        // 1. Environment variable (EMAIL, PASSWORD, SESSION_ID, BASE_URL)
        String envValue = System.getenv(key);
        if (envValue != null) return envValue;

        // 2. System property (-Demail=...)
        String sysProp = System.getProperty(key);
        if (sysProp != null) return sysProp;

        // 3. Local override (data.local.properties)
        String localProp = localProperties.getProperty(key);
        if (localProp != null) return localProp;

        // 4. Default (data.properties)
        return properties.getProperty(key);
    }
}