package xyz.ruiwencloud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences pref;
    private int style_color = 0;
    private SharedPreferences.Editor editor;
    private String update_check=null;
    //private TextView ohmylove_title;
    private TextView ohmylove_title2;
    private TextView ohmylove;
    private Button old_version;
    private Button dev_person;
    private Button clear_cache;
    private LinearLayout constellation;
    private LinearLayout nine_pic;
    private LinearLayout time_display;
    private LinearLayout express;
    private LinearLayout movie_search;
    private LinearLayout music_search;
    private LinearLayout pic2link;
    private LinearLayout poetry;
    private LinearLayout html;
    private LinearLayout dream;
    private Handler handler;
    private String[] msm = {null};
    private String msm_typ;
    private String cache_size;
    private LinearLayout cat_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler=new Handler();

        //获取页面元素
        //ohmylove_title = (TextView)findViewById(R.id.ohmylove_title);
        old_version = (Button)findViewById(R.id.old_version);
        ohmylove_title2 = (TextView)findViewById(R.id.ohmylove_title2);
        ohmylove = (TextView)findViewById(R.id.ohmylove);
        nine_pic = (LinearLayout)findViewById(R.id.nine_pic);
        time_display = (LinearLayout)findViewById(R.id.time_display);
        express = (LinearLayout)findViewById(R.id.express);
        movie_search = (LinearLayout)findViewById(R.id.movie_search);
        music_search = (LinearLayout)findViewById(R.id.music_search);
        pic2link = (LinearLayout)findViewById(R.id.pic2link);
        poetry = (LinearLayout)findViewById(R.id.poetry);
        html = (LinearLayout)findViewById(R.id.html);
        constellation = (LinearLayout)findViewById(R.id.constellation);
        dream = (LinearLayout)findViewById(R.id.dream);
        dev_person = (Button)findViewById(R.id.dev_person);
        clear_cache = (Button)findViewById(R.id.clear_cache);
        cat_money = (LinearLayout)findViewById(R.id.cat_money);
        //绑定事件
        SetOnclick();

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        String[] subtitles = {"时光不老，我们不散", "夏有乔木，雅望天堂", "回首万年，情衷伊人",
                "执子之手，与子偕老", "相识虽浅，似是经年", "静守时光，以待流年", "有美人兮，见之不忘",
                "衣带渐宽终不悔，为伊消得人憔悴", "死生契阔，与子成说。执子之手，与子偕老", "两情若是久长时，又岂在朝朝暮暮",
                "相思相见知何日？此时此夜难为情", "兽炉沈水烟，翠沼残花片，一行行写入相思传", "今夕何夕，见此良人",
                "野径云俱黑，江船火独明", "天涯地角有穷时，只有相思无尽处", "一种爱鱼心各异，我来施食尔垂钩",
                "有美人兮，见之不忘，一日不见兮，思之如狂", "千金纵买相如赋，脉脉此情谁诉", "山有木兮木有枝，心悦君兮君不知",
                "只身千里客，孤枕一灯秋", "直道相思了无益，未妨惆怅是清狂"};
        int nsubtitle = new Random().nextInt(subtitles.length);
        String subtitle = subtitles[nsubtitle];
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(subtitle);
        setSupportActionBar(toolbar);

        //ToolBar 左侧按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_update_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_check = "check";
                autoupdate autoupdate = new autoupdate();
                autoupdate.execute();
            }
        });

        //缓存
        Cache_manger();

        //储存设置
        style_color = pref.getInt("color",0);
        int color_before = 0;
        switch (style_color){
            case 0:
                color_before = R.color.pink;
                break;
            case 1:
                color_before = R.color.skyblue;
                break;
            case 2:
                color_before = R.color.gray;
                break;
            case 3:
                color_before = R.color.green;
                break;
        }
        Set_color(color_before);

        //自动检测更新
        autoupdate autoupdate = new autoupdate();
        autoupdate.execute();

        //测试代码区



        //首次启动引导
        int is_first = pref.getInt("isfirst",1);
        if (is_first==1){
            AlertDialog.Builder builder_up1  = new AlertDialog.Builder(MainActivity.this);
            builder_up1.setTitle("客官里边儿请" ) ;
            String msm = "\n  - 左上角按钮检测软件更新\n  - 右上角颜色盘可更换主题颜色\n  - 精选应用中带星号的为当前可用功能\n  - 老版本全面兼容可用\n\n更多玩法正在开发中\n";
            builder_up1.setMessage(msm) ;
            builder_up1.setCancelable(false);
            builder_up1.setPositiveButton("朕晓得啦", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder_up  = new AlertDialog.Builder(MainActivity.this);
                    builder_up.setTitle("选个风格吧" ) ;
                    final String[] msm_typs = {"wu","du","msm"};
                    final String[] msm_types= {"撩一点","毒一点","暖一点"};
                    builder_up.setCancelable(false);
                    builder_up.setSingleChoiceItems(msm_types, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            msm_typ = msm_typs[which];
                            Log.d("aaaaaaab",msm_typ);
                            editor = pref.edit();
                            editor.putInt("isfirst",0);
                            editor.putString("msm_typ",msm_typ);
                            editor.apply();
                            Setohmylove();
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
            builder_up1.show();

        }else{
            msm_typ = pref.getString("msm_typ","msm");
            Log.d("aaaaa",msm_typ);
        }

        //设置页面底部
        Setohmylove();
    }//OnCreate 结束

    //定义功能区
    private void SetOnclick(){
        old_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OldActivity.class));
            }
        });
        nine_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Nine_picActivity.class));
            }
        });
        time_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimeActivity.class));
            }
        });
        express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
            }
        });
        movie_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MovieActivity.class));
            }
        });
        music_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
            }
        });
        pic2link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
            }
        });
        poetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,WebBrowserActivity.class));
                Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
            }
        });
        dream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
            }
        });
        constellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"功能还在开发中",Toast.LENGTH_SHORT).show();
            }
        });
        html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("输入网址");
                //builder.setIcon(R.drawable.ic_launcher);
                final EditText html_text_pop = new EditText(MainActivity.this);
                html_text_pop.setHint("http(s)://");
                builder.setView(html_text_pop);
                builder.setPositiveButton("获取源码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(MainActivity.this,html_text_pop.getText(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,HtmlActivity.class);
                        intent.putExtra("data",html_text_pop.getText().toString());
                        intent.putExtra("color",style_color);
                        startActivity(intent);
                    }
                });
                //builder.setNegativeButton("否", null);
                builder.show();
            }
        });
        dev_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Dev_personActivity.class));
            }
        });
        clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请再次确认");
                builder.setMessage("是否删除所有缓存");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CleanMessageUtil.clearAllCache(getApplicationContext());
                        Cache_manger();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        cat_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("输入商品链接");
                //builder.setIcon(R.drawable.ic_launcher);
                final EditText history_text_pop = new EditText(MainActivity.this);
                history_text_pop.setHint("将商品链接粘贴进来");
                builder.setView(history_text_pop);
                builder.setPositiveButton("获取历史价格", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(MainActivity.this,html_text_pop.getText(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,History_moneyActivity.class);
                        intent.putExtra("url","http://ruiwencloud.xyz/app/functions/history_money.php?name="+history_text_pop.getText().toString());
                        startActivity(intent);
                    }
                });
                //builder.setNegativeButton("否", null);
                builder.show();
            }
        });
    }

    //ToolBar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    //菜单绑定事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //改变主题颜色
            case R.id.color_change:
                Change_color();
                break;
        }
        return true;
    }

    //切换主题颜色
    private void Change_color(){
        int color = 0;
        editor = pref.edit();
        switch (style_color){
            case 0:
                color = R.color.skyblue;
                style_color = 1;
                editor.putInt("color",style_color);
                break;
            case 1:
                color = R.color.gray;
                style_color = 2;
                editor.putInt("color",style_color);
                break;
            case 2:
                color = R.color.green;
                style_color = 3;
                editor.putInt("color",style_color);
                break;
            case 3:
                color = R.color.pink;
                style_color = 0;
                editor.putInt("color",style_color);
                break;
        }
        editor.apply();
        Set_color(color);
    }
    //界面底部文字
    private void Setohmylove(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    msm[0] = Tools.Get("http://ruiwencloud.xyz/app/msm/index.php?name="+msm_typ,MainActivity.this);
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
            ohmylove.setText(msm[0]);
        }

    };
    //缓存
    private void Cache_manger(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //缓存处理
                try {
                    cache_size = CleanMessageUtil.getTotalCacheSize(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    cache_size = "未知大小";
                }
                handler.post(cacheUi);
            }
        }).start();
    }
    Runnable cacheUi=new Runnable(){
        @Override
        public void run() {
            clear_cache.setText("清空缓存("+cache_size+")");
        }

    };
    //设置主题颜色
    private void Set_color(int color){
        toolbar.setBackgroundColor(getResources().getColor(color));
        //ohmylove_title.setTextColor(getResources().getColor(color));
        ohmylove_title2.setTextColor(getResources().getColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }

    //检测更新
    class autoupdate extends AsyncTask<String, Integer, String> {
        private String versionname;
        private String message;
        private String nurl;
        private String versioncode;
        private String version;

        @Override
        protected String doInBackground(String... params) {
            //update
            Map<String, String> results = Tools.serverVersion(MainActivity.this);
            versionname = results.get("versionname");
            message = results.get("message");
            nurl = results.get("nurl");
            versioncode = results.get("versioncode");
            version = Tools.getVersion(MainActivity.this);
            Log.d("versiona",version);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (version.equals(versionname)||nurl.equals("None")){
                if (update_check!=null){
                    Toast.makeText(MainActivity.this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                AlertDialog.Builder builder_up  = new AlertDialog.Builder(MainActivity.this);
                builder_up.setTitle("发现新版本" ) ;
                String msm = "最新版本: "+versionname+"  当前版本："+version+"\n\n"+"版本更新日志：\n  "+message;
                builder_up.setMessage(msm) ;
                builder_up.setPositiveButton("马上体验" ,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(nurl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                builder_up.setNegativeButton("下次启动再提醒",null);
                builder_up.show();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Cache_manger();
    }
}
