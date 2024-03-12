package com.Server;

import com.Server.utils.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class ServerListener extends Thread{
    private int port;
    private String document_root;
    private ServerSocket serverSocket;
    private ObjectNode serverConf;


    ServerListener(int port, String document_root) throws IOException {
        this.port = port;
        this.document_root = document_root;
        this.serverSocket = new ServerSocket(this.port);
        System.out.println("Server is Running at http://localhost:" + this.port);
    }

   ServerListener(int port, String document_root, JsonNode serverConf ) throws IOException {
        this.port = port;
        this.document_root = document_root;
        this.serverConf = serverConf.deepCopy();

        if(!serverConf.has("documentRoot") ) {
            this.serverConf.put("documentRoot", document_root);
        }

        this.serverSocket = new ServerSocket(this.port);
        System.out.println("Server is Running at http://localhost:" + this.port);
    }
    @Override
    public void run() {
        try {
            while(serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = null;
                socket = serverSocket.accept();
                // socket.setSoTimeout(3000);
                HttpWorker httpWorker = new HttpWorker(socket);
                httpWorker.serverInfo(this.serverConf);

                httpWorker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
}
