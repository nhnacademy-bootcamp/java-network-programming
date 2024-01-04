package com.nhnacademy.quiz.quiz11;

import java.io.IOException;
import java.net.ServerSocket;

import com.nhnacademy.quiz.quiz12.EchoService;

public class Server implements Runnable {
    int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                EchoService echoService = new EchoService(serverSocket.accept());

                echoService.start();
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
