package com.hw.socket.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class client1 {

    private static PrintWriter pw = null;
    private static BufferedReader br = null;
    private static Socket s;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Socket s = new Socket(InetAddress.getLocalHost(),5500);
            pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true){
                System.out.println("Client端请输入：");
                String str = scanner.next();
                pw.println(str);
                pw.flush();
                String string = br.readLine();
                System.out.println("Server端："+string);
                if (str.equals("exit")){
                    break;
                }
            }

            br.close();
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
