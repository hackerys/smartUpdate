package jansen.com.smartupdate;

import android.content.Context;

/**
 * Created Jansen on 2016/5/31.
 */
public class UpdateUtil {
    static {
        System.loadLibrary("bspatch");
    }

    /**
     * 获取安装包的目录位置
     *
     * @param context 上下文对象
     * @return
     */
    public static String getApkDerectory(Context context) {
        return context.getApplicationInfo().sourceDir;
    }

    /**
     * 合并产生安装包文件
     *
     * @param oldApkPath
     * @param patchPath
     * @param newApkPath
     * @return
     */
    public native static int bspatch(String oldApkPath,
                                     String newApkPath, String patchPath);
}
