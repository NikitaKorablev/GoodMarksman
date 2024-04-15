package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import com.example.goodmarksman.objects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

//TODO: Будет отвечать за ведение счета всех игроков
// и хранение игровых объектов
public class DAO implements Iterable<Score> {
    ClientsData clientsData = new ClientsData();
    ArrayList<Client> players = new ArrayList<>();

    public int playersSize() {
        return players.size();
    }

    public void addClient(Client cl, Arrow arrow, Score score) throws Exception {
        if (players.contains(cl) || clientsData.getIndex(cl.getSocket().getPort()) != -1) {
            throw new Exception("Player is already added.");
        }

        players.add(cl);
        clientsData.add("", cl.getSocket().getPort(),
                arrow, score);
    }

    public void sendMsg(Msg msg) throws IOException {
        if (msg.portOwner == -1) throw new IOException("Port owner not set.");
        Client client = players.get(playerIndex(msg.portOwner));
        client.sendMsg(msg);
    }

    public Client getClient(int ind) {
        if (ind == -1 && !players.isEmpty()) { return players.get(players.size() - 1); }
        return players.get(ind);
    }

    public void removeClient(Client cl) {
        clientsData.remove(cl.getSocket().getPort());
        players.remove(cl);
    }

    public int playerIndex(int port) {
        return clientsData.getIndex(port);

//        int i = 0;
//        for (Client cl : players) {
//            if (cl.getSocket().getPort() == port) { return i; }
//            i++;
//        }
//        return -1;
    }

    public Data getClientData(int port) {
        return clientsData.getData(port);
    }

    public ClientsData getPlayersData() {
        return clientsData;
    }

//    ArrayList<Score> scoreBoard = new ArrayList<>();

//    public Game getGame(Circle target1, Circle target2, Polygon arrow) {
////        return new Game(target1, target2, arrow);
//    }

//    public ArrayList<Score> getScoreBoard() {
//        return scoreBoard;
//    }

//    public void set(ArrayList<Score> scoreBoard) {
//        this.scoreBoard = scoreBoard;
//    }

//    public void add(Score score) {
//        this.scoreBoard.add(score);
//    }

//    public void remove(Socket cs) {
//        this.scoreBoard.removeIf(s -> s.getSocket() == cs);
//    }

    @Override
    public Iterator<Score> iterator() {
//        return scoreBoard.iterator();
        return null;
    }

    @Override
    public void forEach(Consumer<? super Score> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Score> spliterator() {
        return Iterable.super.spliterator();
    }

    //    ArrayList<Point> allPoint = new ArrayList<>();
//
//    void set(ArrayList<Point> allPoint) {
//        this.allPoint = allPoint;
//    }
//
//    public ArrayList<Point> getPoints() {
//        return allPoint;
//    }
//
//    void add(Point p) {
//        allPoint.add(p);
//    }
//
//    void remove(Point p) {
//        allPoint.remove(p);
//    }
//
//    @Override
//    public Iterator<Point> iterator() {
//        return allPoint.iterator();
//    }
//
//    @Override
//    public void forEach(Consumer<? super Point> action) {
//        Iterable.super.forEach(action);
//    }
//
//    @Override
//    public Spliterator<Point> spliterator() {
//        return Iterable.super.spliterator();
//    }
}
