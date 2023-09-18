package com.nhnacademy.quiz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Quiz05 {
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
            // tag::getInputStream[]
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            // end::getInputStream[]
            // tag::getOutputStream[]
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
            // end::getOutputStream[]
            int readLength;
            // tag::createBuffer[]
            byte[] buffer = new byte[2048];
            // end::createBuffer[]

            // tag::inputRead[]
            while ((readLength = input.read(buffer)) > 0) {
                // end::inputRead[]

                // tag::exit[]
                if (new String(buffer, 0, readLength).trim().equals("exit")) {
                    break;
                }
                // end::exit[]

                // tag::outputWrite[]
                output.write(buffer, 0, readLength);
                // end::outputWrite[]
            }
            // tag::socketClose[]
            socket.close();
            // end::socketClose[]
            // tag::IOException[]
        } catch (IOException e) {
            System.err.println(e);
        }
        // end::IOException[]
    }
}