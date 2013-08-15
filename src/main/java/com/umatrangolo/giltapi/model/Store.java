package com.umatrangolo.giltapi.model;

public enum Store {
    Women("women"),
    Men("men"),
    Kids("kids"),
    Home("home");

    private final String key;

    public String getKey() {
        return key;
    }

    Store(String key) {
        this.key = key;
    }
}
