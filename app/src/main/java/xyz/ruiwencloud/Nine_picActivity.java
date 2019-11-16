package xyz.ruiwencloud;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;

public class Nine_picActivity extends AppCompatActivity {
    private Button button;
    private Button button2;
    private ImageView imageView;
    private Uri uritempFile;
    private static final int IMAGE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;
    private Bitmap bitmap = null;
    private int FLAG_PIC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_pic);

        button = (Button)findViewById(R.id.button);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        button2=(Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                resizeImage(data.getData());
            case RESIZE_REQUEST_CODE:
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    showImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MRweidiannao";
        picture.split(getApplicationContext(),bitmap,3,3);
        Toast.makeText(getApplicationContext(),"图片已经切割完成，保存在了相册中\"MRweidiannao\"文件夹中",Toast.LENGTH_SHORT).show();
    }
}
