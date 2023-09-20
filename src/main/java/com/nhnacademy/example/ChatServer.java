package com.nhnacademy.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer extends Thread {
    static List<ChatServer> serverList = new LinkedList<>();
    Socket socket;
    BufferedWriter writer;

    public ChatServer(Socket socket) {
        this.socket = socket;
        serverList.add(this);
    }

    public void send(String message) throws IOException {
        writer.write(message);
        writer.flush();
    }

    @Override
    public void run() {
        try (BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            this.writer = socketOut;
            while (!Thread.currentThread().isInterrupted()) {
                String line = socketIn.readLine();

                String[] tokens = line.trim().split(":");
                if (tokens.length == 1) {
                    if (tokens[0].equalsIgnoreCase("who")) {
                        socketOut.write(getName() + "\n");
                        socketOut.flush();
                    }
                } else if (tokens.length > 1) {
                    if (tokens[0].equalsIgnoreCase("id")) {
                        setName(tokens[1]);
                    } else if ((tokens[0].length() > 1) && (tokens[0].charAt(0) == '@')) {
                        String targetId = tokens[0].substring(1, tokens[0].length());
                        if (targetId.equals("@")) {
                            for (ChatServer server : ChatServer.serverList) {
                                server.send("#" + getName() + ":" + tokens[1] + "\n");
                            }
                        } else {
                            for (ChatServer server : serverList) {
                                if (server.getName().equals(targetId)) {
                                    server.send("#" + getName() + ":" + tokens[1] + "\n");
                                    break;
                                }
                            }
                        }
                    } else if ((tokens[0].length() > 1) && (tokens[0].charAt(0) == '!')) {
                        String command = tokens[0].substring(1, tokens[0].length());
                        if (command.equalsIgnoreCase("exit")) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        } catch (IOException ignore) {
            //
        }

        try {
            socket.close();
        } catch (IOException ignore) {
            //
        }
    }

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();

                ChatServer server = new ChatServer(socket);
                server.start();
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        for (ChatServer server : serverList) {
            server.interrupt();
            try {
                server.join();
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
