package com.nhnacademy.example;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Exam02 {
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

        try {
            // tag::newSocket[]
            Socket socket = new Socket(host, port);
            // end::newSocket[]
            // tag::connected[]
            System.out.println("서버에 연결되었습니다.");
            // end::connected[]

            // tag::outputWrite[]
            socket.getOutputStream().write("Hello World!".getBytes());
            // end::outputWrite[]

            // tag::socketClose[]
            socket.close();
            // end::socketClose[]
            // tag::connectException[]
        } catch (ConnectException e) {
            System.err.println(host + ":" + port + "에 연결할 수 없습니다.");
        }
        // end::connectException[]
        // tag::IOException[]
        catch (IOException e) {
            System.err.println(e);
        }
        // end::IOException[]
    }
}
