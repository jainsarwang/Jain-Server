package com.Server;

import com.Server.configuration.Configuration;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class ServerListener extends Thread{
    private int port;
    private String document_root;
    private ServerSocket serverSocket;
    private Object serverConf;


    ServerListener(int port, String document_root) throws IOException {
        this.port = port;
        this.document_root = document_root;
        this.serverSocket = new ServerSocket(this.port);
        System.out.println("Server is Running at http://localhost:" + this.port);
    }

   ServerListener(int port, String document_root, Object serverConf ) throws IOException {
        this.port = port;
        this.document_root = document_root;
        this.serverConf = serverConf;

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
