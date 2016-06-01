package jansen.com.smartupdate.base;

import android.app.Application;
import android.content.Context;

/**
 * Created Jansen on 2016/3/30.
 */
public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
