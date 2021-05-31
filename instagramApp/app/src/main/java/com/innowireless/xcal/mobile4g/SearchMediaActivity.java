package com.innowireless.xcal.mobile4g;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchMediaActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private ListView mListView;
    private MyAdapter mMyAdapter;
    WebView webView;
    String source;

    String copylink;
    private String USERN;

    ArrayList<HashMap<String, String>> mArrayList, mArrayList2;
    ArrayList<DataPage> list;
    ViewPager2 viewPager2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        TextView tvfeedcaption = (TextView) findViewById(R.id.tvfeedcaption);
        TextView tvfeedusername = (TextView) findViewById(R.id.tvfeedusername);
        tvfeedcaption.setVisibility(View.INVISIBLE);
        tvfeedusername.setVisibility(View.INVISIBLE);

        USERN = null;

        adapter = new CustomAdapter();
        mListView = (ListView) findViewById(R.id.listView_main_list_search);
//        mMyAdapter = new MyAdapter();

        mArrayList = new ArrayList<>();
        mMyAdapter = new MyAdapter();

        mArrayList2 = new ArrayList<>();

//        list = new ArrayList<>();
//        viewPager2 = findViewById(R.id.viewPager2);


        Intent intent1 = getIntent(); /*데이터 수신*/
        copylink = intent1.getExtras().getString("copylink");

        webView = findViewById(R.id.webView);



        //WebView 자바스크립트 활성화
        webView.getSettings().setJavaScriptEnabled(true);
        // 자바스크립트인터페이스 연결
        // 이걸 통해 자바스크립트 내에서 자바함수에 접근할 수 있음.
        webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        // 페이지가 모두 로드되었을 때, 작업 정의
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");

//                for(int i =0; i<10; i++){
////                    //         1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
////                    new Handler().postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");
////                        }
////                    },1000);
//                    view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");
//                }

//                for(int i =0; i<10; i++){
//                    view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");
//                    Log.d("handler","check_num - " + i);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (true) {
//                                handler.postDelayed(this,1000);
//                            }
//                        }
//                    },1000);
//                }

//                final int[] check_num = {1};
//                final int[] finish_num = {0};
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (finish_num[0] == 0) {
//                            view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");
//                            Log.d("handler","check_num - " + check_num[0]);
//                            handler.postDelayed(this,1000);
//                            if(check_num[0]>10){
//                                finish_num[0] = 1;
//                            }
//                            check_num[0]++;
//                        }
//                    }
//                },1000);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        for(int i =0; i<10; i++){
//                            view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");
//                        }
//                    }
//                },1000);


                for(int i=0; i<10; i++){
                    view.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");
                    super.onPageFinished(view, url);
                    // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
                    // 자바스크립트 기본 메소드로 html 소스를 통째로 지정해서 인자로 넘김
                    view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
                }
//                super.onPageFinished(view, url);
//                // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
//                // 자바스크립트 기본 메소드로 html 소스를 통째로 지정해서 인자로 넘김
//                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");

            }
        });

        //지정한 URL을 웹 뷰로 접근하기
        webView.loadUrl(copylink);


        Button imageDL = (Button) findViewById(R.id.imageDL);
        imageDL.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.d("SearchMediaActivity", "mArrayList size - "+mArrayList.size());
                for(int i=0; i<mArrayList.size(); i++){
                    if(!mArrayList2.contains(mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA))){
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("media_type", mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIATYPE));
                        hashMap.put("media_url", mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA));
                        hashMap.put("caption", mArrayList.get(i).get(SQLiteHelper.COLUMN_CAPTION));
                        hashMap.put("username", mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME));
                        mArrayList2.add(hashMap);
                    }
