package com.example.ienning.ncuhome.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ienning.ncuhome.R;
import com.example.ienning.ncuhome.adapter.MyCardListAdapter;
import com.example.ienning.ncuhome.db.ItemBook;
import com.example.ienning.ncuhome.db.MyDatabaseHelper;
import com.example.ienning.ncuhome.net.HttpUtil;
import com.example.ienning.ncuhome.net.JsonAnalysis;
import com.example.ienning.ncuhome.net.Jsondb;
import com.example.ienning.ncuhome.ui.SpaceItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ienning on 16-9-30.
 */

public class LibraryRentBook extends Activity {
    private MyDatabaseHelper myDatabase;
    private SharedPreferences preferences;
    private String responseStr;
    private int bookStatus;
    private List<ItemBook> dataList;
    private RecyclerView recyclerView;
    private TextView bookNum;
    private MyCardListAdapter cardListAdapter;
    private LinearLayout backMain;
    private ImageButton backMain1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rent_book);
        recyclerView = (RecyclerView) findViewById(R.id.library_book_list);
        backMain = (LinearLayout) findViewById(R.id.back_main3);
        backMain1 = (ImageButton) findViewById(R.id.back_main4);
        bookNum = (TextView) findViewById(R.id.library_book_num);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LibraryRentBook.this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(linearLayoutManager);
        queryBooks();
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryRentBook.this, MainActivity.class);
                startActivity(intent);
            }
        });
        backMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryRentBook.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getBooks(List<ItemBook> itemBooks) {
        myDatabase = new MyDatabaseHelper(this, "QueryGrade.db", null, 1);
        SQLiteDatabase db = myDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM rentBook", null);
        if (cursor.moveToFirst()) {
            do {
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String barCode = cursor.getString(cursor.getColumnIndex("barCode"));
                String borrowDate = cursor.getString(cursor.getColumnIndex("borrowDate"));
                String checks = cursor.getString(cursor.getColumnIndex("checks"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String returnDate = cursor.getString(cursor.getColumnIndex("returnDate"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                Log.i("Ienning", "getBooks: " + author + barCode + borrowDate + checks + location + returnDate + title);
                itemBooks.add(new ItemBook(author, barCode, borrowDate, checks, location, returnDate, title));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void queryBooks() {
       preferences = getSharedPreferences("ncuhome", MODE_PRIVATE);
        final JsonAnalysis jsonAnalysis = new JsonAnalysis();
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.get("http://www.ncuos.com/api/library/libinfo", preferences.getString("token", null), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseStr = response.body().string();
                LibraryRentBook.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Ienning", "bookStatus is " + responseStr);
                        jsonAnalysis.parseJSONWithLib(LibraryRentBook.this, responseStr);
                        bookStatus = preferences.getInt("bookStatus", 0);
                        dataList = new ArrayList<>();
                        if (bookStatus != 0) {
                            Log.i("Ienning", "Library loggin successful!");
                            getBooks(dataList);
                        }
                        else {
                            Toast.makeText(LibraryRentBook.this, "" + preferences.getString("bookMessage", null), Toast.LENGTH_SHORT).show();
                        }
                        Log.i("Ienning", "run: dataList size is " + dataList.size());
                        cardListAdapter = new MyCardListAdapter(LibraryRentBook.this, dataList);
                        recyclerView.setAdapter(cardListAdapter);
                        bookNum.setText("" + dataList.size());
                        cardListAdapter.setOnItemClickListener(new MyCardListAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                final String json;
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("barCode", dataList.get(position).getBarCode());
                                    jsonObject.put("check", dataList.get(position).getCkeck());
                                    json = jsonObject.toString();
                                    httpUtil.postl("http://www.ncuos.com/api/library/renew", json, preferences.getString("token", null), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            responseStr = response.body().string();
                                            final Jsondb jsondb = jsonAnalysis.parseJSONWithGson2(responseStr);
                                            LibraryRentBook.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.i("Ienning", "run: The message is " + jsondb.getMessage());
                                                    Toast.makeText(LibraryRentBook.this, "" + jsondb.getMessage(), Toast.LENGTH_SHORT).show();
                                                    queryBooks();
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
                        });
                    }
                });
            }
        });
    }
}
