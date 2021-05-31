package com.innowireless.xcal.mobile4g;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FeedDetailActivity extends AppCompatActivity {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="data";
    private RequestQueue queue;

    SQLiteHelper mSQLiteHelper;
    ArrayList<HashMap<String, String>> mUserInfoList;
    private String _userid;
    private String _username;
    private String _accesstoken;
    private String _datetiem;

    ArrayList<HashMap<String, String>> mArrayList;


    Button goInstagram, imageDL, imageSH, nextbtn, undobtn;
    TextView tvfeedcaption, tvfeedusername;
    ImageView imageViewfeed, imageViewcontrol;
    VideoView videoViewfeed;

    private String media_url;
    private String media_type;
    private String permalink;
    private String timestamp;
    private String username;
    private String id;
    private String thumbnail_url;
    private String caption;
    private String access_token;

    private Bitmap bmp;

    private int album_position = 0;

    ViewPager2 viewPager2;
    ArrayList<DataPage> list;


    private int video_set = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initView();

        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        _accesstoken = intent.getStringExtra("_accesstoken");

        Log.d("aaaa", "_accesstoken - "+_accesstoken);

        media_url = hashMap.get("media_url");
        media_type = hashMap.get("media_type");
        permalink = hashMap.get("permalink");
        timestamp = hashMap.get("timestamp");
        username = hashMap.get("username");
        id = hashMap.get("id");
        thumbnail_url = hashMap.get("thumbnail_url");
        caption = hashMap.get("caption");
        access_token = hashMap.get("access_token");



//        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
////        mArrayList = mSQLiteHelper.selectLoginDatas("dev.ksw94");
//        mUserInfoList = mSQLiteHelper.selectLoginDatas(username);
//        Log.d("main2","mArrayList - " + mUserInfoList);
//
//        HashMap<String, String> map  = mUserInfoList.get(0);
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_USERID));
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_USERNAME));
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_ACCESSTOKEN));
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_DATETIME));
//
//        _userid = map.get(SQLiteHelper.COLUMN_USERID);
//        _username = map.get(SQLiteHelper.COLUMN_USERNAME);
//        _accesstoken = map.get(SQLiteHelper.COLUMN_ACCESSTOKEN);
//        _datetiem = map.get(SQLiteHelper.COLUMN_DATETIME);

        Log.d("FeedDetailActivity", "media_type - "+media_type);

        if(media_type.equals("CAROUSEL_ALBUM")) {
//            https://graph.instagram.com/17884146782273567/children?access_token=IGQVJYR1RGOEdEMzd0Q0N0bXVEWHlhQVBsVWEyX2t1b2Y2VzlWT0Jvak0zUTExWi1oRnk5TTZA2MElNSUZAZAZAF9YczNBWmsyY3JzQVVzdEZAvOS1wUGExNmJxcWl5S0toNnhHT3VDUVVrbmhld191bFI0MAZDZD
//            https://graph.instagram.com/17906843686768109?fields=id,media_type,media_url,username,timestamp&access_token=IGQVJYR1RGOEdEMzd0Q0N0bXVEWHlhQVBsVWEyX2t1b2Y2VzlWT0Jvak0zUTExWi1oRnk5TTZA2MElNSUZAZAZAF9YczNBWmsyY3JzQVVzdEZAvOS1wUGExNmJxcWl5S0toNnhHT3VDUVVrbmhld191bFI0MAZDZD...
//            getAlbumAPI(id);
//            list = new ArrayList<>();
            carousel_album_API(_accesstoken, id);
            Log.d("FeedDetailActivity", "onCreate - "+mArrayList);
            tvfeedcaption.setText(caption);
            tvfeedusername.setText(username);
//            viewPager2.setAdapter(new ViewPagerAdapter(list));
//            imageViewfeed.setVisibility(View.VISIBLE);
//            new DownloadFilesTask().execute(media_url);
        }
        else if(media_type.equals("IMAGE")){
            tvfeedcaption.setText(caption);
            tvfeedusername.setText(username);
            imageViewfeed.setVisibility(View.VISIBLE);
            Glide.with(this).load(media_url).into(imageViewfeed);
//            new DownloadFilesTask().execute(media_url);
        }
        else if(media_type.equals("VIDEO")){
//            imageDL.setText("");

            tvfeedcaption.setText(caption);
            tvfeedusername.setText(username);


//            imageViewfeed.setVisibility(View.GONE);
//            new DownloadFilesTask().execute(thumbnail_url);
//            imageViewcontrol.setVisibility(View.GONE);

            videoViewfeed.setVisibility(View.VISIBLE);
            Uri vidurl = Uri.parse(media_url);
            videoViewfeed.setVideoURI(vidurl);
            videoViewfeed.start();
            videoViewfeed.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoViewfeed.start();
                }
            });


