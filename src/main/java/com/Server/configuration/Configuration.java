package com.Server.configuration;

public class Configuration {

    private static Object[] servers;
    private int port;
    private String documentRoot;

    private Object mainServerConf;
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

    public Object getMainServerConf() {
        return this.mainServerConf;
    }

    public void setMainServerConf(Object mainServerConf) {
        this.mainServerConf = mainServerConf;
    }
}
