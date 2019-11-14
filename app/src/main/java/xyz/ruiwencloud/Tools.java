package xyz.ruiwencloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tools {
    private static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public static String Get(String baseurl,Context context) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(baseurl).cacheControl(new CacheControl.Builder().noCache().build()).removeHeader("User-Agent").addHeader("User-Agent",
                getUserAgent(context)).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public  static String Post(String baseurl,String verbose,String values,Context context) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add(verbose,values).build();
        Request request = new Request.Builder().url(baseurl).cacheControl(new CacheControl.Builder().noCache().build()).removeHeader("User-Agent").addHeader("User-Agent",
                getUserAgent(context)).post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            int versioncode = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "None";
    }
    public static Map<String,String> serverVersion(Context context){
        Map<String,String> results = new HashMap<>();
        try {
            String surl="http://www.ruiwencloud.xyz/app/";
            String result = Tools.Get(surl,context);
            JSONObject json = new JSONObject(result);
            String versionname = json.getString("versionname");
            String versioncode = json.getString("versioncode");
            String message = json.getString("message");
            String nurl = json.getString("url");
            results.put("versionname",versionname);
            results.put("versioncode",versioncode);
            results.put("message",message);
            results.put("nurl",nurl);
            return results;
        } catch (Exception e){
            e.printStackTrace();
        }
        results.put("nurl","None");
        return results;
    }
    public static String geturl(String url1){
        BufferedReader in = null;
        String result = null;
        try {
            String surl=url1;
            URL url = new URL(surl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(6 * 1000);
            conn.setReadTimeout(6 * 1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line=reader.readLine()) != null){
                sb.append(line);
            }
            result = sb.toString();
            return result;
        } catch (Exception e){
            e.printStackTrace();
        }
        result = null;
        return result;
    }
    @SuppressLint({"MissingPermission", "NewApi"})
    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = telephonyManager.getImei();
            }
            if (imei == null) {
                imei = telephonyManager.getMeid();
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknow";
        }

    }
}
