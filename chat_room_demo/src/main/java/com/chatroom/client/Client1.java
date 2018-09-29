package com.chatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 {

    private int port = 8080;
    private Socket socket;

    public static void main(String[] args) {
        new Client1();
    }

    public Client1() {
        try {
            socket = new Socket("127.0.0.1", port);
            new Cthread().start();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg1;
            while ((msg1 = br.readLine()) != null) {
                System.out.println(msg1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Cthread extends Thread {
        public void run() {
            try {
                BufferedReader re = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                String msg2;
                while (true) {
                    msg2 = re.readLine();
                    pw.println(msg2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
