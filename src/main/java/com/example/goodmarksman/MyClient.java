package com.example.goodmarksman;

import com.google.gson.Gson;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MyClient {

    int port = 3000;
    InetAddress ip = null;

    public void ClientStart() {
        Socket cs;
        InputStream is;
        OutputStream os;
        DataInputStream dis;
        DataOutputStream dos;
        ObjectOutputStream oos;
        try {
            ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            System.out.append("Client start \n");
            is = cs.getInputStream();
            os = cs.getOutputStream();

            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
//            oos = new ObjectOutputStream(os);

            System.out.println("Client send massage");

            Action act = new Action(TypeAction.F1, "Hi!");

            Gson gson = new Gson();
            String obj_str = gson.toJson(act);

//            oos.reset();
//            oos.writeObject(act);
//            oos.flush();
//            dos.writeUTF(obj_str);
            dos.writeUTF(obj_str);

            System.out.println("Read masage");
            String s = dis.readUTF();

            System.out.println("Mesege from server: " + s);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


    }


    public static void main(String[] args) {
        new MyClient().ClientStart();
    }

}