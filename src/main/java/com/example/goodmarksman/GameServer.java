package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class GameServer implements IObserver {
    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    private final ArrayList<Thread> messageListeners = new ArrayList<>();
    private Thread gameThread = null;

    MsgAction gameState = MsgAction.GAME_STOPPED;
    boolean isPaused = false;
    Thread sendState = null;

    int countReadyPlayers = 0;

    // Подключение к серверу
    public GameServer(Client cl) {
        addPlayer(cl);

        gameThread = new Thread(this::run);
        gameThread.setDaemon(true);
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
                System.err.println("Add player Error: " + e.getMessage());
                throw new RuntimeException(e);
            }

            return;
        }

        try {
            m.addClient(cl, new Arrow(), new Score());
        } catch (Exception e) {
            System.err.println("Error in addPlayer() in GameServer: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Thread thread = new Thread(() -> messageListener(cl));
        thread.setDaemon(true);
        thread.start();
        messageListeners.add(thread);
        System.out.println("new players count " + m.playersSize());
        cl.sendState(m.getPlayersData());
    }

    void messageListener(Client cl) {
//        System.out.println(m.getClientData(cl.getSocket()));

        while (true) {
            try {
                Msg msg = cl.readMsg();
                if (msg.getAction() == MsgAction.CONNECTED) {
                    System.out.println("Client say: " + msg.message);
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
                    //TODO: Возможно надо wait()
                    gameThread.interrupt();
                    gameThread = null;
                    continue;
                }

            } catch (Exception e) {
                try {
                    cl.getSocket().close();
                    System.err.println("Client " + cl.getSocket().getPort() + " closed.");
                    synchronized (this) {
                        messageListeners.remove(m.getPlayerIndex(cl.getSocket()));
                        m.removePlayer(cl);
                        m.removeObserver(cl.getIObserver());
//                        m.
//                        System.out.println(m.getPlayersData());
                    }
                } catch (Exception err) {
                    System.err.println(err.getMessage());
                    throw new RuntimeException(err);
                }

                System.err.println("Remove is called");
                return;
            }
        }
    }

    //TODO: Запуск цикла среверной части
    void run() {
        while (true) {

        }
    }

    public Client getLastClient() {
        return m.getClient(-1);
    }

    @Override
    public void event(GameModel m) {}
}
