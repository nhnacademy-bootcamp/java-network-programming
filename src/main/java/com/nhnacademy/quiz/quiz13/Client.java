package com.nhnacademy.quiz.quiz13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
    String name;
    String host;
    int port;

    public Client(String name, String host, int port) {
        this.name = name;
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

            Thread receiver = new Thread(() -> {
                String message;

                try {
                    while ((message = input.readLine()) != null) {
                        terminalOutput.println(message);
                        terminalOutput.flush();
                    }
                } catch (IOException ignore) {
                    //
                }

            });
            receiver.start();

            String message;

            output.println("#" + name);
            output.flush();

            while ((message = terminalInput.readLine()) != null) {
                output.println(message);
                output.flush();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            System.out.println("Usage : client <id> <host> <port>");
            System.exit(1);
        }

        Thread thread = new Thread(new Client(args[0], args[1], Integer.parseInt(args[2])));

        thread.start();
        thread.join();
    }
}