//                    Log.d("SearchMediaActivity", "mArrayList - "+mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA));
                }

                Log.d("SearchMediaActivity", "mArrayList2 size - "+mArrayList2.size());
                for(int i=0; i<mArrayList2.size(); i++){
//                    if(!mArrayList2.contains(mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA))){
//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("media_type", mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIATYPE));
//                        hashMap.put("media_url", mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA));
//                        hashMap.put("caption", mArrayList.get(i).get(SQLiteHelper.COLUMN_CAPTION));
//                        hashMap.put("username", mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME));
//                        mArrayList2.add(hashMap);
//                    }
                    Log.d("SearchMediaActivity", "mArrayList2 - "+mArrayList2.get(i).get(SQLiteHelper.COLUMN_MEDIA));
                }

                Log.d("SearchMediaActivity", "list size - "+list.size());
                for(int i=0; i<list.size(); i++){
//                    if(!mArrayList2.contains(mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA))){
//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("media_type", mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIATYPE));
//                        hashMap.put("media_url", mArrayList.get(i).get(SQLiteHelper.COLUMN_MEDIA));
//                        hashMap.put("caption", mArrayList.get(i).get(SQLiteHelper.COLUMN_CAPTION));
//                        hashMap.put("username", mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME));
//                        mArrayList2.add(hashMap);
//                    }
                    Log.d("SearchMediaActivity", "list - "+list.get(i).getmedia_url());
                    String media_url, media_type, username;
                    media_url = list.get(i).getmedia_url();
                    media_type = list.get(i).getmedia_type();
                    username = USERN;
                    download_file(media_url, media_type, username);
                }
            }
        });

        Button goInstagram = (Button) findViewById(R.id.goInstagram);
        goInstagram.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(copylink));
                startActivity(browserIntent);
            }
        });
    }

    public class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            Document doc = null;

//            webView.loadUrl("javascript:document.getElementsByClassName('    coreSpriteRightChevron  ')[0].click()");

            //위 자바스크립트가 호출되면 여기로 html이 반환됨
            source = html;
            doc = Jsoup.parse(source);
            Elements link = doc.select("div.KL4Bh img");
//            Elements video = doc.select("div._5wCQW video");
            Elements video = doc.select("div._5wCQW");
            Elements video_thm = doc.select("div._5wCQW img");

            list = new ArrayList<>();
            viewPager2 = findViewById(R.id.viewPager2);

//            if(link.size()>0){
//                String img = link.get(0).attr("src");
//                Log.d("MyJavascriptInterface", "img : "+img);
//
//                String media_type = "IMAGE";
//                String media_url = img;
//                String caption = "";
//                String username = "";
//
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("media_type", media_type);
//                hashMap.put("media_url", img);
//                hashMap.put("caption", caption);
//                hashMap.put("username", username);
//
//                mArrayList.add(hashMap);
//                list.add(new DataPage(media_url, media_type));
//            }


            for(int i = 0; i < link.size(); i++){
                String textt = link.get(i).attr("src");
//                Log.d("MyJavascriptInterface", "source - "+link.get(i).attr("src"));
                Log.d("MyJavascriptInterface", "img : "+textt);

                String media_type = "IMAGE";
                String media_url = textt;
                String caption = "";
                String username = "";

                USERN = username;

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("media_type", media_type);
                hashMap.put("media_url", textt);
                hashMap.put("caption", caption);
                hashMap.put("username", username);

                mArrayList.add(hashMap);
                list.add(new DataPage(media_url, media_type));

//                mMyAdapter.addItem(media_url, caption, username, media_type);
            }


//            if(video.size()>0){
//                String _thumbnail = video.select("img").get(0).attr("src");
//                String _video = video.select("video").get(0).attr("src");
//                Log.d("MyJavascriptInterface", "video : _thumbnail - "+_thumbnail);
//                Log.d("MyJavascriptInterface", "video : _video - "+_video);
//
//                String media_type = "VIDEO";
//                String media_url = _video;
//                String thumbnail_url = _thumbnail;
//                String caption = "";
//                String username = "";
//
//
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("media_type", media_type);
//                hashMap.put("media_url", media_url);
//                hashMap.put("thumbnail_url", thumbnail_url);
//                hashMap.put("caption", caption);
//                hashMap.put("username", username);
//
//                mArrayList.add(hashMap);
//                list.add(new DataPage(thumbnail_url, media_type));
//            }

            for(int i = 0; i < video.size(); i++){
                String _thumbnail = video.select("img").get(i).attr("src");
                String _video = video.select("video").get(i).attr("src");
                Log.d("MyJavascriptInterface", "video : _thumbnail - "+_thumbnail);
                Log.d("MyJavascriptInterface", "video : _video - "+_video);


                String media_type = "VIDEO";
                String media_url = _video;
                String thumbnail_url = _thumbnail;
                String caption = "";
                String username = "";


                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("media_type", media_type);
                hashMap.put("media_url", media_url);
                hashMap.put("thumbnail_url", thumbnail_url);
                hashMap.put("caption", caption);
                hashMap.put("username", username);

                mArrayList.add(hashMap);

//                list.add(new DataPage(thumbnail_url, media_type));
                list.add(new DataPage(media_url, media_type));

//                mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
            }

