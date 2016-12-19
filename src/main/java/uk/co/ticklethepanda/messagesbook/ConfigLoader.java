package uk.co.ticklethepanda.messagesbook;

import uk.co.ticklethepanda.messagesbook.messagestopdf.GetInputs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Created by panda on 19/12/16.
 */
public class ConfigLoader {

    public static Properties getProperties() {

        try {
            InputStream propertiesStream =
                    GetInputs.class.getResourceAsStream("/config.properties");

            if (propertiesStream == null) {
                throw new FileNotFoundException("Properties file not found in classpath.");
            }

            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load properties.", e);
        }
    }
}
