package com.Server;

import com.Server.Http.*;
import com.Server.utils.URLParser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpWorker extends Thread {
    private Socket socket;
    Map<String, String> reqs = new HashMap<>();


    int clientPort;
    private InetAddress address;
    private SocketAddress socketAddress;
    private InputStream reader;
    private OutputStream writer;

    /**
     * Socket data starts from here
     */


    HttpWorker(Socket soc) throws IOException {
        socket = soc;
        reader = socket.getInputStream();
        writer = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            HttpParser httpParser = new HttpParser();
            HttpRequest request = httpParser.parseHttpRequest(reader);

            if(request.getMethod() == HttpMethod.GET) {
                URLParser requestURI = new URLParser( request.getRequestTarget());

                HttpResponse response = new HttpResponse();
                response.setHttpVersion(request.getBestCompatibleHttpVersion());
                File file = new File(requestURI.getPath());

                if (file.exists()) {
                    if (file.isDirectory()) {
                        response.setStatusCode(HttpStatusCode.CLIENT_ERROR_403_FOR_BIDDEN);

                        file = new File("src/main/resources/error/403.html");
                    } else {
                        response.setStatusCode(HttpStatusCode.CLIENT_ERROR_200_OK);
                    }
                } else {
                    // file not found 404 error
                    response.setStatusCode(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND);
                    file = new File("src/main/resources/error/404.html");
                }

                response.setResponseBody(file);

                response.sendResponse(writer);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HttpParsingException e) {
            throw new RuntimeException(e);
        } finally {

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {}
                }

                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {}
                }

                if(socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {}
                }
        }
    }
}
