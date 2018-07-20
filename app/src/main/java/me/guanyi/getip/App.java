package me.guanyi.getip;

import android.app.Application;

import com.vondear.rxtool.RxTool;


/**
 * Created by ajf-dell on 2018/7/18.
 */

public class App extends Application {
    private static final String TAG = "GitIp App";


    @Override
    public void onCreate() {
        super.onCreate();

        RxTool.init(this);
    }
}
