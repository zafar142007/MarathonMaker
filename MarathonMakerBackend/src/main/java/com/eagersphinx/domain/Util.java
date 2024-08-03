package com.eagersphinx.domain;

public class Util {

    public static String getEvent(String event) {
        return (event.length()>7 ?
                event.substring(0, 7).toUpperCase():
                event.toUpperCase());
    }
}
