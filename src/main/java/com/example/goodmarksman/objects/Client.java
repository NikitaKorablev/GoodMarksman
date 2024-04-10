package com.example.goodmarksman.objects;

import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket cs;
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    private boolean isConnected = false;



    public Client(Socket cs) {
        this.cs = cs;

        try {
            is = this.cs.getInputStream();
            dis = new DataInputStream(is);

            os = this.cs.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public String toString() {
        return "Client{" +
                "cs=" + cs +
                ", isConnected=" + isConnected +
                '}';
    }
}
