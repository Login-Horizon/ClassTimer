package com.example.classtimer.Bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Руслан on 27.01.2015.
 */
public class BusProvider {

    private static Bus mBus;

    private BusProvider() {
    }

    public static Bus getInstance() {

        if (mBus == null) {
            mBus = new Bus(ThreadEnforcer.MAIN);
        }

        return mBus;
    }
}
