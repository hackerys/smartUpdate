package jansen.com.smartupdate;

import android.content.Context;

/**
 * Created Jansen on 2016/4/5.
 */
public class AppInfo {
    private String pkName;
    private String versionName;
    private int versionCode;
    private Context mContext;

    public AppInfo(Context mContext) {
        this.mContext = mContext;
        getAppInfo();
    }

    private void getAppInfo() {
        try {
             pkName = mContext.getPackageName();
             versionName = mContext.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
             versionCode = mContext.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
        } catch (Exception e) {
        }
    }

    public String getPkName() {
        return pkName;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }
}
