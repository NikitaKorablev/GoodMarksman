package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import com.example.goodmarksman.objects.Score;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

//TODO: Будет отвечать за ведение счета всех игроков
// и хранение игровых объектов
public class DAO implements Iterable<Score> {



    ArrayList<Score> scoreBoard = new ArrayList<>();

//    public Game getGame(Circle target1, Circle target2, Polygon arrow) {
////        return new Game(target1, target2, arrow);
//    }

    public ArrayList<Score> getScoreBoard() {
        return scoreBoard;
    }

    public void set(ArrayList<Score> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void add(Score score) {
        this.scoreBoard.add(score);
    }

//    public void remove(Socket cs) {
//        this.scoreBoard.removeIf(s -> s.getSocket() == cs);
//    }

    @Override
    public Iterator<Score> iterator() {
        return scoreBoard.iterator();
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
