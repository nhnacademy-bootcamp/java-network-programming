package com.nhnacademy.quiz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Quiz04 {
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

        // tag::getSocketInfo[]
        try {
            // tag::createSocket[]
            Socket socket = new Socket(host, port);
            System.out.println("서버에 연결되었습니다.");
            // end::createSocket[]

            // tag::getOutputStream[]
            OutputStream output = socket.getOutputStream(); // socke에서 Output stream 얻기
            // end::getOutputStream[]
            // tag::getStandardInput[]
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // end::getStandardInput[]
            while (true) {
                // tag::readLine[]
                String line = reader.readLine();
                if (line.equals("exit")) {
                    break;
                }
                // end::readLine[]

                // tag::outputWrite[]
                output.write(line.getBytes()); // console에서 입력받은 문자열을 전송
                output.write("\n".getBytes()); // 문자열 끝을 위한 newline 전송
                output.flush(); // buffer에 남아 있는 데이터까지 완전히 전송
                // end::outputWrite[]
            }

            // tag::closeSocket[]
            socket.close();
            // end::closeSocket[]
        } catch (ConnectException e) {
            System.err.println(host + ":" + port + "에 연결할 수 없습니다.");
        } catch (IOException e) {
            System.err.println(e);
        }
        // end::getSocketInfo[]
    }
}
