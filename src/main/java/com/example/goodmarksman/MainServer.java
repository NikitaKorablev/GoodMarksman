package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.Client;
import com.example.goodmarksman.objects.MsgAction;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private static final GameModel m = Models.buildGM();

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
                if (game == null) { game = new GameServer(cl); }
                else { game.addPlayer(cl); }
//                System.out.println(cl.getSocket().getPort());

//                Client c = new Client(cs);
//                m.addObserver();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static Client getClient(Socket cs) {
        Client cl = new Client(cs);
        cl.setIObserver((model) -> {
//                    Client curClient = game.getLastClient();
//                    System.out.println(curClient.getSocket().getPort());
//                    System.out.println("allo size: " + GameModel.allO.size());
//                    System.out.println(p);
            try {
                Msg message = new Msg(m.getPlayersData(), MsgAction.UPDATE_GAME_STATE);
                System.out.println(message);
                cl.sendMsg(message);
//                        model.getClient(finalCs).sendMsg(message);
//                        cl.sendMsg(message);
            } catch (IOException e) {
                System.err.println("Event error: " + e.getMessage());
                throw new RuntimeException(e);
            }

//                    Msg r = new Msg(model.getScoreBoard());
//                    cl.sendMsg(r);
        });
        return cl;
    }


    public static void main(String[] args) {
        MainServer ms = new MainServer();
        ms.startServer();
    }
}
