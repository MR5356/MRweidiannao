package xyz.ruiwencloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dev_personActivity extends AppCompatActivity {
    private String[] msm = {null};
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_person);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler=new Handler();

        Button submit = (Button)findViewById(R.id.dev_singin);
        final EditText user = (EditText)findViewById(R.id.dev_user);
        final EditText mail = (EditText)findViewById(R.id.dev_mail);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user.getText().toString();
                String email= mail.getText().toString();
                if (name==null||name.isEmpty()||email==null||email.isEmpty()){
                    Toast.makeText(Dev_personActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    user.setText("");
                    mail.setText("");
                    dev_submit(name, email);
                }
                //Toast.makeText(Dev_personActivity.this,"申请已经提交，大约需要1-3天时间审核时间，请留意邮件通知",Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void dev_submit(final String name, final String mail){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String imei = Tools.getIMEI(Dev_personActivity.this);
                    msm[0] = Tools.Get("http://ruiwencloud.xyz/app/api/dev_submit.php?name="+name+"&mail="+mail+"&imei="+imei,Dev_personActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                    msm[0] = "网络不稳定，提交失败";
                }
                handler.post(runnableUi);
            }
        }).start();
    }
    Runnable runnableUi=new Runnable(){
        @Override
        public void run() {
            //Toast.makeText(Dev_personActivity.this,msm[0],Toast.LENGTH_LONG).show();
            close_window();
        }
    };
    private void close_window(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dev_personActivity.this);
        builder.setTitle("");
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setMessage(msm[0]);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //builder.setNegativeButton("取消", null);
        builder.show();
    }
}
