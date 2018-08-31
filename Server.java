package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;

    private static List<Socket> list = new ArrayList<>();

    public static void main(String[] agrs) {
        new Server();
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(8081);
            while (true) {
                Socket socket = serverSocket.accept();
                list.add(socket);
                new Mythread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Mythread extends Thread {
        Socket ssocket;
        private BufferedReader br;
        private PrintWriter pw;
        public String msg;

        public Mythread(Socket s) {
            ssocket = s;
        }

        public void run() {
            try {
                br = new BufferedReader(new InputStreamReader(ssocket.getInputStream()));
                msg = "欢迎【" + ssocket.getInetAddress() + "】进入聊天室！当前聊天室有【"
                        + list.size() + "】人";
                sendMsg();
                while ((msg = br.readLine()) != null) {
                    if(msg!=null&&msg.equals("结束")){
                        sendMsg();
                        list.remove(ssocket);
                        ssocket.close();
                        System.out.println("现在聊天室人员数量为"+list.size());
                        return;
                    }
                    msg = "【" + ssocket.getInetAddress() + "】说：" + msg;
                    sendMsg();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void sendMsg() {
            try {
                System.out.println("发送:"+msg);
                for (int i = list.size() - 1; i >= 0; i--) {
                    pw = new PrintWriter(list.get(i).getOutputStream(), true);
                    pw.println(msg);
                    pw.flush();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }
}
