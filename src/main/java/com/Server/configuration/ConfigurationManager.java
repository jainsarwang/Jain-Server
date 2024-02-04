package com.Server.configuration;

import com.Server.utils.JSON;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfiguration;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance() {
        if (myConfiguration == null)
            myConfiguration = new ConfigurationManager();
        return myConfiguration;
    }

    /*
     * Used to load Configuration Files
     * */
    public Configuration loadConfigurationFile(String path) throws IOException {
        FileReader configFile = new FileReader(path);
        StringBuffer sb = new StringBuffer();

        int i = 0;
        while ((i = configFile.read()) != -1) {
            sb.append((char) i);
        }

//        myCurrentConfiguration = new Configuration(o);
//        myCurrentConfiguration = new JSON(sb.toString()).loadToClass(o, Configuration.class);

        JsonNode conf = JSON.parse(sb.toString());
        myCurrentConfiguration = JSON.fromJson(conf, Configuration.class);

        return myCurrentConfiguration;
    }

    public Configuration getCurrentConfiguration() {
        if(myCurrentConfiguration == null)
            throw new RuntimeException("No Configuration Set");
        return myCurrentConfiguration;
    }
}
