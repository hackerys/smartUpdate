package jansen.com.smartupdate.base;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import jansen.com.smartupdate.MainActivity;

/**
 * Created Jansen on 2016/3/17.
 * 用于程序崩溃但是错误没有输出到logcat的情况
 * 输出目录:wn目录
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getSimpleName();
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    //  public static final String logPath= Environment.getExternalStorageDirectory();
    private CrashHandler() {
    }


    public static CrashHandler getInstance() {
        return INSTANCE;
    }


    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        File file = new File(Environment.getExternalStorageDirectory(), "jansen/error");
        boolean mkSuccess;
        if (!file.isDirectory()) {
            mkSuccess = file.mkdirs();
            if (!mkSuccess) {
                mkSuccess = file.mkdirs();
            }
        }

        StringBuffer sb = new StringBuffer();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String logFile = file.getPath() + File.separator + time + ".log";
            FileOutputStream fos = new FileOutputStream(logFile);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
        }

        // 此处示例发生异常后，重新启动应用
        Intent intent = new Intent(mContext, MainActivity.class);
        // 如果<span style="background-color: rgb(255, 255, 255); ">没有NEW_TASK标识且</span>是UI线程抛的异常则界面卡死直到ANR
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
/*        System.exit(10);*/
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        // new Handler(Looper.getMainLooper()).post(new Runnable() {
        // @Override
        // public void run() {
        // new AlertDialog.Builder(mContext).setTitle("提示")
        // .setMessage("程序崩溃了...").setNeutralButton("我知道了", null)
        // .create().show();
        // }
        // });
        return true;
    }
}