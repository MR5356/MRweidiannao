package xyz.ruiwencloud;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class about_usActivity extends AppCompatActivity {
    private Button join_us;
    private Button share_us;
    private Integer style_color;
    private Toolbar toolbar;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Button weixin;
    private Button jianshu;
    private Button suppose;
    private Button shared_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appbar = (AppBarLayout)findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        Intent intent = getIntent();
        style_color = intent.getIntExtra("color",0);
        Change_color(style_color);

        join_us = (Button)findViewById(R.id.join_us);
        join_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinQQGroup("j_l8CoK40hPgcwzKe8oW9c0UpWaCu-a7");
            }
        });
        share_us = (Button)findViewById(R.id.share_us);
        share_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "点击下载 微电脑 app，一起体验酷酷的应用 http://ruiwencloud.xyz/app/RWCloud.apk 微信请复制到浏览器打开下载");
                startActivity(Intent.createChooser(textIntent, "分享这个酷酷的App"));
            }
        });
        weixin = (Button)findViewById(R.id.weixin);
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", "MRweidiannao");
                cmb.setPrimaryClip(clip);
                Toast.makeText(about_usActivity.this,"微信公众号ID已经成功复制，请手动到微信搜索",Toast.LENGTH_SHORT).show();
            }
        });
        jianshu = (Button)findViewById(R.id.jianshu);
        jianshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.jianshu.com/u/6cbbca425998");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        shared_app = (Button)findViewById(R.id.shared_app);
        shared_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/MR5356/MRweidiannao");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        suppose = (Button)findViewById(R.id.suppos);
        suppose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_up  = new AlertDialog.Builder(about_usActivity.this);
                builder_up.setTitle("" ) ;
                builder_up.setMessage("点击加入官方QQ群进行反馈") ;
                builder_up.setPositiveButton("点击加入" ,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinQQGroup("j_l8CoK40hPgcwzKe8oW9c0UpWaCu-a7");
                    }
                });
                builder_up.setNegativeButton("我再想想",null);
                builder_up.show();
            }
        });

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
    }
    /****************
     *
     * 发起添加群流程。群号：微电脑(971759316) 的 key 为： j_l8CoK40hPgcwzKe8oW9c0UpWaCu-a7
     * 调用 joinQQGroup(j_l8CoK40hPgcwzKe8oW9c0UpWaCu-a7) 即可发起手Q客户端申请加群 微电脑(971759316)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
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
        appbar.setBackgroundColor(getResources().getColor(color));
        collapsingToolbarLayout.setBackgroundColor(getResources().getColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
}
