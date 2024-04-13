package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer implements IObserver {
    GameModel m = Models.buildGM();
    Gson gson = new Gson();


    private final ArrayList<Thread> threads = new ArrayList<>();
    private Thread gameThread = null;

    MsgAction gameState = MsgAction.GAME_STOPPED;
    boolean isPaused = false;
    Thread sendState = null;

    int countReadyPlayers = 0;

    // Подключение к серверу
    public GameServer(Client cl) {
        addPlayer(cl);

        gameThread = new Thread(this::run);
//        new Thread(this::run).start();
    }

    public void addPlayer(Client cl) {
        System.out.println("players count " + m.playersSize());
        if (m.playersSize() >= 4) {
            try {
                cl.sendMsg(new Msg("Too many players connected to the server",
                        MsgAction.CONNECTION_ERROR)
                );
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException(e);
            }

            return;
        }

        m.addClient(cl, new Arrow(), new Score());
        Thread thread = new Thread(() -> messageListener(cl)); thread.start();
        threads.add(thread);
        System.out.println("new players count " + m.playersSize());
        sendState(cl);
    }

    void messageListener(Client cl) {
        System.out.println(m.getClientData(cl.getSocket()));

        while (true) {
            try {
                Msg msg = cl.readMsg();
                if (msg.getAction() == MsgAction.CONNECTED) {
                    System.out.println(msg.message);
                    continue;
                }
                if (msg.getAction() == MsgAction.CONNECTION_ERROR) {
                    throw new Exception(msg.message);
                }
                if (msg.getAction() == MsgAction.CLIENT_STATE) {
                    if (msg.clientState == ClientState.READY) {
                        this.countReadyPlayers++;
                        if (countReadyPlayers == 4) gameThread.start();
                    }
                    else if (msg.clientState == ClientState.NOT_READY) this.countReadyPlayers--;
                    continue;
                }
                if (msg.getAction() == MsgAction.GAME_STOPPED &&
                    gameThread != null && gameThread.isAlive()) {
                    gameThread.interrupt();
                    gameThread = null;
                    continue;
                }

            } catch (Exception e) {
                System.err.println("Client " + cl.getSocket().getPort() + " disconnected: " + e.getMessage());

                threads.remove(m.getPlayerIndex(cl.getSocket()));
                m.removePlayer(cl);
                return;
            }
        }
    }

    //TODO: Запуск цикла среверной части
    void run() {
        while (true) {

        }
    }

    public void sendState(Client cl) {
        try {
//            System.out.println(cl.getSocket().getPort());

            Msg message = new Msg(m.getPlayersData(), MsgAction.UPDATE_GAME_STATE);
            cl.sendMsg(message);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Resp readResp(DataInputStream dis) {
        Resp r = null;
        try {
            String respStr = dis.readUTF();
            r = gson.fromJson(respStr, Resp.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    @Override
    public void event(GameModel m) {}
}
