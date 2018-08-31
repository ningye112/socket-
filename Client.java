package com.company;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private int port=8081;

    public Client() throws IOException {
        socket=new Socket("127.0.0.1",port);
        try{
            new CThread(socket).start();
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while((msg=br.readLine())!=null){
                if(msg.equals("结束")) {
                    socket.close();
                    return;
                }
                System.out.println(msg);
            }
        }  catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.activeCount());
    }
}

class CThread extends Thread{
    private static int number=0;
    Socket socket;

    public CThread(Socket socket){
        this.socket=socket;
    }

    public void run(){
        try {
            BufferedReader re = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
            String msg=null;
            while(true){
                msg=re.readLine();
                pw.println(msg);
                pw.flush();
                if(msg!=null&&msg.equals("结束")){
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
