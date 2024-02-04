package com.Server.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLParser {
    private final String fragment;
    private String host;
    private final String[] queryParams;
    private int port;
    private final String path;
//    private final String file;

    public URLParser(String urlString) {
        URL url = null;
        URI uri = null;

        try {
            url = new URL(urlString);

            this.host = url.getHost();
//            this.port = url.getDefaultPort() > 0 ? url.getDefaultPort() : 80;
            this.port = url.getDefaultPort();

            uri = url.toURI();
        } catch (MalformedURLException e) {
            try {
                uri = new URI(urlString);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        this.path = uri.getPath();

        String queryString = uri.getQuery();

        if(queryString != null) {
            this.queryParams = uri.getQuery().split("&");
        }else {
            this.queryParams = null;
        }
        this.fragment = uri.getFragment();
    }

    public String getQueryString() {
        if(this.queryParams == null) {
            return null;
        }

        return String.join("&", queryParams);
    }

    public String getQueryStringWithSpace() {
        if(this.queryParams == null) {
            return "";
        }

        return String.join(" ", queryParams);
    }

    public String getFragment() {
        return fragment;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
