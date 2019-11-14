package xyz.ruiwencloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History_moneyActivity extends AppCompatActivity {
    private Handler handler;
    private String[] msm = {null};
    private ListView listView;
    private List<String> history_p = new ArrayList<>();
    private  ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_money);
        listView = (ListView)findViewById(R.id.history_money);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler=new Handler();
        Intent intent = getIntent();
        String result = intent.getStringExtra("url");
        pd = ProgressDialog.show(History_moneyActivity.this, "Load", "Loading…");
        GetHistory(result);
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
                    history_p.add(jsonline.getString(i));
                    ArrayAdapter<String> adapter1=new ArrayAdapter<String>(History_moneyActivity.this,android.R.layout.simple_list_item_1,history_p);
                    listView.setAdapter(adapter1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
