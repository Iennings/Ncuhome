package com.example.ienning.ncuhome.personcenter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ienning.ncuhome.R;
import com.example.ienning.ncuhome.activity.Croppicture;
import com.example.ienning.ncuhome.activity.MainActivity;
import com.example.ienning.ncuhome.cache.ClipActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ienning on 16-7-17.
 */
public class PersonCenter extends Activity implements View.OnClickListener{

    private ImageView head;
    private LinearLayout back;
    private LinearLayout library;
    private LinearLayout phoneNumber;
    private LinearLayout changePassword;

    private LinearLayout feedback;
    private LinearLayout aboutHome;
    private LinearLayout logout;
    private PopupWindow popupWindow;
    private String photoSaveName;
    private String path;
    private String photoSavePath;
    private LayoutInflater layoutInflater;
    private TextView  albums;
    private LinearLayout cancel;
    public static final int PHOTOZOOM = 1;
    public static final int IMAGE_COMPLETE = 2;
    public static final int CROPERQCODE = 3;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_center);
        getpermission();
        layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File file = new File(Environment.getExternalStorageDirectory(), "ClipHeadPhoto/cache");
        if (!file.exists()) {
            Log.i("Ienning", "onCreate: The photo cache0 " + file);
            file.mkdirs();
            Log.i("Ienning", "onCreate: The photo cache1 " + file);
        }
//        if (ContextCompat.checkSelfPermission(PersonCenter.this, ))
        Log.i("Ienning", "onCreate: The photo cache2 " + file);
        sharedPreferences = getSharedPreferences("ncuhome", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        photoSavePath = Environment.getExternalStorageDirectory() + "/ClipHeadPhoto/cache/";
        photoSaveName = System.currentTimeMillis() + ".png";
        head = (ImageView) findViewById(R.id.head);
        back = (LinearLayout) findViewById(R.id.back_main);
        library = (LinearLayout) findViewById(R.id.library);
        phoneNumber = (LinearLayout) findViewById(R.id.phonenumber);
        changePassword = (LinearLayout) findViewById(R.id.change_password);
        feedback = (LinearLayout) findViewById(R.id.feedback);
        aboutHome = (LinearLayout) findViewById(R.id.about_home);
        logout = (LinearLayout) findViewById(R.id.logout);
        try {
            head.setImageBitmap(getLoacalBitmap(sharedPreferences.getString("temppath", null)));
        } catch (Exception e) {
           e.printStackTrace();
        }
        back.setOnClickListener(this);
        library.setOnClickListener(this);
        phoneNumber.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        feedback.setOnClickListener(this);
        aboutHome.setOnClickListener(this);
        logout.setOnClickListener(this);
        head.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head:
                showPopupWindow(head);
                Log.i("Ienning", "onClick: this is a small test");
                Toast.makeText(PersonCenter.this, "small test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_main:
                Intent intent = new Intent(PersonCenter.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.library:
                Intent intent1 = new Intent(PersonCenter.this, BindLibrary.class);
                startActivity(intent1);
                break;
            case R.id.phonenumber:
                Intent intent2 = new Intent(PersonCenter.this, Phonenumber.class);
                startActivity(intent2);
                break;
            case R.id.change_password:
                Intent intent3 = new Intent(PersonCenter.this, ChangePassword.class);
                startActivity(intent3);
                break;
            case R.id.feedback:
                Intent intent4 = new Intent(PersonCenter.this, Feedback.class);
                startActivity(intent4);
                break;
            case R.id.about_home:
                Intent intent5 = new Intent(PersonCenter.this, AboutHome.class);
                startActivity(intent5);
                break;
            case R.id.logout:
                Intent intent6 = new Intent(PersonCenter.this, Croppicture.class);
                startActivity(intent6);
                break;
        }
    }
    @SuppressWarnings("deprecation")
    private void showPopupWindow(View parent) {
        if (popupWindow == null) {
            View view = layoutInflater.inflate(R.layout.layout_pop_selector,null);
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            initPop(view);
        }
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
    public void initPop(View view) {
        albums = (TextView)view.findViewById(R.id.albums);
        cancel = (LinearLayout) view.findViewById(R.id.cancel);
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                /* ACTION_GET_CONTENT 返回的URI 不是文件路径，所以使用ACTION_PICK */
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, PHOTOZOOM);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM:
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String[] proj = {
                        MediaStore.Images.Media.DATA
                };
                Log.i("Ienning", "The result is " + proj + " and the uri is " + uri);
                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    Log.i("Ienning", "onActivityResult: the cursor is " + column_index);
                    path = cursor.getString(column_index);
                }
                Intent intent3 = new Intent(PersonCenter.this, ClipActivity.class);
                intent3.putExtra("path", path);
                Log.i("Ienning", "The Path is " + path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE:
                final String temppath = data.getStringExtra("path");
                editor.putString("temppath", temppath);
                editor.commit();
                head.setImageBitmap(getLoacalBitmap(temppath));
                Log.i("Ienning", "it is true or not");
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void getpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                new AlertDialog.Builder(this)
                        .setMessage("get permission")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PersonCenter.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        }
        else {
            Log.i("Ienning", " this is ok manifest permission");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Ienning", "onRequestPermissionResult: the result permission is ok!");
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "permission denied！", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
    }
    /*
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitkat && DocumentsContract.isDocumentUri(context, uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            Log.i(TAG, "getPath: docId is " + docId);
            final String[] split = docId.split(":");
            final String type = split[0];
            Uri contentUri = null;
            if("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            Log.i("Ienning", "getPath: split is " + split[0] + " " + split[1]);
            final String selection ="id=?";
            final String[] selectionArgs = new String[] {
                    split[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                Log.i("Ienning", "getPath: index and path is " + index + cursor.getString(index));
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    */
}
