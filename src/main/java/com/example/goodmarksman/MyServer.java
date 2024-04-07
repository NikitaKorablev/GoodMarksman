package com.example.goodmarksman;

import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Executable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {
    private int connectedClients = 0;

    int port = 3000;
    InetAddress ip = null;

    private void addConnectedClient(Socket cs) {
        InputStream is;
        OutputStream os;
        DataInputStream dis;
//        ObjectInputStream ois;
        DataOutputStream dos;

        try {
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

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void StartServer()
    {
        ServerSocket ss;
        Socket cs;

        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server start\n");



            while (connectedClients < 4) {
                cs = ss.accept();
                System.out.println("Client connect (" + cs.getPort() + ")");



            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }



    public static void main(String[] args) {
        new MyServer().StartServer();
    }

}
