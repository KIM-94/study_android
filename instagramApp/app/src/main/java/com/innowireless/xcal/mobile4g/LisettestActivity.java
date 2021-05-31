package com.innowireless.xcal.mobile4g;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import java.util.List;

import static android.view.View.GONE;

public class LisettestActivity extends AppCompatActivity implements AbsListView.OnScrollListener  {
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="data";
    private RequestQueue queue;

    private ListView mListView;


    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private List<String> list;                      // String 데이터를 담고있는 리스트
    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 3;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    private int check = 0;
    private int finish_set = 0;
    private int finish_set_1 = 0;

    SQLiteHelper mSQLiteHelper;
    ArrayList<HashMap<String, String>> mArrayList, mUserInfoArraylist;
    //    MyAdapter mMyAdapter;
    MyFeedAdapter mMyAdapter;

    private String _userid;
    private String _username;
    private String _accesstoken;
    private String _datetiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        String userNAME = hashMap.get(SQLiteHelper.COLUMN_USERNAME);

        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
//        mArrayList = mSQLiteHelper.selectLoginDatas("dev.ksw94");
        mUserInfoArraylist = mSQLiteHelper.selectLoginDatas(userNAME);
        Log.d("main2","mArrayList - " + mUserInfoArraylist);

        HashMap<String, String> map  = mUserInfoArraylist.get(0);
        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_USERID));
        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_USERNAME));
        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_ACCESSTOKEN));
        Log.d("main2","MainActivity2 - " + map.get(SQLiteHelper.COLUMN_DATETIME));

        _userid = map.get(SQLiteHelper.COLUMN_USERID);
        _username = map.get(SQLiteHelper.COLUMN_USERNAME);
        _accesstoken = map.get(SQLiteHelper.COLUMN_ACCESSTOKEN);
        _datetiem = map.get(SQLiteHelper.COLUMN_DATETIME);

        TextView usernameTV = (TextView)findViewById(R.id.usernameTV);
        usernameTV.setText(_username);


        mArrayList = new ArrayList<>();
        ArrayList<HashMap<String, String>> hashMap2 = (ArrayList<HashMap<String, String>>)intent.getSerializableExtra("map2");
        mArrayList = hashMap2;
//        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
////        mArrayList = mSQLiteHelper.selectLoginDatas("dev.ksw94");
//        mArrayList = mSQLiteHelper.selectFeedDatas("dev.ksw94");

//        적용 코드
        mListView = (ListView) findViewById(R.id.listView_main_list_search);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mMyAdapter = new MyFeedAdapter(this, mArrayList);
        mListView.setAdapter(mMyAdapter);

        progressBar.setVisibility(GONE);
        mListView.setOnScrollListener(this);
        getItem();

        Button syncbtn = (Button) findViewById(R.id.syncbtn);
        syncbtn.setVisibility(GONE);
        syncbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                mArrayList = new ArrayList<>();
//                feedAPI(_accesstoken, _userid, _datetiem);
//                ListTestActivity3 _sync = new ListTestActivity3();
                feedAPI(_accesstoken, _userid, _datetiem);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


////        참고 코드
//        listView = (ListView) findViewById(R.id.listview);
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        list = new ArrayList<String>();
//        adapter = new ListViewAdapter(this, list);
//        listView.setAdapter(adapter);
//
//        progressBar.setVisibility(View.GONE);
//        listView.setOnScrollListener(this);
//        getItem();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true

//        totalItemCount = mArrayList.size();
//        visibleItemCount = check;
//        totalItemCount = check;

        Log.d("LisettestActivity", "totalItemCount - "+totalItemCount);
        Log.d("LisettestActivity", "visibleItemCount - "+visibleItemCount);
        Log.d("LisettestActivity", "firstVisibleItem - "+firstVisibleItem);
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem(){

//        MyAdapter mMyAdapter = new MyAdapter();

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        Log.d("LisettestActivity", "_mArrayList - "+mArrayList.size());
        Log.d("LisettestActivity", "_check - "+check);

        Log.d("LisettestActivity", "size - check - "+(mArrayList.size()-check));

        if(mArrayList.size()-check<=3 && finish_set==0){
            Log.d("LisettestActivity", "if mArrayList - "+mArrayList.size());
            Log.d("LisettestActivity", "if check - "+check);
            Log.d("LisettestActivity", "if num - "+((page * OFFSET) + 0) );

            for(int i = 0; i <mArrayList.size()-check ; i++){

                Log.d("LisettestActivity", "last  _i - " + i );
                Log.d("LisettestActivity", "last  _media_url - " + mArrayList.get((page * OFFSET) + i ).get("media_url") );

                String media_url = mArrayList.get((page * OFFSET) + i ).get("media_url");
                String media_type = mArrayList.get((page * OFFSET) + i ).get("media_type");
                String permalink = mArrayList.get((page * OFFSET) + i ).get("permalink");
                String timestamp = mArrayList.get((page * OFFSET) + i ).get("timestamp");
                String username = mArrayList.get((page * OFFSET) + i ).get("username");
                String id = mArrayList.get((page * OFFSET) + i ).get("id");

                String caption = null;
                try{
                    caption = mArrayList.get((page * OFFSET) + i ).get("caption");
                } catch (Exception e){
                    caption = "none";
                }
                String thumbnail_url = null;
                try{
                    thumbnail_url = mArrayList.get((page * OFFSET) + i ).get("thumbnail_url");
                } catch (Exception e){
                    thumbnail_url = "none";
                }

                if (media_type.equals("VIDEO")){
                    mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
                }
                else {
                    mMyAdapter.addItem(media_url, caption, username, media_type);
                }

                Log.d("LisettestActivity", "if check - "+((page * OFFSET) + i));
                Log.d("LisettestActivity", "if page - "+page);
                Log.d("LisettestActivity", "if OFFSET - "+OFFSET);
//                check = (page * OFFSET) + check;
                String label = "Label " + ((page * OFFSET) + i);
                //            list.add(label);
                Log.d("LisettestActivity", "COLUMN_MEDIA - "+mArrayList.get((page * OFFSET) + i).get(SQLiteHelper.COLUMN_MEDIA));

//                list.add(((page * OFFSET) + i ) + " - " + mArrayList.get((page * OFFSET) + i ).get(SQLiteHelper.COLUMN_ID));
            }
            finish_set = 1;
            finish_set_1 = 1;

            //         1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    page++;
                    mMyAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(GONE);
                    mLockListView = true;
                }
            },1000);

