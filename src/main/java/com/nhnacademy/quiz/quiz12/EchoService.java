package com.nhnacademy.quiz.quiz12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class EchoService implements Runnable {
    static List<EchoService> serviceList = new LinkedList<>();

    Thread thread;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public EchoService(Socket socket) throws IOException {
        thread = new Thread(this);
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        serviceList.add(this);
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

    public void write(String message) {
        writer.println(message);
        writer.flush();

    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                EchoService.broadcast(reader.readLine());
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public static synchronized void broadcast(String message) {
        for (EchoService service : serviceList) {
            service.write(message);
        }
    }
}
