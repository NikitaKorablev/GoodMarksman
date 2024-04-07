package com.example.goodmarksman;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class GameServer implements IObserver {

    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    MsgAction gameState = MsgAction.GAME_STOPPED;
    boolean isPaused = false;
    Thread sendState = null;
    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    // Подключение к серверу
    public GameServer(Socket cs) {
        this.cs = cs;
        try {
            os = this.cs.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        new Thread(this::messageLisner).start();
        new Thread(this::run).start();
    }

    void messageLisner() {
        try {
            is = this.cs.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        while (true) {
            Msg msg = readMsg();
            if (msg.getAction() == MsgAction.CONNECT) {
                System.out.println(msg.message);
//                try {
//                    synchronized (this) { gameState = MsgAction.GAME_START; }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
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

    public Resp readResp() {
        Resp r = null;
        try {
            String respStr = dis.readUTF();
            r = gson.fromJson(respStr, Resp.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public Msg readMsg() {
        Msg msg = null;
        try {
            String respStr = dis.readUTF();
            msg = gson.fromJson(respStr, Msg.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return msg;
    }

    public void sendResp(Resp resp) {
        String strResp = gson.toJson(resp);
        try {
            dos.writeUTF(strResp);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMsg(Msg msg) {
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
