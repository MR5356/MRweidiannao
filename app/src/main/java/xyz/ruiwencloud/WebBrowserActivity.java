package xyz.ruiwencloud;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;

import static xyz.ruiwencloud.Tools.Get;

public class WebBrowserActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar pg1;
    private WebChromeClient.CustomViewCallback mCallBack;
    private FrameLayout mVideoContainer;
    private Button search_btn;
    private EditText search_input;

    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        search_btn = (Button)findViewById(R.id.webview_search);
        search_btn.setBackgroundColor(0);
        search_input = (EditText)findViewById(R.id.webview_input);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                String msm;
                String url = search_input.getText().toString();
                if(url.contains("http")){
                    open_url(url);
                }else{
                    open_url("https://cn.bing.com/search?q="+url);
                }
            }
        });
        open_url("http://bing.com");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null){
            webView.destroy();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetJavaScriptEnabled")
    private void open_url(String msm_url){
        webView =  findViewById(R.id.webView);
        mVideoContainer = findViewById(R.id.videoContainer2);
        mVideoContainer.setVisibility(View.INVISIBLE);
        pg1 = findViewById(R.id.progressBar1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView.getSettings(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setDomStorageEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(webView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//允许使用缓存
        webView.setDownloadListener(new DownloadListener(){
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Log.e("HEHE","开始下载");
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        class MyWebChromeClient extends WebChromeClient {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    pg1.setVisibility(View.GONE);
                } else {
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }
            }
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                fullScreen();
                webView.setVisibility(View.GONE);
                mVideoContainer.setVisibility(View.VISIBLE);
                mVideoContainer.addView(view);
                mCallBack=callback;
                super.onShowCustomView(view, callback);
            }
            @Override
            public void onHideCustomView() {
                fullScreen();
                if (mCallBack!=null){
                    mCallBack.onCustomViewHidden();
                }
                webView.setVisibility(View.VISIBLE);
                mVideoContainer.removeAllViews();
                mVideoContainer.setVisibility(View.GONE);
                super.onHideCustomView();
            }
        }

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if(url.startsWith("weixin://") //微信
                            || url.startsWith("alipays://") //支付宝
                            || url.startsWith("mailto://") //邮件
                            || url.startsWith("tel://")//电话
                            || url.startsWith("dianping://")//大众点评
                        //其他自定义的scheme
                    ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }
                catch (Exception e){
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webView.loadUrl(msm_url);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage("主子要走了么？");

                //设置确定按钮
                builder.setNegativeButton("溜了溜了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                //设置取消按钮
                builder.setPositiveButton("容我再想想",null);
                //显示提示框
                builder.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
