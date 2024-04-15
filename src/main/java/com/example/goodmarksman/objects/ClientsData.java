package com.example.goodmarksman.objects;

import java.util.ArrayList;

public class ClientsData {
    private final ArrayList<Data> clientsData = new ArrayList<>();

    public ClientsData() {}
    public ClientsData(Data data) {
        clientsData.add(data);
    }
    public ClientsData(String playerName, int socketPort, Arrow arrow, Score score) {
        clientsData.add(new Data(playerName, socketPort, arrow, score));
    }
    public ClientsData(ArrayList<Data> data) {
        clientsData.addAll(data);
    }

    public void add(Data data) { clientsData.add(data); }
    public void add(String playerName, int socketPort, Arrow arrow, Score score) {
        clientsData.add(new Data(playerName, socketPort, arrow, score));
    }

    public Data getData(int socketPort) {
        for (Data data : clientsData) {
            if (data.getPlayerPort() == socketPort) {
                return data;
            }
        }
        return null;
    }

    public int getIndex(int socketPort) {
//        System.out.println(clientsData);
        //TODO: Приходит неверный сокет
        int i = 0;
        for (Data data : clientsData) {
            if (data.getPlayerPort() == socketPort) { return i; }
            i++;
        }
        return -1;
    }

    public void remove(int socketPort) {
//        System.out.println(this.getData(socketPort));
        clientsData.remove(getData(socketPort));
//        System.out.println(clientsData);
//        clientsData.remove(getIndex(socketPort));
    }

    public ArrayList<Data> getClientsData() { return clientsData; }

    @Override
    public String toString() {
        return "ClientsData{" +
                "clientsData=" + clientsData +
                '}';
    }
}
