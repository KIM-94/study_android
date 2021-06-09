package com.study.hello.hellovolley;

import android.content.Context;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static java.sql.DriverManager.println;

public class Weater {

    private static final String TAG_Weater_1 ="lon";
    private static final String TAG_Weater_2 ="lat";
    private static final String TAG_Weater_3 ="id";
    private static final String TAG_Weater_4 ="main";
    private static final String TAG_Weater_5 ="description";
    private static final String TAG_Weater_6 ="icon";
    private static final String TAG_Weater_7 ="temp";
    private static final String TAG_Weater_8 ="feels_like";
    private static final String TAG_Weater_9 ="temp_min";
    private static final String TAG_Weater_10 ="temp_max";
    private static final String TAG_Weater_11 ="pressure";
    private static final String TAG_Weater_12 ="humidity";
    private static final String TAG_Weater_13 ="speed";
    private static final String TAG_Weater_14 ="name";
    private static final String TAG_Weater_15 ="TAG_Weater_";
    private static final String TAG_Weater_16 ="TAG_Weater_";
    private static final String TAG_Weater_17 ="TAG_Weater_";
    private static final String TAG_Weater_18 ="TAG_Weater_";
    private static final String TAG_Weater_19 ="TAG_Weater_";
    private static final String TAG_Weater_20 ="TAG_Weater_";
    private static final String TAG_Weater_21 ="TAG_Weater_";
    private static final String TAG_Weater_22 ="TAG_Weater_";
    private static final String TAG_Weater_23 ="TAG_Weater_";
    private static final String TAG_Weater_24 ="TAG_Weater_";
    private static final String TAG_Weater_25 ="TAG_Weater_";
    private static final String TAG_Weater_26 ="TAG_Weater_";

    void weatherAPI(View.OnClickListener context) {
        gpsTracker = new GpsTracker(MainActivity.this);
//        String url = "https://news.naver.com/";
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+gpsTracker.getLatitude()+"&lon="+gpsTracker.getLongitude()+"&appid="+apiKEY;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답을 잘 받았을 때 이 메소드가 자동으로 호출
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> " + response);
                    }
                },
                new Response.ErrorListener() { //에러 발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }
        };
        request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue((Context) context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(request);
        println("요청 보냄.");
    }
}
