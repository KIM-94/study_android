package com.innowireless.xcal.mobile4g;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    ImageButton loginbtn;

    SQLiteHelper mSQLiteHelper;

    public ArrayList<HashMap<String, String>> mArrayList;
    private ListView mListViewList;
//    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        mListViewList = (ListView)findViewById(R.id.listView_main_list_login);
//        mArrayList = new ArrayList<>();
//        ArrayList<HashMap<String, String>> mArrayList;

        mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);
        mArrayList = mSQLiteHelper.checkLoginDatas();

        Log.d("LoginActivity", "mArrayList : " + mArrayList.isEmpty());

        if(mArrayList.isEmpty()){
            mListViewList.setVisibility(View.INVISIBLE);
        }
        else {
            mListViewList.setVisibility(View.VISIBLE);
        }

        ListAdapter adapter = new SimpleAdapter(
                LoginActivity.this, mArrayList, R.layout.row_listview,
                new String[]{SQLiteHelper.COLUMN_USERNAME},
                new int[]{R.id.label}
        );


        mListViewList.setAdapter(adapter);
        mListViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("LoginActivity", "mListViewList : click"+position);
                HashMap<String, String> map  = mArrayList.get(position);

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("map", map);
//                finish();
                startActivity(intent);
            }
        });

//        mListViewList.setAdapter(adapter);
//        mListViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                HashMap<String, String> map  = mArrayList.get(position);
//                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
//                intent.putExtra("map", map);
//                startActivity(intent);
//            }
//        });

        loginbtn = (ImageButton) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, LoginActivity2.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void listevent(ListAdapter adapter){
        mListViewList.setAdapter(adapter);
        mListViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("LoginActivity", "mListViewList : click");
                HashMap<String, String> map  = mArrayList.get(position);
                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                intent.putExtra("map", map);
                startActivity(intent);
            }
        });
    }

    public void initView(){
        loginbtn = (ImageButton)findViewById(R.id.loginbtn);
//        mArrayList = new ArrayList<>();
//        mListViewList = (ListView) findViewById(R.id.listView_main_list);
    }

//    public void MyOnClick(View view)
//    {
//        switch (view.getId()) {
//            case R.id.loginbtn:
//                Intent intent = new Intent(LoginActivity.this, LoginActivity2.class);
//                startActivity(intent);
//                break;
//            case R.id.listView_main_list_login:
//                Log.d("LoginActivity", "mListViewList : click");
//                listevent(adapter);
//                break;
////            case R.id.btn3:
////                textView.setTextColor(Color.GREEN);
////                break;
//        }
//    }
}