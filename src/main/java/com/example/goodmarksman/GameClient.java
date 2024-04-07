package com.example.goodmarksman;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class GameClient implements IObserver {

    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    MsgAction gameState = MsgAction.GAME_STOPPED;

    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    // Подключение к серверу
    public GameClient(Socket cs) {
        this.cs = cs;
        try {
            os = cs.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        new Thread(this::run).start();
    }
    //TODO: Запуск цикла клиентской части
    void run() {
        try {
            is = cs.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        while (true) {
            Resp r = readResp();
//            m.set(r.getPoints());
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
        System.out.println(msg.message);
        try {
            String strMsg = gson.toJson(msg);
            System.out.println(strMsg);
            dos.writeUTF(strMsg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void event(GameModel m) {

    }
}
