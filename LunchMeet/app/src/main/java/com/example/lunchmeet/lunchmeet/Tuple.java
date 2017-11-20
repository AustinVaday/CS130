package com.example.lunchmeet.lunchmeet;

/**
 * Created by yeo on 11/19/17.
 */

public class Tuple<X, Y> {
    private final X x;
    private final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public X getLat() { return x; }
    public Y getLng() { return y; }
}
