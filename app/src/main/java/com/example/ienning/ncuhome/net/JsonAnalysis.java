package com.example.ienning.ncuhome.net;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ienning.ncuhome.config.MyApplication;
import com.example.ienning.ncuhome.db.MyDatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ienning on 16-7-27.
 */
public class JsonAnalysis {
    private String token;
    private Context mContext;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    public String parseJSONWithGson(String json) {
        Jsondb jsondb = gson.fromJson(json, Jsondb.class);
        return jsondb.getToken();
    }
    public Jsondb parseJSONWithGson2(String json) {
        Jsondb jsondb = gson.fromJson(json, Jsondb.class);
        return jsondb;
    }
    public void parseJSONWithGsonSed(String json) {
        Jsondb jsondb  = gson.fromJson(json, Jsondb.class);
    }
    public int parseJSONWithGsonStatus(String json) {
        Jsondb jsondb = gson.fromJson(json, Jsondb.class);
        return jsondb.getStatus();
    }
    public JsonAnalysis() {

    }
    public JsonAnalysis(Context context) {
        mContext = context;
    }
    public String parseJSONWithScores(String json) {
        return "";
    }
    public void parseJSONWithGsons(Context context, String json) {
        Type userListType = new TypeToken<UserGrade<List<List<UserScores>>>>(){}.getType();
        UserGrade<List<List<UserScores>>> userListGrade = gson.fromJson(json, userListType);
        if (userListGrade.status != 0) {
            List<List<UserScores>> scores = userListGrade.scores;
            Iterator<UserScores> it = scores.get(0).iterator();
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context, "QueryGrade.db", null, 1);
            SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
            if (tableIsExist(db, "Grade")) {
                Log.i("Ienning", "delete Grade is ");
                db.delete("Grade", null, null);
            }
            ContentValues values = new ContentValues();
            while (it.hasNext()) {
                UserScores us = it.next();
                Log.i("Ienning", "lesson The result is " + us.credit + " " + us.lesson_name + " " + us.score + " " + us.term);
                values.put("term", us.term);
                values.put("lessonName", us.lesson_name);
                values.put("score", us.score);
                values.put("credit", us.credit);
                db.insert("Grade", null, values);
                values.clear();
            }
        }
        //Log.i("Ienning", "print the result is" + scores.get(0).s);
    }
    public void parseJSONWithMyElect(String json) {
        Log.i("Ienning", "parse de json is " + json);
        UserElectric userElectric= gson.fromJson(json, UserElectric.class);
        UserMyElectric userMyElectric = userElectric.data;
        mContext = MyApplication.getContext();
        preferences = mContext.getSharedPreferences("ncuhome", mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("electUserDormNum", userElectric.qsh);
        editor.putString("electUserNumber", userMyElectric.byyd);
        editor.putString("electLeftMoney", userMyElectric.xjye);
        editor.putString("electLeftNumber", userMyElectric.dlye);
        editor.putString("electExceptTime", userMyElectric.syts);
        editor.commit();
        //parseJSONWithElect(userMyElectric);
    }
    public void parseJSONWithFour(String json) {
        UserFourAndSix userFourAndSix = gson.fromJson(json, UserFourAndSix.class);
        UserMyFourAndSix userMyFourAndSix = userFourAndSix.data;
        mContext = MyApplication.getContext();
        preferences = mContext.getSharedPreferences("ncuhome", mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("fourUserName", userMyFourAndSix.name);
        editor.putString("fourSchool", userMyFourAndSix.scholl);
        editor.putString("fourType", userMyFourAndSix.type);
        editor.putString("fourExamid", userMyFourAndSix.examid);
        editor.putString("fourTime", userMyFourAndSix.time);
        editor.putString("fourTotal", userMyFourAndSix.total);
        editor.putString("fourListening", userMyFourAndSix.listening);
        editor.putString("fourReading", userMyFourAndSix.reading);
        editor.putString("fourWritingTranslating", userMyFourAndSix.writing_translating);
        editor.commit();
        /*
        //Type userElectListType = new TypeToken<UserElectric<List<UserMyElectric>>>(){}.getType();
        //UserElectric<List<UserMyElectric>> userElectric = gson.fromJson(json, userElectListType);
        List<UserMyElectric> electrics = userElectric.data;
        Iterator<UserMyElectric> it = electrics.iterator();
        if (it.hasNext()) {
            UserMyElectric electricsResult = it.next();
            Log.i("Ienning", "parseJSONWithElect: " + electricsResult.byyd);
        }
        */
    }
    public void parseJSONWithLib(Context context, String json) {
        Type userListType = new TypeToken<UserLib<List<UserLibBook>>>(){}.getType();
        UserLib<List<UserLibBook>> userListBook = gson.fromJson(json, userListType);
        mContext = MyApplication.getContext();
        preferences = mContext.getSharedPreferences("ncuhome", mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("bookStatus", userListBook.status);
        editor.putString("bookMessage", userListBook.message);
        editor.commit();
        if (userListBook.status != 0) {
            List<UserLibBook> libBooks = userListBook.data;
            /* 有优化的空间 */
            Iterator<UserLibBook> it = libBooks.iterator();
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context, "QueryGrade.db", null , 1);
            SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
            if (tableIsExist(db, "rentBook")) {
                db.delete("rentBook", null, null);
            }
            ContentValues values = new ContentValues();
            while (it.hasNext()) {
                UserLibBook ul = it.next();
                values.put("author", ul.author);
                values.put("barCode", ul.barCode);
                values.put("borrowDate", ul.borrowDate);
                values.put("checks", ul.check);
                values.put("location", ul.location);
                values.put("returnDate", ul.returnDate);
                values.put("title", ul.title);
                db.insert("rentBook", null, values);
                values.clear();
                Log.i("Ienning", "parseJSONWithLib: is result " + ul.title + ul.author + ul.barCode + ul.borrowDate + ul.check + ul.location + ul.returnDate);
            }
        }
    }
    private boolean tableIsExist(SQLiteDatabase db, String talbename) {
        boolean result = false;
        if (db == null) {
            return false;
        }
        if (talbename == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select * from " + talbename;
            cursor  = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                if (id > 0) {
                    result = true;
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
