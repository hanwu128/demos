package com.hw.socket.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static BufferedReader br = null;
    private static PrintWriter pw = null;
    private static ServerSocket ss;
    private static Socket s;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(5500);
            System.out.println("服务器正在启动...");
            s=ss.accept();
            System.out.println("连接成功"+s.getRemoteSocketAddress());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            while (true){
                String string = br.readLine();
                System.out.println("Client端："+string);
                System.out.println("Server端请输入：");
                String str = scanner.next();
                pw.println();
                pw.flush();
                if (str.equals("exit")){
                    break;
                }

            }
            pw.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
