package com.example.lunchmeet.lunchmeet;

import java.util.List;

/**
 * Created by Brian on 11/6/2017.
 */

public interface DBObserver<T> {
    public void run(T param);
}