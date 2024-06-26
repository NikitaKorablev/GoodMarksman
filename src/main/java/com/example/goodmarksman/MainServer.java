package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.Client;
import com.example.goodmarksman.objects.Score;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainServer {
    public static final GameModel model = Models.buildGM(true);

    int port = 3000;
    InetAddress ip = null;

    private static GameServer game = null;

    void startServer() {
        ServerSocket ss;
        Socket cs;

        try {
            ip = InetAddress.getLocalHost();
            System.out.println(ip);
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server start\n");

            while (true) {
                // TODO: Логика подключения нескольких клиентов
                cs = ss.accept();
                System.out.println("Client connect (" + cs.getPort() + ")");

                Client cl = new Client(cs);

                synchronized (Thread.currentThread()) {
                    if (game == null) { game = new GameServer(cl); }
                    else {
                        game.addListener(cl);
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        model.getDao().getScoreBord("Player1");

        MainServer ms = new MainServer();
        ms.startServer();
    }
}
