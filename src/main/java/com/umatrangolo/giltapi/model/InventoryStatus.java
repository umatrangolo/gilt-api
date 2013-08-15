package com.umatrangolo.giltapi.model;

public enum InventoryStatus {
    SoldOut("sold out"),
    ForSale("for sale"),
    Reserver("reserved");

    private final String key;

    public String getKey() {
        return key;
    }

    InventoryStatus(String key) {
        this.key = key;
    }
}
