package com.example.lunchmeet.lunchmeet;

/**
 * Java code for Tuple class representing the location coordinates of users on the map
 * @author Isaac Yeo
 */

public class Tuple<X, Y> {
    private final X x;
    private final Y y;
    /**
     * Constructor for coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return latitude
     */
    public X getLat() { return x; }
    /**
     * @return longitude
     */
    public Y getLng() { return y; }
}
