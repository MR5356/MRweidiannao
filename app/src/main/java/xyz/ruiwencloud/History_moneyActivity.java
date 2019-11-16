package xyz.ruiwencloud;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class History_moneyActivity extends AppCompatActivity {
    private Handler handler;
    private String[] msm = {null};
    private ListView listView;
    private List<String> history_p = new ArrayList<>();
    private  ProgressDialog pd;
    private Toolbar toolbar;
    private Integer style_color;
    private List<His_money> money_history2 = new ArrayList<His_money>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_money);
        listView = (ListView)findViewById(R.id.history_money);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("商品历史价格");
        setSupportActionBar(toolbar);

        handler=new Handler();
        Intent intent = getIntent();
        String result = intent.getStringExtra("url");
        pd = ProgressDialog.show(History_moneyActivity.this, "Load", "Loading…");
        GetHistory(result);

        style_color = intent.getIntExtra("color",0);
        Change_color(style_color);
    }
    private void GetHistory(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    msm[0] = Tools.Get(url,getApplicationContext());
                    Log.d("aaaaaaaa",url);
                }catch (Exception e){
                    e.printStackTrace();
                    msm[0] = "nonet";
                }
                handler.post(runnableUi);
            }
        }).start();
    }
    Runnable runnableUi=new Runnable(){
        @Override
        public void run() {
            pd.dismiss();
            EndDo();
        }
    };

    private void EndDo(){
        if(msm[0].equals("nonet")){
            //Toast.makeText(History_moneyActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(History_moneyActivity.this);
            builder.setTitle("");
            //builder.setIcon(R.drawable.ic_launcher);
            builder.setMessage("网络连接失败");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //builder.setNegativeButton("取消", null);
            builder.show();
        }else if(msm[0].equals("error")){
            //Toast.makeText(History_moneyActivity.this,"商品未收录",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(History_moneyActivity.this);
            builder.setTitle("");
            //builder.setIcon(R.drawable.ic_launcher);
            builder.setMessage("商品未收录");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //builder.setNegativeButton("取消", null);
            builder.show();
        }else{
            try {
                JSONArray jsonline = new JSONArray(msm[0]);
                for (int i = 0; i<jsonline.length();i++){
                    Log.d("jsonline",jsonline.getString(i));
                    //history_p.add(jsonline.getString(i));
                    //ArrayAdapter<String> adapter1=new ArrayAdapter<String>(History_moneyActivity.this,android.R.layout.simple_list_item_1,history_p);
                    //listView.setAdapter(adapter1);
                    JSONArray datamoney = new JSONArray(jsonline.getString(i));
                    His_money hismoney = new His_money(datamoney.getString(0),datamoney.getString(1));
                    Log.d("hismoney",datamoney.getString(0)+datamoney.getString(1));
                    money_history2.add(hismoney);
                }
                MoneyAdapter adapter = new MoneyAdapter(History_moneyActivity.this,R.layout.money_his,money_history2);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
    public class His_money{
        private String data;
        private String money;

        public His_money(String data, String money){
            this.data = data;
            this.money = money;
        }

        public String getData(){
            return data;
        }

        public String getMoney(){
            return money;
        }
    }
    public class MoneyAdapter extends ArrayAdapter<His_money>{
        private int resourceId;

        public MoneyAdapter(Context context, int textViewResourceId, List<His_money> objects){
            super(context,textViewResourceId,objects);
            resourceId = textViewResourceId;
        }

        @NotNull
        @Override
        public View getView(int position, View convertView, @NotNull ViewGroup parent) {
            His_money his_money = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            TextView data = (TextView)view.findViewById(R.id.money_data);
            TextView money = (TextView)view.findViewById(R.id.money_money);
            Log.d("hisdata",his_money.getData());
            Log.d("hismoney",his_money.getMoney());
            data.setText(his_money.getData());
            money.setText(his_money.getMoney());
            return view;
        }
    }
}
