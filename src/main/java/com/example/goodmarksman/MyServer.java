package com.example.goodmarksman;

import com.google.gson.Gson;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    int port = 3000;
    InetAddress ip = null;

    public void StartServer()
    {
        ServerSocket ss;
        Socket cs;
        InputStream is;
        OutputStream os;
        DataInputStream dis;
//        ObjectInputStream ois;
        DataOutputStream dos;
        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server start\n");

            while (true) {
                cs = ss.accept();
                System.out.println("Client connect (" + cs.getPort() + ")");

                is = cs.getInputStream();
                os = cs.getOutputStream();

                dis = new DataInputStream(is);
//                ois = new ObjectInputStream(is);
                dos = new DataOutputStream(os);

                System.out.println("Read masage");
                String s = dis.readUTF();
                System.out.println("Connect: " + s);

                Gson gson = new Gson();
                Action act = gson.fromJson(s, Action.class);

                System.out.println(act);

                dos.writeUTF("hello from Server!");

            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }



    public static void main(String[] args) {
        new MyServer().StartServer();
    }

}
