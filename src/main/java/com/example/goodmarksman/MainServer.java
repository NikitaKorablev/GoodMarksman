package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.Client;
import com.example.goodmarksman.objects.MsgAction;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private final GameModel m = Models.buildGM();

    int port = 3000;
    InetAddress ip = null;

    private GameServer game = null;

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
                if (game == null) { game = new GameServer(cl); }
                else { game.addPlayer(cl); }

                m.addObserver((model) -> {
                    System.out.println(cl.getSocket().getPort());
                    // TODO: список состояния игры
                    try {
                        Msg message = new Msg(m.getPlayersData(), MsgAction.UPDATE_GAME_STATE);
                        cl.sendMsg(message);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        throw new RuntimeException(e);
                    }

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
