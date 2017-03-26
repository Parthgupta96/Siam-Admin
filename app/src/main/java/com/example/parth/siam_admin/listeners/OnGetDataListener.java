package com.example.parth.siam_admin.listeners;

/**
 * Created by Parth on 03-02-2017.
 */

public interface OnGetDataListener<T> {
    public void onStart();
    public void onSuccess(T o);

}
