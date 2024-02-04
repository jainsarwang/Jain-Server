package com.Server;

import com.Server.configuration.Configuration;
import com.Server.configuration.ConfigurationManager;
import com.Server.utils.JSON;
import com.fasterxml.jackson.databind.JsonNode;


class Server {
    public static void main(String[] args) throws Exception {

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();


        if(conf.getPort() != 0 && conf.getDocumentRoot() != null) {
            new ServerListener(conf.getPort(), conf.getDocumentRoot()).start(); // running Default server
        }

        // running multiple other servers
        for (Object server: conf.getServers()) {
            System.out.println(server.getClass());
            JsonNode node = JSON.toJSON(server);
            Configuration newConfiguration = JSON.fromJson(node, Configuration.class);

            if(conf.getPort() != 0 && conf.getDocumentRoot() != null) {
                new ServerListener(newConfiguration.getPort(), conf.getDocumentRoot(), server).start();
            }
        }
    }
}