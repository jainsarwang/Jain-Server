package com.Server.configuration;

public class Configuration {

    private static Object[] servers;
    private int port;
    private String documentRoot;

//    private Configuration() {
////        port = 80;
//    }
//
//    public Configuration() {}
//    public Configuration (Object o)  {
//        JSONObject json = (JSONObject) o;
//
//        this.port = (int) json.get("port");
//        this.documentRoot = (String) json.get("documentRoot");
//    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDocumentRoot() {
        return this.documentRoot;
    }

    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }


    public Object[] getServers() {
        return servers;
    }

    public void setServers(Object[] servers) {
        this.servers = servers;
    }
}
