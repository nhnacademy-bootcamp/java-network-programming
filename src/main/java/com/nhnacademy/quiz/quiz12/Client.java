package com.nhnacademy.quiz.quiz12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
    String host;
    int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port);
                BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter terminalOutput = new PrintWriter(System.out);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream())) {

            String message;

            while ((message = terminalInput.readLine()) != null) {
                output.println(message);
                output.flush();

                String receivedMessage = input.readLine();
                terminalOutput.println(receivedMessage);
                terminalOutput.flush();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Client("localhost", 12345));

        thread.start();
        thread.join();
    }
}
