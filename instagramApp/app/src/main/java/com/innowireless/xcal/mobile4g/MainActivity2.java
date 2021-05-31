package com.innowireless.xcal.mobile4g;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="data";
    private RequestQueue queue;

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mListViewList;

    private TextView listSize, usernameTV;
    private ImageView userprofilIV;
    Button btn, syncbtn;
    private WebView webview;
    private WebSettings mWebSettings; //웹뷰세팅

    private String accessToken;
    private String datetime;

    SQLiteHelper mSQLiteHelper;

    private String USERID = "";
    private String ACCESSTOKEN = "";

    private String _userid;
    private String _username;
    private String _accesstoken;
    private String _datetiem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        this.initView();

        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");


        _userid = hashMap.get(SQLiteHelper.COLUMN_USERID);
        _username = hashMap.get(SQLiteHelper.COLUMN_USERNAME);
        _accesstoken = hashMap.get(SQLiteHelper.COLUMN_ACCESSTOKEN);
        _datetiem = hashMap.get(SQLiteHelper.COLUMN_DATETIME);


//        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
//        mArrayList = mSQLiteHelper.selectLoginDatas("dev.ksw94");
//        HashMap<String, String> map  = mArrayList.get(0);
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_USERID));
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_USERNAME));
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_ACCESSTOKEN));
//        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_DATETIME));
//
//        _userid = map.get(SQLiteHelper.COLUMN_USERID);
//        _username = map.get(SQLiteHelper.COLUMN_USERNAME);
//        _accesstoken = map.get(SQLiteHelper.COLUMN_ACCESSTOKEN);
//        _datetiem = map.get(SQLiteHelper.COLUMN_DATETIME);

        mListViewList = (ListView) findViewById(R.id.listView_main_list_search);
        mArrayList = new ArrayList<>();

        listSize = (TextView)findViewById(R.id.textItemsize);
        usernameTV = (TextView)findViewById(R.id.usernameTV);
        userprofilIV = (ImageView)findViewById(R.id.userprofilIV);

        usernameTV.setText(_username);

        feedAPI(_accesstoken, _userid, _datetiem);

        syncbtn = (Button) findViewById(R.id.syncbtn);
        syncbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mArrayList = new ArrayList<>();
                feedAPI(_accesstoken, _userid, _datetiem);
            }
        });

    }

    private void feedAPI(String access_token, String userid, String datetime) {

        queue = Volley.newRequestQueue(this);

        String fields = "caption,media_url,media_type,permalink,timestamp,username,thumbnail_url";
        String url = "https://graph.instagram.com/me/media?fields="+fields+"&access_token="+access_token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "response - "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    for(int i=jsonArray.length()-1;0<=i;i--){

                        JSONObject item = jsonArray.getJSONObject(i);

                        String caption = null;
                        try{
                            caption = item.getString("caption");
                        } catch (Exception e){
                            caption = "none";
                        }
                        String thumbnail_url = null;
                        try{
                            thumbnail_url = item.getString("thumbnail_url");
                        } catch (Exception e){
                            thumbnail_url = "none";
                        }
                        String media_url = item.getString("media_url");
                        String media_type = item.getString("media_type");
                        String permalink = item.getString("permalink");
                        String timestamp = item.getString("timestamp");
                        String username = item.getString("username");
                        String id = item.getString("id");

                        Log.d(TAG, "caption "+caption);
                        Log.d(TAG, "thumbnail_url "+thumbnail_url);
                        Log.d(TAG, "media_url "+media_url);
                        Log.d(TAG, "media_type "+media_type);
                        Log.d(TAG, "permalink "+permalink);
                        Log.d(TAG, "timestamp "+timestamp);
                        Log.d(TAG, "username "+username);
                        Log.d(TAG, "id "+id);


                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("caption", caption);
                        hashMap.put("media_url", media_url);
                        hashMap.put("media_type", media_type);
                        hashMap.put("permalink", permalink);
                        hashMap.put("timestamp", timestamp);
                        hashMap.put("username", username);
                        hashMap.put("id", id);
                        hashMap.put("thumbnail_url", thumbnail_url);
                        hashMap.put("access_token", _accesstoken);

                        mArrayList.add(hashMap);

                        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
                        mSQLiteHelper.insertFeedDatas(caption, media_url, media_type, thumbnail_url, permalink, timestamp, username, userid, id, datetime);
                    }

                    String TAG_listSize = Integer.toString(jsonArray.length());
                    listSize.setText(TAG_listSize + "건");

                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity2.this, mArrayList, R.layout.item_list3,
                            new String[]{"timestamp", "caption", "media_type", "permalink", "username"},
                            new int[]{R.id.textViewtimestamp, R.id.tvcaption, R.id.tvmedia_type, R.id.tvpermalink, R.id.tvusername}
                    );

                    mListViewList.setAdapter(adapter);
                    mListViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            HashMap<String, String> map  = mArrayList.get(position);
                            Intent intent = new Intent(MainActivity2.this, FeedDetailActivity.class);
                            intent.putExtra("map", map);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }

                return;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", "onErrorResponse - "+error);
                Toast.makeText(getApplicationContext(),"세션이 종료 되었습니다. 로그인을 다시 진행하세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
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
}