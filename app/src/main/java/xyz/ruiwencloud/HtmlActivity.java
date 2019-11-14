package xyz.ruiwencloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class HtmlActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView;
    private Handler handler;
    private String[] msm = {null};
    private Integer style_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        handler=new Handler();

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("HTML源码");
        setSupportActionBar(toolbar);
        textView = (TextView)findViewById(R.id.html_text);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String message = textView.getText().toString();
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", message);
                cmb.setPrimaryClip(clip);
                Toast.makeText(HtmlActivity.this,"复制成功啦",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //ToolBar 左侧按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        style_color = intent.getIntExtra("color",0);
        Change_color(style_color);
        assert data != null;
        Log.d("aaaaaa","a"+data);
        FitTextView(data);
    }
    //ToolBar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.html_menu, menu);
        return true;
    }
    //菜单绑定事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //改变主题颜色
            case R.id.html_menu:
                String message = textView.getText().toString();
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", message);
                cmb.setPrimaryClip(clip);
                Toast.makeText(HtmlActivity.this,"复制成功啦",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    private void FitTextView(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(url.contains("http")){
                        msm[0] = Tools.Get(url,HtmlActivity.this);
                    }else{
                        try {
                            msm[0] = Tools.Get("http://"+url,HtmlActivity.this);
                        }catch (Exception e){
                            e.printStackTrace();
                            msm[0] = Tools.Get("https://"+url,HtmlActivity.this);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    msm[0] = "你好像没有网啊，宝贝儿";
                }
                handler.post(runnableUi);
            }
        }).start();
    }
    Runnable runnableUi=new Runnable(){
        @Override
        public void run() {
            textView.setText(msm[0]);
        }

    };
    private void Change_color(Integer style_color){
        int color = 0;
        switch (style_color){
            case 0:
                color = R.color.pink;
                break;
            case 1:
                color = R.color.skyblue;
                break;
            case 2:
                color = R.color.gray;
                break;
            case 3:
                color = R.color.green;
                break;
        }
        Set_color(color);
    }
    private void Set_color(int color){
        toolbar.setBackgroundColor(getResources().getColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
}
