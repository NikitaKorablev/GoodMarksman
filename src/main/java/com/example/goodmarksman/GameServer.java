package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class GameServer implements IObserver {
//    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    private final ArrayList<Thread> messageListeners = new ArrayList<>();
    private Thread gameThread = null;

    MsgAction gameState = MsgAction.GAME_STOPPED;
    boolean isPaused = false;
    Thread sendState = null;

    int countReadyPlayers = 0;

    // Подключение к серверу
    public GameServer(Client cl) {
//        addPlayer(cl);
        addListener(cl).start();

        gameThread = new Thread(this::run);
        gameThread.setDaemon(true);
//        new Thread(this::run).start();
    }

    public Thread addListener(Client cl) {
        Thread thread = new Thread(() -> messageListener(cl));
        thread.setDaemon(true);
        messageListeners.add(thread);
        return thread;
    }

    public void addPlayer(Client cl) {
        System.out.println("players count " + MainServer.m.playersSize());
        if (MainServer.m.playersSize() >= 4) {
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
            MainServer.m.addClient(cl, new Arrow(cl.getSocket().getPort()), new Score(cl.getSocket().getPort()));
        } catch (Exception e) {
            System.err.println("Error in addPlayer() in GameServer: " + e.getMessage());
            throw new RuntimeException(e);
        }
//        Thread thread = new Thread(() -> messageListener(cl));
//        thread.setDaemon(true);
//        thread.start();
//        messageListeners.add(thread);
        System.out.println("new players count " + MainServer.m.playersSize());
//        cl.sendState(MainServer.m.getPlayersData());
    }

    void messageListener(Client cl) {
        try {
            while (true) {
                Msg msg = cl.readMsg();

                synchronized (Thread.currentThread()) {
                    switch (msg.getAction()) {
//                        case CONNECT:
//                            System.out.println("Client say: " + msg.message);
//                            break;
                        case CONNECTION_ERROR:
                            throw new Exception(msg.message);
                        case CLIENT_STATE:
                            if (msg.clientState == ClientState.READY) {
                                this.countReadyPlayers++;
                                if (countReadyPlayers == 4) gameThread.start();
                            }
                            else if (msg.clientState == ClientState.NOT_READY)
                                this.countReadyPlayers--;
                            break;
                        case SET_NAME:
//                            MainServer.m.getDao().setClientName(cl.getSocket(), msg.message);
                            cl.setName(msg.message);
                            addPlayer(cl);
//                            cl.sendMsg(new Msg("", MsgAction.CLIENT_CONNECTED));
                            break;
                        case GAME_STOPPED:
                            if (gameThread != null && gameThread.isAlive()) {
                                //TODO: Возможно надо wait()
                                gameThread.interrupt();
                                gameThread = null;
                            }
                            break;
                        case UPDATE_GAME_STATE:
                            if (msg.arrow != null) {
                                MainServer.m.getDao().getClientsData().updateArrow(msg.arrow);
                                MainServer.m.event();
                            }
                            break;
                        default:
                            System.out.println("Get message: " + msg.action);
                            break;
                    }
                }
            }

        } catch (Exception e) {
            try {
                cl.getSocket().close();
                System.err.println("Client " + cl.getSocket().getPort() + " closed.");
                System.err.println("GameServer: " + e.getMessage());
                synchronized (this) {
                    messageListeners.remove(MainServer.m.getPlayerIndex(cl.getSocket()));
                    MainServer.m.removePlayer(cl);
                    MainServer.m.removeObserver(cl.getIObserver());
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

    //TODO: Запуск цикла среверной части
    void run() {
        while (true) {

        }
    }

    public Client getLastClient() {
        return MainServer.m.getClient(-1);
    }

    @Override
    public void event(GameModel m) {}
}
