package com.innowireless.xcal.mobile4g;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class signin_param extends StringRequest {
    private final static String URL = "https://api.instagram.com/oauth/access_token";
    private Map<String, String> map;

    public signin_param(String client_id, String client_secret, String code, String grant_type, String redirect_uri, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST,URL, listener, errorListener);

        map = new HashMap<>();
        map.put("client_id",client_id);
        map.put("client_secret",client_secret);
        map.put("code",code);
        map.put("grant_type",grant_type);
        map.put("redirect_uri",redirect_uri);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
