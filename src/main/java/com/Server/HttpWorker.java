package com.Server;

import com.Server.Http.*;
import com.Server.utils.FileResponse;
import com.Server.utils.URLParser;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    private String documentRoot;

    int clientPort;
    private InetAddress address;
    private SocketAddress socketAddress;
    private InputStream reader;
    private OutputStream writer;
    private ObjectNode serverInfo;

    /**
     * Socket data starts from here
     */
    HttpWorker(Socket soc) throws IOException {
        socket = soc;
        reader = socket.getInputStream();
        writer = socket.getOutputStream();
    }

    public void serverInfo(ObjectNode serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public void run() {
        try {
            HttpParser httpParser = new HttpParser();
            HttpRequest request = httpParser.parseHttpRequest(reader);

            if(request.getMethod() == HttpMethod.GET) {
                URLParser requestURI = new URLParser(request.getRequestTarget());

                HttpResponse response = new HttpResponse();
                response.setHttpVersion(request.getBestCompatibleHttpVersion());

                FileOperations file = new FileOperations(serverInfo.get("documentRoot").asText(), requestURI.getPath());
                FileResponse fileData = file.getMostSuitableFile();

                response.setStatusCode(fileData.statusCode);
                response.setResponseBody(fileData.file);

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
