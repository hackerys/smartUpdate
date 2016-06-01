package jansen.com.smartupdate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    private Handler handler = new Handler();

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incremnt_layout);
        AppInfo mInfo = new AppInfo(this);
        this.button = (Button) this.findViewById(R.id.button);
        this.button.setOnClickListener(this);
        this.textView = (TextView) this.findViewById(R.id.textView);
        this.textView.setText("我的版本是" + mInfo.getVersionName());
        Toast.makeText(this, "新的的安装包", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(button)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File rootFile = Environment.getExternalStorageDirectory();
                        File patchFile = new File(rootFile, "patch.patch");
                        if (patchFile.exists()) {
                            File newApkFile = new File(rootFile, "newApkFile.apk");
                            int state = UpdateUtil.bspatch(UpdateUtil.getApkDerectory(MainActivity.this),
                                    newApkFile.getAbsolutePath(), patchFile.getAbsolutePath());
                            Log.e("bspatch", "" + state);

                            //删除差异文件
//                            patchFile.delete();

                            //安装新的apk文件
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(newApkFile),
                                    "application/vnd.android.package-archive");
                            startActivity(intent);
                        } else {//无差异文件
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "无补丁文件", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
