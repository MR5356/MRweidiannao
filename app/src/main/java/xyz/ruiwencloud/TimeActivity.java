package xyz.ruiwencloud;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {
    private TextView tv_time;
    private TextView tv_data;
    private static final int msgKey1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);//横屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        tv_time = (TextView)findViewById(R.id.time_time);
        tv_data = (TextView)findViewById(R.id.time_data);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);// 取得窗口属性
        int screenWidth = dm.widthPixels;// 窗口的宽度
        //int screemHeight= dm.heightPixels;//窗口的高度
        if(screenWidth<=1080){
            tv_time.setTextSize(56);
        }

        new TimeThread().start();
    }
    public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("HH : mm : ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd EEE");
                    tv_time.setText(format.format(date));
                    tv_data.setText(format2.format(date));
                    break;
                default:
                    break;
            }
        }
    };
}
