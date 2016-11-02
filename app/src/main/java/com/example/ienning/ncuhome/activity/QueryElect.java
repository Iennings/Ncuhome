package com.example.ienning.ncuhome.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ienning.ncuhome.R;
import com.example.ienning.ncuhome.net.HttpUtil;
import com.example.ienning.ncuhome.net.JsonAnalysis;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ienning on 16-8-28.
 */
public class QueryElect extends Activity{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String responseStr;
    private int status;
    private EditText dormNumber;
    private EditText telphone;
    private String dormNum;
    private String telphonenum;
    private Button electQuery;
    private TextView useNumber;
    private TextView leftMoney;
    private TextView leftNumber;
    private TextView exceptTime;
    private LinearLayout back;
    private TextView centerTitle;
    private Button changeDromnum;
    private ImageButton back1;
    private TextView dormNums;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_query_myelectric);
        useNumber = (TextView) findViewById(R.id.query_elect_use_number);
        leftMoney = (TextView) findViewById(R.id.query_elect_left_money);
        leftNumber = (TextView) findViewById(R.id.query_elect_left_number);
        exceptTime = (TextView) findViewById(R.id.query_elect_except_time);
        dormNums = (TextView) findViewById(R.id.query_elect_show);
        back = (LinearLayout) findViewById(R.id.back_main3);
        back1 = (ImageButton) findViewById(R.id.back_main4);
        centerTitle = (TextView) findViewById(R.id.center_title);
        changeDromnum = (Button) findViewById(R.id.query_elect_change_dromnum);
        changeDromnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueryElect.this, Login.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QueryElect.this, MainActivity.class);
                startActivity(intent);
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QueryElect.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*

        /*
        dormNumber = (EditText) findViewById(R.id.query_elect_dorm);
        telphone = (EditText) findViewById(R.id.query_elect_tel);
        electQuery = (Button) findViewById(R.id.query_elect_query);
        electQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dormNum = dormNumber.getText().toString();
                telphonenum = telphone.getText().toString();
                if (checkQuery(dormNum, telphonenum))
                {
                    queryElec(dormNum, telphonenum);
                }
            }
        });
    }
    public boolean checkQuery(String dormNum, String telphonenum) {
        if (dormNum.length() == 0) {
            Toast.makeText(QueryElect.this, "请输入寝室序号", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (dormNum.length() != 6)
        {
            Toast.makeText(QueryElect.this, "请输入正确寝室号", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (telphonenum.length() == 0) {
            Toast.makeText(QueryElect.this, "请输入您的来联系方式", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
        */
        queryElec();
    }
    public void queryElec() {
        preferences = getSharedPreferences("ncuhome", MODE_PRIVATE);
        Log.i("Ienning", "queryElec: " + preferences.getString("token", null));
        editor = preferences.edit();
        final JsonAnalysis jsonAnalysis = new JsonAnalysis();
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.get("http://www.ncuos.com/api/info/elec", preferences.getString("token", null), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Ienning", "onFailure: elect " + "没有网络连接请求");
                Toast.makeText(QueryElect.this, "没有网络链接请求", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("Ienning", "onResponse: to confirm it if has internet");
                if (preferences.getInt("relogin", 0) == 0) {
                    responseStr = response.body().string();
                    status = jsonAnalysis.parseJSONWithGsonStatus(responseStr);
                    Log.i("Ienning", "onResponse: the stattus is " + responseStr);
                    if (status == 1) {
                        QueryElect.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jsonAnalysis.parseJSONWithMyElect(responseStr);
                            /*
                            editor.putString("electUseNumber", userMyElectric.byyd);
                            editor.putString("electLeftMoney", userMyElectric.xjye);
                            editor.putString("electLeftNumber", userMyElectric.dlye);
                            editor.putString("electExceptTime", userMyElectric.syts);
                            editor.commit();
                            */
                                useNumber.setText(preferences.getString("electUserNumber", null));
                                leftMoney.setText(preferences.getString("electLeftMoney", null));
                                leftNumber.setText(preferences.getString("electLeftNumber", null));
                                exceptTime.setText(preferences.getString("electExceptTime", null));
                                dormNums.setText(preferences.getString("electUserDormNum", null));
                            }
                        });
                    }
                }
                else {
                    Intent intent = new Intent(QueryElect.this, Login.class);
//                    intent.putExtra("message", "抱歉你的密码已经修改了，请重新登录");
                    startActivity(intent);
                }
            }
        });
    }
}
