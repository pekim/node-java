package uk.co.pekim.nodejava.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.pekim.nodejava.ResourceUtil;

public class ConfigurationTest {
    @Test
    public void test() throws Exception {
        String configString = ResourceUtil.getAsString("/uk/co/pekim/nodejava/configuration/" + "config.json");
        Configuration configuration = Configuration.parseJson(configString);

        assertEquals(1234, configuration.getNodePort());
    }
}
