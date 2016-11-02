package com.example.ienning.ncuhome.personcenter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ienning.ncuhome.R;
import com.example.ienning.ncuhome.activity.Login;
import com.example.ienning.ncuhome.net.HttpUtil;
import com.example.ienning.ncuhome.net.JsonAnalysis;
import com.example.ienning.ncuhome.net.Jsondb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ienning on 16-8-1.
 */
public class ChangePassword extends Activity{
    private EditText oldPassword;
    private EditText newPassword;
    private String odpassword;
    private String nwpassword;
    private Button changePassword;
    private LinearLayout back;
    private TextView centerTitle;
    private String json;
    private String responseStr;
    private int status;
    private String message;
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_password);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        changePassword = (Button) findViewById(R.id.to_change_password);
        back = (LinearLayout) findViewById(R.id.back_main3);
        centerTitle = (TextView) findViewById(R.id.center_title);
        centerTitle.setText("修改密码");
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odpassword = oldPassword.getText().toString();
                nwpassword = newPassword.getText().toString();
                setChangePassword(odpassword, nwpassword);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, PersonCenter.class);
                startActivity(intent);
            }
        });
    }
    public void setChangePassword(String odpassword, String nwpassword) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("old_password", odpassword);
            jsonObject.put("new_password", nwpassword);
            json = jsonObject.toString();
            Log.i("Ienning", "setChangePassword: the json is " + json);
            sharedPreferences = getSharedPreferences("ncuhome", MODE_PRIVATE);
            httpUtil.put("http://www.ncuos.com/api/user/password", json, sharedPreferences.getString("token", null), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("Ienning", "onFailure: " + "没有网络连接");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseStr = response.body().string();
                    JsonAnalysis jsonAnalysis = new JsonAnalysis();
                    final Jsondb jsondb = jsonAnalysis.parseJSONWithGson2(responseStr);
                    ChangePassword.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangePassword.this, jsondb.getMessage(), Toast.LENGTH_SHORT).show();
                            if (jsondb.getStatus() == 1) {
                                Intent intent = new Intent(ChangePassword.this, Login.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
