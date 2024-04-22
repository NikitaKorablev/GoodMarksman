package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.Client;
import com.example.goodmarksman.objects.Action;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static final GameModel model = Models.buildGM();

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

                Client cl = getClient(cs);

                synchronized (Thread.currentThread()) {
                    if (game == null) { game = new GameServer(cl); }
                    else {
                        game.addListener(cl).start();
                    }

                    model.addObserver(cl.getIObserver());
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static Client getClient(Socket cs) {
        Client cl = new Client(cs);
        try {
            cl.sendMsg(new Msg("", Action.CLIENT_CONNECTED));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        cl.setIObserver((model) -> {
            try {
                Msg message = new Msg(MainServer.model.getPlayersData(), Action.UPDATE_GAME_STATE);
                System.out.println("Server Observer: " + MainServer.model.getPlayersData());
                cl.sendMsg(message);
            } catch (IOException e) {
                System.err.println("Event error: " + e.getMessage());
            }
        });
        return cl;
    }


    public static void main(String[] args) {
        MainServer ms = new MainServer();
        ms.startServer();
    }
}
