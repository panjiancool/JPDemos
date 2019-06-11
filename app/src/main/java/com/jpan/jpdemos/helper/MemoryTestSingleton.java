package com.jpan.jpdemos.helper;

import android.content.Context;

public class MemoryTestSingleton {
    private Context mContext;
    private static MemoryTestSingleton instance;

    private MemoryTestSingleton(Context context) {
        mContext = context;
    }

    public static synchronized MemoryTestSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MemoryTestSingleton(context);
        }
        return instance;
    }
}
