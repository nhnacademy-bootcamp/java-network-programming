package com.nhnacademy.quiz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Quiz07 {
    static class Receiver extends Thread {
        BufferedReader input;

        public Receiver(BufferedReader input) {
            this.input = input;
        }

        @Override
        public void run() {
            char[] buffer = new char[2048];

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int length;
                    length = input.read(buffer);
                    System.out.println(new String(buffer, 0, length));
                }
            } catch (IOException ignore) {
                //
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        if (args.length > 0) {
            host = args[0];
        }

        try {
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException ignore) {
            System.err.println("Port가 올바르지 않습니다.");
            System.exit(1);
        }

        try (Socket socket = new Socket(host, port);
                BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            System.out.println("서버에 연결되었습니다.");

            Receiver receiver = new Receiver(input);

            receiver.start();

            String line;
            while ((line = terminalIn.readLine()) != null) {
                if (line.trim().equals("exit")) {
                    break;
                }
                output.write(line);
                output.flush();
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}