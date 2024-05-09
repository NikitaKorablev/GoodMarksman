package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;

public class Models {
    static GameModel gm;

    public static GameModel buildGM(boolean isServer) {
        gm = new GameModel(isServer);
        return gm;
    }
}
