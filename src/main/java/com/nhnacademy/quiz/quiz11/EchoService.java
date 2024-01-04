package com.nhnacademy.quiz.quiz11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoService implements Runnable {

    Thread thread;
    Socket socket;

    public EchoService(Socket socket) {
        thread = new Thread(this);
        this.socket = socket;
    }

    public synchronized void start() {
        thread.start();
    }

    public synchronized void stop() {
        if (socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while (socket.isConnected()) {
                String message = reader.readLine();

                writer.println(message);
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