//            videoViewfeed.setVisibility(View.INVISIBLE);
//            Uri vidurl = Uri.parse(media_url);
//            videoViewfeed.setVideoURI(vidurl);
//            videoViewfeed.pause();

//            new DownloadFilesTask().execute(media_url);
        }

//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(media_url));
//        String title = id;
//        request.setTitle(title);
//        request.setDescription("Downloading File please wait....");
//
//        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//        downloadManager.enqueue(request);
//
//        Toast.makeText(FeedDetailActivity.this,"Downloading Started",Toast.LENGTH_SHORT).show();


    }


    public void initView(){
        tvfeedcaption = (TextView) findViewById(R.id.tvfeedcaption);
        tvfeedusername = (TextView) findViewById(R.id.tvfeedusername);
        imageViewfeed = (ImageView) findViewById(R.id.imageViewfeed);
        imageViewcontrol = (ImageView) findViewById(R.id.imageViewcontrol);
        videoViewfeed = (VideoView) findViewById(R.id.videoViewfeed);
        imageViewfeed.setVisibility(View.INVISIBLE);
        videoViewfeed.setVisibility(View.INVISIBLE);

        goInstagram = (Button) findViewById(R.id.goInstagram);
        imageDL = (Button) findViewById(R.id.imageDL);
        imageSH = (Button) findViewById(R.id.imageSH);

        nextbtn = (Button) findViewById(R.id.nextbtn);
        undobtn = (Button) findViewById(R.id.undobtn);

        viewPager2 = findViewById(R.id.viewPager2);
//        btnToggle = findViewById(R.id.btnToggle);

        videoViewfeed.setVisibility(View.GONE);
        imageViewfeed.setVisibility(View.GONE);
        imageViewcontrol.setVisibility(View.GONE);

        mArrayList = new ArrayList<>();
    }

    public void MyOnClick(View view)
    {
        switch (view.getId()) {
            case R.id.goInstagram:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(permalink));
                startActivity(browserIntent);
                break;
            case R.id.imageDL:
                if(media_type.equals("IMAGE")) {
//                    saveBitmapToJpeg(bmp, id);    // 내부 저장소에 저장
                    download_file(media_url, media_type);
                }
                else if(media_type.equals("VIDEO")){
//                    download();
                    download_file(media_url, media_type);
                }
                else {
                    for (int i = 0; i < mArrayList.size(); i++) {
                        String _md_ty = mArrayList.get(i).get("media_type");
                        String _md_url = mArrayList.get(i).get("DL_url");

                        download_file(_md_url, _md_ty);

//                        Log.d("usermanager", "username - " + mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME));
//
//                        String username = mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME);
//                        String datetime = mArrayList.get(i).get(SQLiteHelper.COLUMN_DATETIME);
//                        String userid = mArrayList.get(i).get(SQLiteHelper.COLUMN_USERID);
//                        String accesstoken = mArrayList.get(i).get(SQLiteHelper.COLUMN_ACCESSTOKEN);
                    }
                }
                break;
            case R.id.imageSH:

                break;
            case R.id.imageViewfeed:
//                if(media_type.equals("VIDEO")){
//                    imageViewfeed.setVisibility(View.INVISIBLE);
//                    videoViewfeed.setVisibility(View.VISIBLE);
//                    Uri vidurl = Uri.parse(media_url);
//                    videoViewfeed.setVideoURI(vidurl);
////                    videoViewfeed.pause();
//                    videoViewfeed.start();
//                }
                break;
            case R.id.imageViewcontrol:
                if(media_type.equals("VIDEO")){
                    imageViewfeed.setVisibility(View.INVISIBLE);
                    imageViewcontrol.setVisibility(View.INVISIBLE);
                    videoViewfeed.setVisibility(View.VISIBLE);
                    Uri vidurl = Uri.parse(media_url);
                    videoViewfeed.setVideoURI(vidurl);
//                    videoViewfeed.pause();
                    videoViewfeed.start();
                    video_set = 1;
                }
                break;
            case R.id.videoViewfeed:
                if(media_type.equals("VIDEO")){
////                    imageViewcontrol.setImageResource(R.drawable.ic_pause);
////                    imageViewcontrol.setVisibility(View.VISIBLE);
//                    videoViewfeed.setVisibility(View.VISIBLE);
                    Uri vidurl = Uri.parse(media_url);
                    videoViewfeed.setVideoURI(vidurl);
////                    videoViewfeed.pause();

                    if (video_set == 0){
                        videoViewfeed.setVisibility(View.VISIBLE);
                        videoViewfeed.pause();
                        video_set = 1;
                    }
                    else if (video_set == 1){
                        videoViewfeed.setVisibility(View.VISIBLE);
                        videoViewfeed.resume();
                        video_set = 0;
                    }
                    if(videoViewfeed.isPlaying()){
                        videoViewfeed.start();
                    }
                }
                break;
            case R.id.nextbtn:

//                if(media_type.equals("VIDEO")){
//                    imageViewcontrol.setImageResource(R.drawable.ic_pause);
//                    imageViewcontrol.setVisibility(View.VISIBLE);
//                    videoViewfeed.setVisibility(View.VISIBLE);
////                    Uri vidurl = Uri.parse(media_url);
////                    videoViewfeed.setVideoURI(vidurl);
////                    videoViewfeed.pause();
//                    videoViewfeed.pause();
//                }
                break;

            case R.id.undobtn:
//                if(media_type.equals("VIDEO")){
//                    imageViewcontrol.setImageResource(R.drawable.ic_pause);
//                    imageViewcontrol.setVisibility(View.VISIBLE);
//                    videoViewfeed.setVisibility(View.VISIBLE);
////                    Uri vidurl = Uri.parse(media_url);
////                    videoViewfeed.setVideoURI(vidurl);
////                    videoViewfeed.pause();
//                    videoViewfeed.pause();
//                }
                break;
        }
    }

    private void download_file(String Murl, String Mtype){
        Log.d("check","download_file - click");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Murl));
        String title = id;
        if(Mtype.equals("VIDEO")){
            title = id+".mp4";
        }
        else if(Mtype.equals("IMAGE")){
            title = id+".png";
        }
        request.setTitle(title);
        request.setDescription("Downloading File please wait....");

        String cookie = CookieManager.getInstance().getCookie(media_url);
        request.addRequestHeader("cookie",cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(FeedDetailActivity.this,"Downloading Started",Toast.LENGTH_SHORT).show();
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            imageViewfeed.setImageBitmap(result);
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap, String id_number) {   // 선택한 이미지 내부 저장소에 저장

        File tempFile = new File(getCacheDir(), id_number+".png");    // 파일 경로와 이름 넣기
//        File tempFile = new File(storeDir.getPath() + File.separator + id_number+".png");    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private File getSaveFolder() {
        String folderName = username;
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.pathSeparator + folderName);
        if(!dir.exists()){
            dir.mkdirs();
        } return dir;
    }



    private void download() {
        Downback DB = new Downback();
        DB.execute("");

    }


    private class Downback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String vidurl = media_url;
            downloadfile(vidurl);
            return null;

        }


    }

    private void downloadfile(String vidurl) {

        SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
        String date = sd.format(new Date());
        String name = "video" + date + ".mp4";

        try {
            String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "My_Video";
//            String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/";
            File rootFile = new File(rootDir);
//            File rootFile = new File(String.valueOf(getCacheDir()));    // 파일 경로와 이름 넣기
            rootFile.mkdir();
            URL url = new URL(vidurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    name));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }
    }


    private void carousel_album_API(String access_token, String userid) {

//        list = new ArrayList<>();

        queue = Volley.newRequestQueue(this);
//        https://graph.instagram.com/17884146782273567/children?access_token=IGQVJYR1RGOEdEMzd0Q0N0bXVEWHlhQVBsVWEyX2t1b2Y2VzlWT0Jvak0zUTExWi1oRnk5TTZA2MElNSUZAZAZAF9YczNBWmsyY3JzQVVzdEZAvOS1wUGExNmJxcWl5S0toNnhHT3VDUVVrbmhld191bFI0MAZDZD
        String url = "https://graph.instagram.com/"+userid+"/children?access_token="+access_token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "response - "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    list = new ArrayList<>();

//                    for(int i=jsonArray.length()-1;0<=i;i--){
                    for(int i=0; i<jsonArray.length(); i++){


                        JSONObject item = jsonArray.getJSONObject(i);

                        String carousel_id = item.getString("id");

                        Log.d(TAG, "carousel_id "+carousel_id);

                        carousel_img_url(access_token, carousel_id);

//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("carousel_id", carousel_id);
//
//                        mArrayList.add(hashMap);
                    }

//                    showAB();

//                    viewPager2.setAdapter(new ViewPagerAdapter(list));

                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }
                return;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

    private void carousel_img_url(String access_token, String id) {



        queue = Volley.newRequestQueue(this);
//        https://graph.instagram.com/17906843686768109?fields=id,media_type,media_url,username,timestamp&access_token=IGQVJYR1RGOEdEMzd0Q0N0bXVEWHlhQVBsVWEyX2t1b2Y2VzlWT0Jvak0zUTExWi1oRnk5TTZA2MElNSUZAZAZAF9YczNBWmsyY3JzQVVzdEZAvOS1wUGExNmJxcWl5S0toNnhHT3VDUVVrbmhld191bFI0MAZDZD...
        String url = "https://graph.instagram.com/"+id+"?fields=thumbnail_url,id,media_type,media_url,username,timestamp&access_token="+access_token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "response - "+response);

                try {
//                    list = new ArrayList<>();
//                    list = new ArrayList<>();
//                    viewPager2 = findViewById(R.id.viewPager2);

                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    String _media_type = jsonObject.getString("media_type");
                    String _media_url = jsonObject.getString("media_url");
                    String _thumbnail_url = new String();
                    try {
                        _thumbnail_url = jsonObject.getString("thumbnail_url");
                    } catch (Exception e){
                        _thumbnail_url = "none";
                    }



                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("media_type", _media_type);
                    hashMap.put("media_url", _media_url);
                    hashMap.put("thumbnail_url", _thumbnail_url);
                    if(_media_type.equals("IMAGE")){
                        hashMap.put("DL_url", _media_url);
                    }
                    else if(_media_type.equals("VIDEO")){
                        hashMap.put("DL_url", _media_url);
                    }

                    if(_media_type.equals("IMAGE")){
                        Log.d("fdsf", "_media_type - "+_media_type);
                        Log.d("fdsf", "_media_url - "+_media_url);
                        hashMap.put("show_url", _media_url);
                        list.add(new DataPage(_media_url, _media_type));
                        Log.d("fdsf", "list - "+list);
                    }
                    else if(_media_type.equals("VIDEO")){
                        Log.d("fdsf", "_media_type - "+_media_type);
                        Log.d("fdsf", "_media_url - "+_media_url);
                        hashMap.put("show_url", _media_url);
                        list.add(new DataPage(_media_url, _media_type));
                        hashMap.put("show_url", _thumbnail_url);
                        Log.d("fdsf", "list - "+list.get(0));
//                        list.add(new DataPage(_thumbnail_url, media_type));
                    }

                    mArrayList.add(hashMap);


                    viewPager2.setAdapter(new ViewPagerAdapter(list));

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                    SearchMediaActivity.this.textView.setText(html);
////                    showsearchmedia(mArrayList);
//                            viewPager2.setVisibility(View.VISIBLE);
//                            viewPager2.setAdapter(new ViewPagerAdapter(list));
//                        }
//                    });

//                    jObject.getString("humidity"));
//
//                    for(int i=jsonArray.length()-1;0<=i;i--){
//
//                        JSONObject item = jsonArray.getJSONObject(i);
//
//                        String _media_type = jObject.getString("media_type");
//                        String _media_url = jObject.getString("media_url");
//                        String _thumbnail_url = null;
//                        try {
//                            _thumbnail_url = jObject.getString("thumbnail_url");
//                        } catch (Exception e){
//                            _thumbnail_url = "none";
//                        }
//
//
//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("media_type", _media_type);
//                        hashMap.put("media_url", _media_url);
//                        hashMap.put("thumbnail_url", _thumbnail_url);
//                        if(_media_type.equals("IMAGE")){
//                            hashMap.put("DL_url", _media_url);
//                        }
//                        else if(_media_type.equals("VIDEO")){
//                            hashMap.put("DL_url", _media_url);
//                        }
//
//                        if(_media_type.equals("IMAGE")){
//                            hashMap.put("show_url", _media_url);
//                        }
//                        else if(_media_type.equals("VIDEO")){
//                            hashMap.put("show_url", _thumbnail_url);
//                        }
//
//                        mArrayList.add(hashMap);
//                    }

                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }
                return;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    private void showAB(){
        viewPager2.setAdapter(new ViewPagerAdapter(list));
    }
}