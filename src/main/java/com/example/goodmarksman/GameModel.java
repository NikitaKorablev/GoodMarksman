package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
//import com.example.goodmarksman.models.SCGame;
import com.example.goodmarksman.objects.Arrow;
import com.example.goodmarksman.objects.COLORS;
import com.example.goodmarksman.objects.Score;
import com.example.goodmarksman.objects.Target;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class GameModel implements Iterable<Score> {
    Target target1;
    Target target2;
    Arrow arrow;
    DAO dao;
    ArrayList<IObserver> allO = new ArrayList<>();

    public GameModel() {
        this.target1 = new Target(COLORS.BLUE, 10, 350, 120, -1, 1, 1);
        this.target2 = new Target(COLORS.RED, 18, 292, 120, 1, 2, 2);

        this.arrow = new Arrow();
    }

    @Override
    public Iterator<Score> iterator() {
        return dao.iterator();
    }

    void event() {
        for (IObserver o: allO) {
            o.event(this);
        }
    }

    public void addObserver(IObserver o) {
        allO.add(o);
    }

    void set(ArrayList<Score> scoreBord) {
        dao.set(scoreBord);
        event();
    }

    public Msg updateState() {



        event();
        return new Msg("");
    }

    public ArrayList<Score> getScoreBoard() {
        return dao.getScoreBoard();
    }

    public void addScore(Score s) {
        dao.add(s);
        event();
    }
    public void removeScore(Socket s) {
//        dao.remove(s);
        event();
    }
}
