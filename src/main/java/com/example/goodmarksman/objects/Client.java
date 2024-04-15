package com.example.goodmarksman.objects;

import com.example.goodmarksman.IObserver;
import com.example.goodmarksman.Msg;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    Socket cs = null;
    InputStream is = null;
    OutputStream os = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    Gson gson = new Gson();

    IObserver observer = null;

//    Arrow arrow = null;
//    Score score = null;


    public Client(Socket cs) {
        this.cs = cs;

        try {
            is = this.cs.getInputStream();
            dis = new DataInputStream(is);

            os = this.cs.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

//        this.arrow = arrow == null ? new Arrow() : arrow;
//        this.score = score == null ? new Score() : score;
    }
    public Socket getSocket() { return cs; }

    public InputStream getIs() { return is; }
    public void setIs(InputStream is) { this.is = is; }

    public OutputStream getOs() { return os; }
    public void setOs(OutputStream os) { this.os = os; }

    public DataInputStream getDis() { return dis; }
    public void setDis(DataInputStream dis) { this.dis = dis; }

    public DataOutputStream getDos() { return dos; }
    public void setDos(DataOutputStream dos) { this.dos = dos; }

    public void setIObserver(IObserver observer) { this.observer = observer; }
    public IObserver getIObserver() { return observer; }

//    public boolean isConnected() { return isConnected; }
//    public void setConnected(boolean connected) { isConnected = connected; }

//    public Arrow getArrow() { return arrow; }
//    public void setArrow(Arrow arrow) { this.arrow = arrow; }
//
//    public Score getScore() { return score; }
//    public void setScore(Score score) { this.score = score; }

    public Msg readMsg() throws IOException {
        Msg msg;
        try {
            String respStr = dis.readUTF();
            System.out.println(respStr);
            msg = gson.fromJson(respStr, Msg.class);
        } catch(IOException e) {
            System.err.println(e.getMessage());
            throw e;
        }
        return msg;
    }

    public void sendMsg(Msg msg) throws IOException {
        try {
            System.out.println(msg);
            String strMsg = gson.toJson(msg);
            dos.writeUTF(strMsg);
        } catch (Exception e) {
            System.err.println(msg);
            System.err.println("Error in sendMessage(): " + e.getMessage());
            throw e;
        }
    }

    public void sendState(ArrayList<Data> data) {
        try {
//            System.out.println(cl.getSocket().getPort());

            Msg message = new Msg(data, MsgAction.UPDATE_GAME_STATE);
            sendMsg(message);
        } catch (IOException e) {
            System.err.println("Error of send data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "cs=" + cs +
                '}';
    }
}