//            mLockListView = true;
        }
        else if(finish_set_1 == 0){

//            mLockListView = true;
            Log.d("LisettestActivity", "else mArrayList - "+mArrayList.size());
            Log.d("LisettestActivity", "else check - "+check);
            // 다음 20개의 데이터를 불러와서 리스트에 저장한다.
            for(int i = 0; i < 3; i++){

//                String caption = mArrayList.get((page * OFFSET) + i ).get("caption");
                String media_url = mArrayList.get((page * OFFSET) + i ).get("media_url");
                String media_type = mArrayList.get((page * OFFSET) + i ).get("media_type");
                String permalink = mArrayList.get((page * OFFSET) + i ).get("permalink");
                String timestamp = mArrayList.get((page * OFFSET) + i ).get("timestamp");
                String username = mArrayList.get((page * OFFSET) + i ).get("username");
                String id = mArrayList.get((page * OFFSET) + i ).get("id");
//                String thumbnail_url = mArrayList.get((page * OFFSET) + i ).get("thumbnail_url");
                String access_token = mArrayList.get((page * OFFSET) + i ).get("access_token");

//                hashMap.put("caption", caption);
//                hashMap.put("media_url", media_url);
//                hashMap.put("media_type", media_type);
//                hashMap.put("permalink", permalink);
//                hashMap.put("timestamp", timestamp);
//                hashMap.put("username", username);
//                hashMap.put("id", id);
//                hashMap.put("thumbnail_url", thumbnail_url);
//                hashMap.put("access_token", _accesstoken);

                String caption = null;
                try{
                    caption = mArrayList.get((page * OFFSET) + i ).get("caption");
                } catch (Exception e){
                    caption = "none";
                }
                String thumbnail_url = null;
                try{
                    thumbnail_url = mArrayList.get((page * OFFSET) + i ).get("thumbnail_url");
                } catch (Exception e){
                    thumbnail_url = "none";
                }

                if (media_type.equals("VIDEO")){
                    mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
                }
                else {
                    mMyAdapter.addItem(media_url, caption, username, media_type);
                }


                check = (page * OFFSET) + i + 1;
                String label = "Label " + ((page * OFFSET) + i);
                //            list.add(label);
                Log.d("LisettestActivity", "COLUMN_MEDIA - "+mArrayList.get((page * OFFSET) + i).get(SQLiteHelper.COLUMN_MEDIA));

//                list.add(check + " - " + mArrayList.get((page * OFFSET) + i).get(SQLiteHelper.COLUMN_ID));
            }
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    page++;
//                    adapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
//                    mLockListView = false;
//                }
//            },1000);

            //         1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    page++;
                    mMyAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(GONE);
                    mLockListView = false;
                }
            },1000);

        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HashMap<String, String> map  = mArrayList.get(position+1);
                HashMap<String, String> map  = mArrayList.get(position);

                Log.d("ddd", "_accesstoken - "+_accesstoken);

                Intent intent = new Intent(LisettestActivity.this, FeedDetailActivity.class);
                intent.putExtra("map", map);
                intent.putExtra("_accesstoken", _accesstoken);
                startActivity(intent);
            }
        });

//        mListView.setAdapter(mMyAdapter);

//        for(int i = 0; i < 3; i++){
//            check = (page * OFFSET) + i;
//            String label = "Label " + ((page * OFFSET) + i);
//            //            list.add(label);
//            Log.d("LisettestActivity", "COLUMN_MEDIA - "+mArrayList.get((page * OFFSET) + i).get(SQLiteHelper.COLUMN_MEDIA));
//            list.add(check + " - " + mArrayList.get((page * OFFSET) + i).get(SQLiteHelper.COLUMN_ID));
//        }

////         1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                page++;
//                mMyAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                mLockListView = false;
//            }
//        },1000);

    }


    public void feedAPI(String access_token, String userid, String datetime) {

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

//                        CustomDTO dto = new CustomDTO();
//                        dto.setResId(media_url);
//                        dto.setTitle(username);
//                        dto.setContent(media_type);
//
//                        adapter.addItem(dto);


////                        new DownloadFilesTask().execute(media_url);
////                        mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_background), username, media_type);
//                        if (media_type.equals("VIDEO")){
//                            mMyAdapter.addItem(thumbnail_url, caption, username, media_type);
//                        }
//                        else {
//                            mMyAdapter.addItem(media_url, caption, username, media_type);
//                        }





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

//                        mArrayList.add(hashMap);
//
                        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
//                        mSQLiteHelper.insertFeedDatas(caption, media_url, media_type, thumbnail_url, permalink, timestamp, username, userid, id, datetime);
                        mSQLiteHelper.updateFeedDatas(caption, media_url, media_type, thumbnail_url, permalink, timestamp, username, userid, id, datetime);
                    }

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
                Intent intent = new Intent(LisettestActivity.this, LoginActivity.class);
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