//            showsearchmedia(mArrayList);

//            for(int i = 0; i < video_thm.size(); i++){
//                String textt = video_thm.get(i).attr("src");
////                Log.d("MyJavascriptInterface", "source - "+link.get(i).attr("src"));
//                Log.d("MyJavascriptInterface", "source - "+textt);
//
//                String media_type = "VIDEO";
//                String thumbnail_url = textt;
//                String caption = "";
//                String username = "";
//
//
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("media_type", "VIDEO");
//                hashMap.put("thumbnail_url", textt);
//                hashMap.put("caption", caption);
//                hashMap.put("username", username);
//
//                mArrayList.add(hashMap);
//
////                mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
//            }

//            showsearchmedia(mArrayList);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    SearchMediaActivity.this.textView.setText(html);
//                    showsearchmedia(mArrayList);
                    viewPager2.setAdapter(new ViewPagerAdapter(list));
                }
            });

//            ArrayList<HashMap<String, String>> map  = mArrayList;
//            Intent intent = new Intent(SearchMediaActivity.this, ShowSearchMediaActivity.class);
//            intent.putExtra("map", map);
//            startActivity(intent);
//            finish();

//            new Handler().postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    //딜레이 후 시작할 코드 작성
//                    ArrayList<HashMap<String, String>> map  = mArrayList;
//                    Intent intent = new Intent(SearchMediaActivity.this, ShowSearchMediaActivity.class);
//                    intent.putExtra("map", map);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 1000);// 0.6초 정도 딜레이를 준 후 시작



//            temp = doc.select("img").select("[src]");
//            String temp2 = null;

//            byte []
//            source = Base64.encodeToString(html.getBytes(StandardCharsets.UTF_8), Base64.NO_PADDING);
//            myWebView.loadData(source, "text/html", "base64");
//            Log.d("MyJavascriptInterface", "source - "+doc.select("img").select("[src]").toString());
//            Log.d("MyJavascriptInterface", "source - "+doc.select("img").select("[src]").html().replaceAll("amp;", " "));
//            Log.d("MyJavascriptInterface", "source - "+temp);


        }
    }

    private void download_file(String Murl, String Mtype, String id){
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

        String cookie = CookieManager.getInstance().getCookie(Murl);
        request.addRequestHeader("cookie",cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(SearchMediaActivity.this,"Downloading Started - "+title, Toast.LENGTH_SHORT).show();
    }

//    public void showsearchmedia(ArrayList<HashMap<String, String>> map){
//        ArrayList<HashMap<String, String>> _mArrayList = new ArrayList<>();
//        _mArrayList = map;
//
//        for (int i=0; i<_mArrayList.size(); i++){
//            Log.d("ShowSearchMediaActivity", "mArrayList - "+_mArrayList.get(i).get("media_type"));
//
//            String media_type, thumbnail_url, caption, username, media_url;
//
//            media_type = _mArrayList.get(i).get("media_type");
//            media_url = _mArrayList.get(i).get("media_url");
//            thumbnail_url = _mArrayList.get(i).get("thumbnail_url");
//            caption = _mArrayList.get(i).get("caption");
//            username = _mArrayList.get(i).get("username");
//
//            if(media_type.equals("IMAGE")){
//                mMyAdapter.addItem(media_url, caption, username, media_type);
//            }
//            else if(media_type.equals("VIDEO")){
//                mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
//            }
//        }
//
//        mListView.setAdapter(mMyAdapter);
//
//    }
}