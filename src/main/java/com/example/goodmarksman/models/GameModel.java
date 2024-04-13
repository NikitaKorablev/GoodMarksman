package com.example.goodmarksman.models;

//import com.example.goodmarksman.models.Game;
//import com.example.goodmarksman.models.SCGame;
import com.example.goodmarksman.DAO;
import com.example.goodmarksman.IObserver;
import com.example.goodmarksman.Msg;
import com.example.goodmarksman.objects.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class GameModel implements Iterable<Score> {
    private Pane gameView;
    private Circle smallTarget;
    private Circle bigTarget;
    private Polygon arrow;
    private DAO dao;
    private ArrayList<Text> scoreList;
    private ArrayList<IObserver> allO = new ArrayList<>();

    public GameModel() {
        this.dao = new DAO();
    }

    //TODO: ХЗ зачем оно мне надо
    @Override
    public Iterator<Score> iterator() {
        return dao.iterator();
    }

    public void event() {
        for (IObserver o: allO) {
            o.event(this);
        }
    }

    public void addObserver(IObserver o) {
        allO.add(o);
    }

    public int playersSize() {
        return dao.playersSize();
    }

    public void addClient(Client cl, Arrow arrow, Score score) {
        dao.addClient(cl, arrow, score);
        event();
    }

    public Data getClientData(Socket s) {
        return dao.getClientData(s.getPort());
    }

    public ClientsData getPlayersData() {
        return dao.getPlayersData();
    }

    public int getPlayerIndex(Socket s) {
        return dao.playerIndex(s.getPort());
    }

    public void removePlayer(Client cl) {
        dao.removeClient(cl);
    }

    public Pane getGameView() { return gameView; }
    public void setGameView(Pane gameView) { this.gameView = gameView; }

    public Circle getSmallTarget() { return smallTarget; }
    public void setSmallTarget(Circle smallTarget) { this.smallTarget = smallTarget; }

    public Circle getBigTarget() { return bigTarget; }
    public void setBigTarget(Circle bigTarget) { this.bigTarget = bigTarget; }

    public Polygon getArrow() { return arrow; }
    public void setArrow(Polygon arrow) { this.arrow = arrow; }
}
