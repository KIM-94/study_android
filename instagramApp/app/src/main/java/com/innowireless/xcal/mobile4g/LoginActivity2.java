package com.innowireless.xcal.mobile4g;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LoginActivity2 extends AppCompatActivity {
    private WebView webview;
    private WebSettings mWebSettings; //웹뷰세팅


    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="data";
    private RequestQueue queue;

    private String datetime;

    SQLiteHelper mSQLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // 웹뷰 시작
        webview = (WebView) findViewById(R.id.webView1);


//      쿠키 삭제
        webview.clearCache(true);
        webview.clearHistory();
        // 자동완성은 8.0부터는 내장되어 아래 함수 안먹음
        webview.clearFormData();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(LoginActivity2.this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        } else {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(new ValueCallback() {
                @Override
                public void onReceiveValue(Object value) {
                    Log.d("onReceiveValue", value.toString());
                }
            });
            cookieManager.getInstance().flush();
        }

        webview.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = webview.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

//        webview.loadUrl("https://www.instagram.com/oauth/authorize?client_id=545607339953613&redirect_uri=https://193.122.111.182/capstonD2/jsontest2.php&scope=user_profile%2Cuser_media&response_type=code");
        webview.loadUrl("https://www.instagram.com/oauth/authorize?client_id=458067242192936&redirect_uri=https://instagram.com/&scope=user_profile%2Cuser_media&response_type=code");


        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            /**
             * 웹페이지 로딩이 시작할 때 처리
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("webview", "onPageStarted - "+url);
            }

            /**
             * 웹페이지 로딩중 에러가 발생했을때 처리
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("webview", "onReceivedError - "+failingUrl);
            }

            /**
             * 웹페이지 로딩이 끝났을 때 처리
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("webview", "onPageFinished - "+url);
                if(url.contains("?code=")){
                    String accessToken = url.split("code=")[1].split("#_")[0];
                    Log.d("webview", "accessToken - "+accessToken);

                    accessTokenAPI(accessToken);

//                    Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
//                    intent.putExtra("accessToken",accessToken);
//                    finish();
//                    startActivity(intent);
                }

            }
        });
    }
    public void MyOnClick(View view)
    {
        switch (view.getId()) {
            case R.id.webView1:
                Log.d("url", "url - "+webview.getUrl());
//                accessTokenAPI(webview.getUrl());
                break;
//            case R.id.btn2:
//                textView.setTextColor(Color.BLUE);
//                break;
//            case R.id.btn3:
//                textView.setTextColor(Color.GREEN);
//                break;
        }
    }


    private void accessTokenAPI(String token) {

//        final String client_id = "545607339953613";
        final String client_id = "458067242192936";
//        final String client_secret = "8807aca65b9d49bcdd4f1dad0e1c119f";
        final String client_secret = "444c34376b46f5fc73437eccc69e4f4a";
//        final String code = token.split("code=")[1].split("#_")[0];
        final String code = token;
        final String grant_type = "authorization_code";
//        final String redirect_uri = "https://193.122.111.182/capstonD2/jsontest2.php";
        final String redirect_uri = "https://instagram.com/";

        Response.Listener<String> resposneListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "response - "+response);
                try {
//                    Log.d("response", "response - "+response);
                    JSONObject jsonObject = new JSONObject(response);
                    String access_token = jsonObject.getString("access_token");
                    String user_id = jsonObject.getString("user_id");

                    Log.d("response", "access_token : "+access_token+", user_id : "+user_id);

                    longaccessTokenAPI(access_token, user_id);
//                    getUserInfoAPI(user_id,access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),"에러발생", Toast.LENGTH_SHORT).show();
                return;
            }
        };

        // Volley 로 회원양식 웹으로 전송
        signin_param registerRequest = new signin_param(client_id, client_secret, code, grant_type, redirect_uri, resposneListener,errorListener);
        registerRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(registerRequest);
    }

    private void longaccessTokenAPI(String token, String userid) {
        final String client_secret = "444c34376b46f5fc73437eccc69e4f4a";

        queue = Volley.newRequestQueue(this);

        String url = "https://graph.instagram.com/access_token?grant_type=ig_exchange_token&client_secret="+client_secret+"&access_token="+token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "response - "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String _userid = userid;
                    String _access_token = jsonObject.getString("access_token");

                    getUserInfoAPI(_userid,_access_token);

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

    private void getUserInfoAPI(String user_id, String access_token) {

        queue = Volley.newRequestQueue(this);
//        String url = "https://ç.com/me/media"+gpsTracker.getLatitude()+"&lon="+gpsTracker.getLongitude()+"&appid="+apiKEY;

        String url = "https://graph.instagram.com/me?fields=id,username&access_token="+access_token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "response - "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // TIME.
                    long now = System.currentTimeMillis();  // 현재시간을 msec 으로 구한다
                    Date date = new Date(now);  // 현재시간을 date 변수에 저장한다.
                    SimpleDateFormat sdfNow1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    datetime = sdfNow1.format(date);  // nowDate 변수에 값을 저장한다.

                    Log.d(TAG, "userid - " + jsonObject.getString("id"));
                    Log.d(TAG, "username - " + jsonObject.getString("username"));
                    Log.d(TAG, "access token - " + access_token);
                    Log.d(TAG, "start_time - " + datetime);

                    String _userid = jsonObject.getString("id");
                    String _username = jsonObject.getString("username");
                    String _access_token = access_token;
                    String _datetime = datetime;


                    mSQLiteHelper = new SQLiteHelper(getApplicationContext(), "MyInsta.db", null, 1);

//                    userid 중복 체크
                    if (mSQLiteHelper.checkUsetInfoDatas(_userid, _username, _access_token, _datetime) > 0) {
//                   if 중복 체크 결과 - 이미 존재하는 userid - update
                        mSQLiteHelper.updateUsetInfoDatas(_userid, _username, _access_token, _datetime);
                        Log.d("check","updateUsetInfoDatas");
                        Log.d("check","updateUsetInfoDatas - access_token : "+_access_token);
                    }
                    else {
//                   else if 중복 체크 결과 - 새로 추가하는 userid - insert
                        mSQLiteHelper.insertUsetInfoDatas(_userid, _username, _access_token, _datetime);
                        Log.d("check","insertUsetInfoDatas");
                    }

//                    try{
////                   else if 중복 체크 결과 - 새로 추가하는 userid - insert
//                        mSQLiteHelper.insertUsetInfoDatas(_userid, _username, _access_token, _datetime);
//                        Log.d("check","insertUsetInfoDatas");
//                    } catch (Exception e){
////                   if 중복 체크 결과 - 이미 존재하는 userid - update
//                        mSQLiteHelper.updateUsetInfoDatas(_userid, _username, _access_token, _datetime);
//                        Log.d("check","updateUsetInfoDatas : " + e);
//                    }

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(SQLiteHelper.COLUMN_USERID, _userid);
                    hashMap.put(SQLiteHelper.COLUMN_USERNAME, _username);
                    hashMap.put(SQLiteHelper.COLUMN_ACCESSTOKEN, _access_token);
                    hashMap.put(SQLiteHelper.COLUMN_DATETIME, _datetime);

                    Intent intent = new Intent(LoginActivity2.this, MenuActivity.class);
                    intent.putExtra("map", hashMap);
                    finish();
                    startActivity(intent);

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

}