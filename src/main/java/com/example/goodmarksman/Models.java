package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;

public class Models {
    static GameModel gm = new GameModel();

    public static GameModel buildGM() {
        return gm;
    }
}
