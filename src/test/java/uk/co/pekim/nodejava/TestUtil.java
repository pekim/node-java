/**
 * 
 */
package uk.co.pekim.nodejava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utilities used in tests.
 * 
 * @author Mike D Pilsbury
 */
public class TestUtil {
    /**
     * Read a resource.
     * 
     * @param resourceName
     *            the full path to the resource.
     * @return the resource file contents.
     * @throws IOException
     *             if something went wrong.
     */
    public static String getResourceAsString(String resourceName) throws IOException {
        InputStream input = TestUtil.class.getResourceAsStream(resourceName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        StringBuilder string = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            string.append(line);
            line = reader.readLine();
        }

        return string.toString();
    }
}
