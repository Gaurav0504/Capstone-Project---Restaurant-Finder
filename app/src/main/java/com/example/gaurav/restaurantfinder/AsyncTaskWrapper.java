package com.example.gaurav.restaurantfinder;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.concurrent.Callable;

public class AsyncTaskWrapper<V> {

    @SuppressLint("StaticFieldLeak")
    public void doTask(Runnable runnable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                runnable.run();
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public Callable<V> doTaskAndReturn(Callable<V> callable) {
        new AsyncTask<Void, Void, V>() {

            @Override
            protected V doInBackground(Void... voids) {
                try {
                    return callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        return null;
    }
}
