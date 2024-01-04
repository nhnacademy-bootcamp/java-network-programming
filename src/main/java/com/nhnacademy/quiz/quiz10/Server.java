package com.nhnacademy.quiz.quiz10;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server implements Runnable {
    int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                try (Socket socket = serverSocket.accept();
                        BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                        BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
                    byte[] buffer = new byte[1024];
                    int length;

                    System.out.println("Client[" + socket.getInetAddress().getHostAddress() + "] connected");
                    while ((length = input.read(buffer)) >= 0) {
                        if (length > 0) {
                            System.out.println(new String(Arrays.copyOf(buffer, length)));
                            output.write(buffer, 0, length);
                            output.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Server(12345));

        thread.start();
        thread.join();
    }
}
