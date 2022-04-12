package com.iamtanshu.tapingap.model;

import android.view.View;

public class LastView {
    private View view = null;
    private int color = -1;
    private static LastView instance;

    private int viewToClick = -1;

    private LastView() {
    }

    public synchronized static LastView getInstance() {
        if (instance == null) {
            instance = new LastView();
        }
        return instance;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getViewToClick() {
        return viewToClick;
    }

    public void setViewToClick(int viewToClick) {
        this.viewToClick = viewToClick;
    }

}
