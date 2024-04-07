//package com.example.goodmarksman;
//
//import com.example.goodmarksman.models.Game;
//import com.example.goodmarksman.models.SCGame;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class Server {
//    private final int port = 3000;
//    private InetAddress ip = null;
//    private ServerDataStreamer sds;
//
//    private SCGame game;
//
//    Server() {
//        game = new Model().getSCGame();
//    }
//
//    public void startServer() {
//        ServerSocket ss;
//        Socket cs;
//        InputStream is;
//        OutputStream os;
//        DataInputStream dis;
//        DataOutputStream dos;
//
//        try {
//            this.ip = InetAddress.getLocalHost();
//            ss = new ServerSocket(port, 0, ip);
//            System.out.append("Server start\n");
//
//            cs = ss.accept();
//            System.out.println("Client connect (" + cs.getPort() + ")");
//
//            is = cs.getInputStream();
//            os = cs.getOutputStream();
//
//            dis = new DataInputStream(is);
////                ois = new ObjectInputStream(is);
//            dos = new DataOutputStream(os);
//
//            sds = new ServerDataStreamer(dis, dos);
//
//            game.startGame(sds);
//        } catch(Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//
//
//    }
//
//
//
//}
