package com.example.ienning.ncuhome.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ienning.ncuhome.R;

/**
 * Created by ienning on 16-8-30.
 */
public class FourAndSixInfo extends Activity{
    SharedPreferences sharedPreferences;
    private TextView fourName;
    private TextView fourSchool;
    private TextView fourType;
    private TextView fourNumber;
    private TextView fourTime;
    private TextView fourAll;
    private TextView fourListener;
    private TextView fourReading;
    private TextView fourWriting;
    private TextView centerTitle;
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_four);
        back = (LinearLayout) findViewById(R.id.back_main3);
        centerTitle = (TextView) findViewById(R.id.center_title);
        fourName = (TextView) findViewById(R.id.four_name);
        fourSchool = (TextView) findViewById(R.id.four_school);
        fourType = (TextView) findViewById(R.id.four_type);
        fourNumber = (TextView) findViewById(R.id.four_number);
        fourTime = (TextView) findViewById(R.id.four_time);
        fourAll = (TextView) findViewById(R.id.four_all);
        fourListener = (TextView) findViewById(R.id.four_listener);
        fourReading = (TextView) findViewById(R.id.four_read);
        fourWriting = (TextView) findViewById(R.id.four_write);
        sharedPreferences = getSharedPreferences("ncuhome", MODE_PRIVATE);
        fourName.setText(sharedPreferences.getString("fourUserName", null));
        fourSchool.setText(sharedPreferences.getString("fourSchool", null));
        fourType.setText(sharedPreferences.getString("fourType", null));
        fourNumber.setText(sharedPreferences.getString("fourExamid", null));
        fourTime.setText(sharedPreferences.getString("fourTime", null));
        fourAll.setText(sharedPreferences.getString("fourTotal", null));
        fourListener.setText(sharedPreferences.getString("fourListening", null));
        fourReading.setText(sharedPreferences.getString("fourReading", null));
        fourWriting.setText(sharedPreferences.getString("fourWritingTranslating", null));
        centerTitle.setText("四六级查询");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FourAndSixInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
