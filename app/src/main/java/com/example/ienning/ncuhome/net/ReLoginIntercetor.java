package com.example.ienning.ncuhome.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ienning.ncuhome.config.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ienning on 16-7-28.
 */
public class ReLoginIntercetor implements Interceptor{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    Context context1;
    private String token;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public ReLoginIntercetor() {
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.i("Ienning", "zhe shi yi ge tong ku  ");
        Request originalRequest = chain.request();
        Response originalResponse = chain.proceed(originalRequest);
        Log.i("Ienning", "zhe ge code shi " + originalResponse.code());
        if (originalResponse.code() == 401) {
            originalResponse.body().close();
            Log.i("Ienning", "ri le gou le ");
            context = MyApplication.getContext();
            preferences = context.getSharedPreferences("ncuhome", context.MODE_PRIVATE);
            editor = preferences.edit();
            RequestBody body = RequestBody.create(JSON, preferences.getString("userJson", null));
            Request loginRequest = new Request.Builder()
                    .url("http://www.ncuos.com/api/user/token")
                    .post(body)
                    .build();
            Response loginResponse = chain.proceed(loginRequest);
            JsonAnalysis jsonAnalysis = new JsonAnalysis();
            String responseStr = loginResponse.body().string();
            Jsondb jsondb = jsonAnalysis.parseJSONWithGson2(responseStr);
            if (loginResponse.code() == 200 && jsondb.getStatus() == 1)
            {
//                token = jsonAnalysis.parseJSONWithGson(responseStr);
                token = jsondb.getToken();
                editor.putString("token", token);
                editor.commit();
                Log.i("Ienning", "intercept: " + preferences.getString("token", null));
                loginResponse.body().close();
                // 注意要替换请求头，因为请求头的Token是原来过期的Token，需要使用新的替换
                Request compressRequest = originalRequest.newBuilder()
                        .header("Authorization", "passport " + token)
                        .build();
                return chain.proceed(compressRequest);
            }
            else {
                Log.i("Ienning", "intercept: to test intent if ok");
                editor.putInt("relogin", 1);
                editor.putInt("state", 0);
                editor.commit();
            }
        }
        return originalResponse;
    }
}
