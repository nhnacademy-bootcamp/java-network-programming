package com.nhnacademy.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Snc {

    public static void run(Socket socket) {
        try (BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter terminalOut = new BufferedWriter(new OutputStreamWriter(System.out))) {
            Transfer transfer1 = new Transfer(socketIn, terminalOut);
            Transfer transfer2 = new Transfer(terminalIn, socketOut);

            transfer1.start();
            transfer2.start();

            transfer1.join();
            transfer2.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        String host = "ems.nhnacademy.com";
        int port = 12345;
        boolean serverMode = true;

        if (args.length < 2) {
            System.out.println("설정이 잘못되었습니다.");
        }

        if (!args[0].equals("-l")) {
            host = args[0];
            serverMode = false;
        }

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println(e);
            System.exit(1);
        }

        if (serverMode) {
            try (ServerSocket serverSocket = new ServerSocket(port);
                    Socket socket = serverSocket.accept()) {
                Snc.run(socket);
            } catch (IOException e) {
            }
        } else {
            try (Socket socket = new Socket(host, port)) {
                Snc.run(socket);
            } catch (IOException e) {
            }
        }
    }
}
