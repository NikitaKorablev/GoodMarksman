package com.example.goodmarksman;

import com.example.goodmarksman.objects.Client;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer implements IObserver {
    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    private final ArrayList<Client> players = new ArrayList<>();
    private final ArrayList<Thread> threads = new ArrayList<>();

    MsgAction gameState = MsgAction.GAME_STOPPED;
    boolean isPaused = false;
    Thread sendState = null;
//    Socket cs;
//    InputStream is;
//    OutputStream os;
//    DataInputStream dis;
//    DataOutputStream dos;

    // Подключение к серверу
    public GameServer(Socket cs) {
        addPlayer(cs);
//        new Thread(this::run).start();
    }

    public MsgAction addPlayer(Socket cs) {
        System.out.println("players count " + players.size());
        if (players.size() >= 4) {
            return MsgAction.CONNECTION_ERROR;
        }
        Client cl = new Client(cs);
        players.add(cl);
        System.out.println("new players count " + players.size());
        Thread thread = new Thread(() -> messageListener(cl));
        thread.start();
        threads.add(thread);
        return MsgAction.CONNECTED;
    }

    void messageListener(Client cl) {
        System.out.println(players);

        while (true) {
            try {
                Msg msg = readMsg(cl.getDis());
                if (msg.getAction() == MsgAction.CONNECTED) {
                    System.out.println(msg.message);
                }
            } catch (IOException e) {
                System.err.println("Client " + cl.getSocket().getPort() + " disconnected: " + e.getMessage());

                threads.remove(players.indexOf(cl));
                players.remove(cl);
                return;
            }
        }
    }

    //TODO: Запуск цикла среверной части
    void run() {
        while (true) {
            if (gameState == MsgAction.GAME_START) {
//                if (isPaused) {
//                    try {
//                        synchronized (gameThread) {
//                            try {
//                                gameThread.wait();
//                            } catch (InterruptedException e) {
//                                System.err.println(e);
//                                this.stopGame();
//                            }
//                        }
//                    } catch (Exception e) {
//                        System.err.println(e);
//                        this.stopGame();
//                    }
//                }

                m.updateState();

//                if (arrow.getIsShooting()) {
//                    m.updateArrowState();
//                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException err) {
                    System.err.println(err);
                }
            }
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

    public Msg readMsg(DataInputStream dis) throws IOException {
        Msg msg;
        try {
            String respStr = dis.readUTF();
            System.out.println(respStr);
            msg = gson.fromJson(respStr, Msg.class);
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return msg;
    }

//    public void sendResp(Resp resp) {
//        String strResp = gson.toJson(resp);
//        try {
//            dos.writeUTF(strResp);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public void sendMsg(DataOutputStream dos, Msg msg) {
        String strMsg = gson.toJson(msg);
        try {
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void event(GameModel m) {
//        sendMsg(m.lastChanges());
    }
}
