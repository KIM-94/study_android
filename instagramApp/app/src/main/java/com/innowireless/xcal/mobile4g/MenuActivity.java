package com.innowireless.xcal.mobile4g;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
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

public class MenuActivity extends AppCompatActivity {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="data";
    private RequestQueue queue;


    private BackPressCloseHandler backPressCloseHandler;

    ImageView iv_myfeedbtn, iv_usersettingbtn, iv_downbtn;

    private ArrayList<HashMap<String, String>> mArrayList;
    private HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        backPressCloseHandler = new BackPressCloseHandler(this);

        Intent intent = getIntent();
        map = (HashMap<String, String>)intent.getSerializableExtra("map");

        iv_myfeedbtn = (ImageView) findViewById(R.id.iv_myfeedbtn);
        iv_downbtn = (ImageView) findViewById(R.id.iv_downbtn);
        iv_usersettingbtn = (ImageView) findViewById(R.id.iv_usersettingbtn);

//        feedAPI(map.get(SQLiteHelper.COLUMN_ACCESSTOKEN), map.get(SQLiteHelper.COLUMN_USERID), map.get(SQLiteHelper.COLUMN_DATETIME));

        iv_myfeedbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                feedAPI(map.get(SQLiteHelper.COLUMN_ACCESSTOKEN), map.get(SQLiteHelper.COLUMN_USERID), map.get(SQLiteHelper.COLUMN_DATETIME));

//                Intent intent = new Intent(MenuActivity.this, LisettestActivity.class);
//                intent.putExtra("map", map);
//                intent.putExtra("map2", mArrayList);
////                finish();
//                startActivity(intent);
            }
        });

        iv_downbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, GetSharelinkActivity.class);
//                finish();
                startActivity(intent);
            }
        });

        iv_usersettingbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, UserManagerActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


    public void feedAPI(String access_token, String userid, String datetime) {

        mArrayList = new ArrayList<>();

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

                    MyAdapter mMyAdapter = new MyAdapter();

//                    for(int i=jsonArray.length()-1;0<=i;i--){
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i);

                        String media_url = item.getString("media_url");
                        String media_type = item.getString("media_type");
                        String permalink = item.getString("permalink");
                        String timestamp = item.getString("timestamp");
                        String username = item.getString("username");
                        String id = item.getString("id");

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

                        if (media_type.equals("VIDEO")){
                            mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
                        }
                        else {
                            mMyAdapter.addItem(media_url, caption, username, media_type);
                        }


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
//                        hashMap.put("access_token", _accesstoken);

                        mArrayList.add(hashMap);
//
                        SQLiteHelper mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
                        mSQLiteHelper.insertFeedDatas(caption, media_url, media_type, thumbnail_url, permalink, timestamp, username, userid, id, datetime);
                    }

                    Intent intent = new Intent(MenuActivity.this, LisettestActivity.class);
                    intent.putExtra("map", map);
                    intent.putExtra("map2", mArrayList);
//                finish();
                    startActivity(intent);

//                    String TAG_listSize = Integer.toString(jsonArray.length());
//                    listSize.setText(TAG_listSize + "건");
//
//                    mListView.setAdapter(mMyAdapter);
//                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            HashMap<String, String> map  = mArrayList.get(position+1);
////                            HashMap<String, String> map  = new HashMap<>();
//
//                            Intent intent = new Intent(ListTestActivity3.this, FeedDetailActivity.class);
//                            intent.putExtra("map", map);
//                            startActivity(intent);
//                        }
//                    });


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
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
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