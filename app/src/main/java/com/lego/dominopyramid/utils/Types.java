package com.lego.dominopyramid.utils;

public enum Types {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6");

    private String id;

    Types(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public static Types getTypeByID(int id) {
        for (Types type : values()) {
            if (type.toString().equals(String.valueOf(id))) {
                return type;
            }
        }
        return null;
    }

    public int getIDByType() {
        return Integer.valueOf(id);
    }
}