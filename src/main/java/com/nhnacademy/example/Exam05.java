package com.nhnacademy.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam05 {
    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();

            socket.getOutputStream().write("Hello!".getBytes());
            socket.getOutputStream().flush();

            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}