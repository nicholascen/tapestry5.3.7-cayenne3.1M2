package org.panther.tap5cay3.model;

import org.panther.tap5cay3.model.auto._Tap5cay3;

public class Tap5cay3 extends _Tap5cay3 {

    private static Tap5cay3 instance;

    private Tap5cay3() {}

    public static Tap5cay3 getInstance() {
        if(instance == null) {
            instance = new Tap5cay3();
        }

        return instance;
    }
}
