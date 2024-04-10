package com.example.goodmarksman;

import com.example.goodmarksman.objects.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

                Msg connection_accepted = new Msg("", MsgAction.CONNECTED);
                if (game == null) {
                    game = new GameServer(cs);
                    game.sendMsg(
                            new Client(cs).getDos(),
                            connection_accepted
                    );
                }
                else {
                    MsgAction action = game.addPlayer(cs);
                    System.out.println(action);
                    if (action == MsgAction.CONNECTION_ERROR) {
                        game.sendMsg(
                                new Client(cs).getDos(),
                                new Msg("Too many players connected to the server", action)
                                );
                        cs.close();
                        continue;
                    } else if (action == MsgAction.CONNECTED) {
                        game.sendMsg(
                                new Client(cs).getDos(),
                                connection_accepted
                        );
                    }
                }

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
