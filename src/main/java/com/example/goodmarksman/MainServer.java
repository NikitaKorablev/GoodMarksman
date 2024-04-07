package com.example.goodmarksman;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    GameModel m = Models.buildGM();

    int port = 3000;
    InetAddress ip = null;

    void startServer() {
        ServerSocket ss;
        Socket cs;

        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server start\n");

            while (true) {
                // TODO: Логика подключения нескольких клиентов
                cs = ss.accept();
                System.out.println("Client connect (" + cs.getPort() + ")");

                GameServer cl = new GameServer(cs);
                m.addObserver((model) -> {
                    // TODO: при попадании в мишень, отпрака таблицы
//                    Msg r = new Msg(model.getScoreBoard());
//                    cl.sendMsg(r);
                });
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void main(String[] args) {
        MainServer ms = new MainServer();
        ms.startServer();
    }
}
