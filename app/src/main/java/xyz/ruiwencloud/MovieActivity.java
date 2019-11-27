package xyz.ruiwencloud;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {
    private List<video_display> Movielist = new ArrayList<>();
    public List<String> des1 = new ArrayList<>();
    public String movie_name1 = null;
    private Handler handler;
    private MovieAdapter adapter = null;
    private Toolbar toolbar;
    private Integer style_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler=new Handler();
        toolbar = (Toolbar) findViewById(R.id.toolbar6);
        toolbar.setTitle("视频搜索");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        style_color = intent.getIntExtra("color",0);
        Change_color(style_color);

        Movielist.clear();
        des1.clear();
        latest_movies latestMovies = new latest_movies();
        latestMovies.execute();

        SearchView searchView = (SearchView)findViewById(R.id.movie_search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movie_name1 = query;
                des1.clear();
                Movielist.clear();
                latest_movies latestMovies1 = new latest_movies();
                latestMovies1.execute();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //新建类作为视频显示
    public class video_display{
        private String title;
        private String imageid;

        public video_display(String title, String imageid){
            this.title = title;
            this.imageid = imageid;
        }
        public String getTitle(){
            return title;
        }
        public String getImageid(){
            return imageid;
        }
    }
    //视频显示适配器
    public class MovieAdapter extends ArrayAdapter<video_display> {
        private int resourceId;
        private Context context;

        public MovieAdapter(Context context, int textViewResourceId, List<video_display> objects){
            super(context,textViewResourceId,objects);
            resourceId = textViewResourceId;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            video_display videoDisplay = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            ImageView imageView = (ImageView)view.findViewById(R.id.movie_image);
            TextView textView = (TextView)view.findViewById(R.id.movie_title);
            String imageurl = videoDisplay.getImageid();
            Log.d("imageurl",imageurl);
            Glide.with(this.context).load(imageurl).into(imageView);
            textView.setText(videoDisplay.getTitle());
            return view;
        }
    }
    private void initMovie(String title, String url){
        video_display movie = new video_display(title,url);
        Movielist.add(movie);
    }
    public class latest_movies extends AsyncTask<String,Integer,String>{
        private String results;
        @Override
        protected String doInBackground(String... strings) {
            if (movie_name1==null){
                try {
                    results = Tools.Get("http://ruiwencloud.xyz/app/api/latest_movie.php",MovieActivity.this);
                } catch (IOException e) {
                    results = null;
                    e.printStackTrace();
                }
            }else{
                try {
                    results = Tools.Get("http://www.ruiwencloud.xyz/movie/app.php?name="+movie_name1,MovieActivity.this);
                } catch (IOException e) {
                    results = null;
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (results == null){
                handler.post(runnableUi);
            }else {
                try {
                    JSONArray jsonArray = new JSONArray(results);
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        des1.add(jsonObject.toString());
                        initMovie(jsonObject.optString("name"),jsonObject.optString("jpg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new MovieAdapter(MovieActivity.this,R.layout.movie_item,Movielist);
                GridView gridView = (GridView)findViewById(R.id.grid_movie);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onclick(position);
                    }
                });
            }
        }
    }
    Runnable runnableUi=new Runnable(){
        @Override
        public void run() {
            Toast.makeText(MovieActivity.this,"网络出故障了，检查一下吧",Toast.LENGTH_SHORT).show();
        }

    };
    private void onclick(int position){
        //Toast.makeText(MovieActivity.this,des.get(position),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MovieActivity.this, MoviedesActivity.class);
        intent.putExtra("des",des1.get(position));
        intent.putExtra("color",style_color);
        startActivity(intent);
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
