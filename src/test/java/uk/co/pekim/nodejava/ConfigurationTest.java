package uk.co.pekim.nodejava;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigurationTest {
    @Test
    public void test() throws Exception {
        Configuration configuration = Configuration.parseJson("{\"nodePort\": 1234}");

        assertEquals(1234, configuration.getNodePort());
    }
}
