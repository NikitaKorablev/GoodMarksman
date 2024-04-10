package com.example.goodmarksman;

import com.example.goodmarksman.objects.Client;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class GameClient implements IObserver {

    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    MsgAction gameState = MsgAction.GAME_STOPPED;

    private Client server;
    private Thread msgListener = null;
    public boolean nameChecked = false;
//    Socket cs;
//    InputStream is;
//    OutputStream os;
//    DataInputStream dis;
//    DataOutputStream dos;

    // Подключение к серверу
    public GameClient(Socket cs) throws Exception {
        server = new Client(cs);

        System.out.println("Client connected");
        Msg msg = readMsg();
        System.out.println(msg);
        if (msg.getAction() == MsgAction.CONNECTION_ERROR) {
            System.err.println(msg.message);
            server.setConnected(false);
            nameChecked = true;
            server.getSocket().close();
            throw new Exception(msg.message);
        }

        msgListener = new Thread(this::messageListener);
        msgListener.start();
//        new Thread(this::run).start();
    }

    void messageListener() {
        boolean listening = true;

        while (listening) {
            try {
                Msg msg = readMsg();
                System.out.println(msg);
                if (msg.getAction() == MsgAction.CONNECTED) {
                    System.out.println(msg.message);
                    server.setConnected(true);
                    nameChecked = true;
                }
                if (msg.getAction() == MsgAction.CONNECTION_ERROR) {
                    System.err.println(msg.message);
                    server.setConnected(false);
                    nameChecked = true;
                    listening = false;
                    server.getSocket().close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
    }

    //TODO: Запуск цикла клиентской части
    void run() {
//        while (true) {
//            Resp r = readResp();
//            m.set(r.getPoints());
//        }
    }

    public boolean isConnected() {
        return server.isConnected();
    }

    public Resp readResp(Client cl) throws IOException {
        Resp r = null;
        try {
            String respStr = cl.getDis().readUTF();
            r = gson.fromJson(respStr, Resp.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public Msg readMsg() throws IOException {
        Msg msg;
        try {
            String respStr = server.getDis().readUTF();
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

    public void sendMsg(Msg msg) {
        try {
            String strMsg = gson.toJson(msg);
            System.out.println(strMsg);
            server.getDos().writeUTF(strMsg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void event(GameModel m) {

    }
}
