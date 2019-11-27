package xyz.ruiwencloud;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;

public class Nine_pic_freeActivity extends AppCompatActivity {
    private Button button;
    private Button button2;
    private ImageView imageView;
    private Uri uritempFile;
    private LinearLayout crop_layout;
    private EditText xcut_pic;
    private EditText ycut_pic;
    private static final int IMAGE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;
    private Bitmap bitmap = null;
    private int FLAG_PIC = 0;
    private Toolbar toolbar;
    private Integer style_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_pic_free);
        toolbar = (Toolbar) findViewById(R.id.toolbar5);
        toolbar.setTitle("自由切图");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        style_color = intent.getIntExtra("color",0);
        Change_color(style_color);

        crop_layout = (LinearLayout)findViewById(R.id.crop_layout);
        crop_layout.setVisibility(View.INVISIBLE);
        xcut_pic = (EditText)findViewById(R.id.xcut_pic);
        ycut_pic = (EditText)findViewById(R.id.ycut_pic);
        button = (Button)findViewById(R.id.button);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        button2=(Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FLAG_PIC == 0 ){
                    Toast.makeText(getApplicationContext(),"未选择照片",Toast.LENGTH_SHORT).show();
                }else{
                    one2nine();
                }
            }
        });
        imageView = (ImageView) findViewById(R.id.image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        switch (requestCode){
            case IMAGE:
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex); //获取照片路径
                        cursor.close();
                        bitmap = BitmapFactory.decodeFile(path);
                        crop_layout.setVisibility(View.VISIBLE);
                        showImage();
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
                //resizeImage(data.getData());
            case RESIZE_REQUEST_CODE:
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    showImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪的大小
        intent.putExtra("outputX", 1080);
        intent.putExtra("outputY", 1080);
        intent.putExtra("return-data", true);
        //设置返回码
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }
    private void showImage(){
        FLAG_PIC = 1;
        imageView.setImageBitmap(bitmap);
    }
    private void one2nine(){
        int xpiece = 3;
        int ypiece = 3;
        if(xcut_pic.getText().toString()==null||xcut_pic.getText().toString().isEmpty()||ycut_pic.getText().toString()==null||ycut_pic.getText().toString().isEmpty()){
            xpiece = 3;
            ypiece = 3;
        }else{
            xpiece = Integer.parseInt(xcut_pic.getText().toString());
            ypiece = Integer.parseInt(ycut_pic.getText().toString());
        }
        picture.split(getApplicationContext(),bitmap,xpiece,ypiece);
        Toast.makeText(getApplicationContext(),"图片已经切割完成，保存在了相册中\"MRweidiannao\"文件夹中",Toast.LENGTH_SHORT).show();
    }
    private void one2more(){
        int xpiece = 3;
        int ypiece = 3;
        if(xcut_pic.getText().toString().isEmpty()||ycut_pic.getText().toString().isEmpty()){
            xpiece = 3;
            ypiece = 3;
        }else{
            xpiece = Integer.parseInt(xcut_pic.getText().toString());
            ypiece = Integer.parseInt(ycut_pic.getText().toString());
        }
        picture.split(getApplicationContext(),bitmap,xpiece,ypiece);
        Toast.makeText(getApplicationContext(),"图片已经切割完成，保存在了相册中\"MRweidiannao\"文件夹中",Toast.LENGTH_SHORT).show();
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
}
