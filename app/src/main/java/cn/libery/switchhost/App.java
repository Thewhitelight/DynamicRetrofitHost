package cn.libery.switchhost;

import android.app.Application;

/**
 * @author shizhiqiang on 2018/5/15.
 */
public class App extends Application {

    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }
}
