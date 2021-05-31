package com.innowireless.xcal.mobile4g;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManagerActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;

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

    public Bitmap getBitmap;


    //    ------------------------------
    private CustomAdapter adapter;
    private ListView listView;

    private ListView mListView;
    ImageView loginbtn;

    ArrayList<HashMap<String, String>> mArrayList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        mListView = (ListView) findViewById(R.id.listview_manager);

        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
        mArrayList = mSQLiteHelper.checkLoginDatas();

        ManagerAdapter mMyAdapter = new ManagerAdapter();

        mArrayList1 = new ArrayList<>();

        HashMap<String,String> hashMap1 = new HashMap<>();
        for(int i=0; i<mArrayList.size(); i++){
            double dist, KmH;

            hashMap1 = mArrayList.get(i);

            Log.d("usermanager", "username - "+ mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME));

            String username = mArrayList.get(i).get(SQLiteHelper.COLUMN_USERNAME);
            String datetime = mArrayList.get(i).get(SQLiteHelper.COLUMN_DATETIME);
            String userid = mArrayList.get(i).get(SQLiteHelper.COLUMN_USERID);
            String accesstoken = mArrayList.get(i).get(SQLiteHelper.COLUMN_ACCESSTOKEN);

            mMyAdapter.addItem(username, datetime, userid);

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put(SQLiteHelper.COLUMN_USERID, userid);
            hashMap.put(SQLiteHelper.COLUMN_USERNAME, username);
            hashMap.put(SQLiteHelper.COLUMN_ACCESSTOKEN, accesstoken);
            hashMap.put(SQLiteHelper.COLUMN_DATETIME, datetime);

            mArrayList1.add(hashMap);
        }

        mListView.setAdapter(mMyAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map  = mArrayList1.get(position);
//                            HashMap<String, String> map  = new HashMap<>();

                show(map);

//                Intent intent = new Intent(UserManagerActivity.this, MenuActivity.class);
//                intent.putExtra("map", map);
//                startActivity(intent);
            }
        });

        loginbtn = (ImageView) findViewById(R.id.iv_loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(UserManagerActivity.this, LoginActivity2.class);
//                finish();
                startActivity(intent);
            }
        });
//        Log.d("LoginActivity", "mArrayList : " + mArrayList.isEmpty());
//
//        ListAdapter adapter = new SimpleAdapter(
//                LoginActivity.this, mArrayList, R.layout.row_listview,
//                new String[]{SQLiteHelper.COLUMN_USERNAME},
//                new int[]{R.id.label}
//        );
//
//
//        mListViewList.setAdapter(adapter);
//        mListViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("LoginActivity", "mListViewList : click"+position);
//                HashMap<String, String> map  = mArrayList.get(position);
//
//                Intent intent = new Intent(LoginActivity.this, ListTestActivity3.class);
//                intent.putExtra("map", map);
////                finish();
//                startActivity(intent);
//            }
//        });


    }

    void show(HashMap<String, String> map)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("원하는 동작을 선택 하세요.");
//        builder.setMessage("계정 전환 / 로그아웃");
        builder.setPositiveButton("계정전환",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserManagerActivity.this, MenuActivity.class);
                        intent.putExtra("map", map);
                        finish();
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("로그아웃",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                        SQLiteHelper mSQLiteHelper;
                        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
                        mSQLiteHelper.deletUsetInfoDatas(map.get(SQLiteHelper.COLUMN_USERID), map.get(SQLiteHelper.COLUMN_USERNAME), map.get(SQLiteHelper.COLUMN_DATETIME));

                        Intent intent = new Intent(UserManagerActivity.this, LoginActivity.class);
                        finish();
//                        intent.putExtra("map", map);
                        startActivity(intent);
                    }
                });
        builder.show();
    }
}