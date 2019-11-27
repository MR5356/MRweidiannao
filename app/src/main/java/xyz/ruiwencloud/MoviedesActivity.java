package xyz.ruiwencloud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviedesActivity extends AppCompatActivity {
    public List<String> online1 = new ArrayList<>();
    public List<String> download = new ArrayList<>();
    public List<String> online1_show = new ArrayList<>();
    public List<String> online2_show = new ArrayList<>();
    public List<String> download_show = new ArrayList<>();
    private String name="name1";
    private String vkey="vkey1";
    private Handler handler;
    private String[] msm = {null};
    private Toolbar toolbar;
    private Integer style_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedes);
        handler=new Handler();
        toolbar = (Toolbar) findViewById(R.id.toolbar7);
        toolbar.setTitle("视频详情");
        setSupportActionBar(toolbar);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        String result = intent.getStringExtra("des");
        style_color = intent.getIntExtra("color",0);
        Change_color(style_color);
        try {
            JSONObject jsonObject = new JSONObject(result);
            name = jsonObject.optString("name");
            String info = jsonObject.optString("info");
            String douban = jsonObject.optString("douban");
            String jpg = jsonObject.optString("jpg");
            String onlines = jsonObject.optString("online");
            String downloads = jsonObject.optString("download");
            JSONArray jsonline = new JSONArray(onlines);
            for (int i = 0; i<jsonline.length();i++){
                Log.d("jsonline",jsonline.getString(i));
                online1.add(jsonline.getString(i));
                int j = i+1;
                online1_show.add(name+"    第"+j+"集");
            }
            JSONArray jsdownload = new JSONArray(downloads);
            for (int i = 0; i<jsdownload.length();i++){
                Log.d("jsdownload",jsdownload.getString(i));
                download.add(jsdownload.getString(i));
                String temp[] = jsdownload.getString(i).split("/");
                String fileName = temp[temp.length-1];
                download_show.add(fileName);
                String fileName2 = fileName.replace(".mp4","");
                online2_show.add(fileName2);
            }

            ImageView imageView = (ImageView)findViewById(R.id.des_jpg);
            Glide.with(this).load(jpg).into(imageView);
            TextView textView_title = (TextView)findViewById(R.id.des_title);
            textView_title.setText(name);
            TextView textView_douban = (TextView)findViewById(R.id.des_douban);
            textView_douban.setText("豆瓣评分："+douban);
            TextView textView_info = (TextView)findViewById(R.id.des_info);
            textView_info.setText("影片简介："+info);

            //在线1
            ListView listView1 = (ListView) findViewById(R.id.des_list1);
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MoviedesActivity.this,android.R.layout.simple_list_item_1,online1_show);
            listView1.setAdapter(adapter1);
            setListViewHeightBasedOnChildren(listView1);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    onclick1(position);
                }
            });

            //在线2
            ListView listView2 = (ListView) findViewById(R.id.des_list2);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MoviedesActivity.this,android.R.layout.simple_list_item_1,online2_show);
            listView2.setAdapter(adapter2);
            setListViewHeightBasedOnChildren(listView2);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    onclick2(position);
                }
            });

            //下载
            ListView listView3 = (ListView) findViewById(R.id.des_list3);
            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(MoviedesActivity.this,android.R.layout.simple_list_item_1,download_show);
            listView3.setAdapter(adapter3);
            setListViewHeightBasedOnChildren(listView3);
            listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    onclick3(position);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Button delete_movie = (Button)findViewById(R.id.moviedes_delete);
        delete_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoviedesActivity.this);
                builder.setTitle("请再次确认");
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setMessage("确认此视频包含违规内容，执行删除操作");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vkey = Tools.getIMEI(MoviedesActivity.this);
                        movie_delete();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        //获取listview的适配器
        ListAdapter listAdapter = listView.getAdapter(); //item的高度
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight()+listView.getDividerHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }
    public void onclick1(int position){
        //Toast.makeText(MovieActivity.this,des.get(position),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(online1.get(position)));
        startActivity(intent);
    }
    public void onclick2(int position){
        //Toast.makeText(MovieActivity.this,des.get(position),Toast.LENGTH_SHORT).show();
        Intent openVideo = new Intent(Intent.ACTION_VIEW);
        openVideo.setDataAndType(Uri.parse(download.get(position)), "video/*");
        startActivity(openVideo);
    }
    public void onclick3(int position){
        //Toast.makeText(MovieActivity.this,des.get(position),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(download.get(position)));
        startActivity(intent);
    }
    public void movie_delete(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    msm[0] = Tools.Get("http://ruiwencloud.xyz/app/api/delete_movie.php?vkey="+vkey+"&name="+name,MoviedesActivity.this);
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
            Toast.makeText(MoviedesActivity.this,msm[0],Toast.LENGTH_SHORT).show();
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
