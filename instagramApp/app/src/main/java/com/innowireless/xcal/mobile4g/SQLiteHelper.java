package com.innowireless.xcal.mobile4g;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG_Time = "Time";
    public static final String TAG_LastTime = "LastTime";
    public static final String TAG_DistDif = "DistDif";
    public static final String TAG_CheckTime = "CheckTime";

    public static final String TAG_Lati = "Lati";
    public static final String TAG_Longti = "Longti";


    private static final String TAG_TABLE_NAME = "getaccel2";


    // 데이터베이스
    private static final String DATABASE_NAME      = "test.db";
    private static final int DATABASE_VERSION      = 1;

//    caption,media_url,thumbnail_url,media_type,thumbnail,permalink,timestamp,username
    // 테이블
    public static final String FEED_TABLE_NAME      = "feedTb";
    public static final String COLUMN_CAPTION       = "caption";
    public static final String COLUMN_THUMNAIL      = "thumbnail_url";
    public static final String COLUMN_MEDIA         = "media_url";
    public static final String COLUMN_MEDIATYPE     = "media_type";
    public static final String COLUMN_PERMALINK     = "permalink";
    public static final String COLUMN_TIMESTAMP     = "timestamp";
    public static final String COLUMN_USERNAME      = "username";
    public static final String COLUMN_ID            = "id";

    public static final String USERINFO_TABLE_NAME  = "userinfoTb";
    public static final String COLUMN_USERID        = "user_id";
    public static final String COLUMN_ACCESSTOKEN   = "access_token";
    public static final String COLUMN_DATETIME      = "datetiem";


    private static final String DATABASE_CREATE_TABLE_FEED = "create table "
            + FEED_TABLE_NAME + "("
            + COLUMN_CAPTION + " text, "
            + COLUMN_MEDIA + " text, "
            + COLUMN_MEDIATYPE + " text, "
            + COLUMN_THUMNAIL + " text, "
            + COLUMN_PERMALINK + " text, "
            + COLUMN_TIMESTAMP + " text, "
            + COLUMN_USERNAME + " text, "
            + COLUMN_USERID + " text, "
            + COLUMN_ID + " text, "
            + COLUMN_DATETIME + " text, "
            + "PRIMARY KEY (id) );";


    private static final String DATABASE_CREATE_TABLE_USERINFO = "create table "
            + USERINFO_TABLE_NAME + "("
            + COLUMN_USERID + " text, "
            + COLUMN_USERNAME + " text, "
            + COLUMN_ACCESSTOKEN + " text, "
            + COLUMN_DATETIME + " text, "
            + "PRIMARY KEY (user_id) );";

// 기존 테이블에 레코드 추가시 사용
//    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
//            + TABLE_TEAM + " ADD COLUMN " + COLUMN_COACH + " string;";
//
//    private static final String DATABASE_ALTER_TEAM_2 = "ALTER TABLE "
//            + TABLE_TEAM + " ADD COLUMN " + COLUMN_STADIUM + " string;";


//    public SQLiteHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
//        super(context, name, factory, version);
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 앱을 삭제후 앱을 재설치하면 기존 DB파일은 앱 삭제시 지워지지 않기 때문에
        // 테이블이 이미 있다고 생성 에러남
        // 앱을 재설치시 데이터베이스를 삭제해줘야함.
        db.execSQL("DROP TABLE IF EXISTS " + USERINFO_TABLE_NAME);
        db.execSQL(DATABASE_CREATE_TABLE_USERINFO);

        db.execSQL("DROP TABLE IF EXISTS " + FEED_TABLE_NAME);
        db.execSQL(DATABASE_CREATE_TABLE_FEED);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USERINFO_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + FEED_TABLE_NAME);
        onCreate(db);

        //기존 테이블에 레코드 추가시 사용
//        if (oldVersion < 2) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
//        }
//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_TEAM_2);
//        }
    }

    public void insertAllDatas1() {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(TAG_Time, "2");
            cv.put(TAG_LastTime, "2");
            cv.put(TAG_DistDif, "2");
            cv.put(TAG_CheckTime, "2");
//                cv.put(TAG_LastTime, tempMap.get(TAG_LastTime));
//                cv.put(TAG_DistDif, tempMap.get(TAG_DistDif));
//                cv.put(TAG_CheckTime, tempMap.get(TAG_CheckTime));
            db.insert("imageTb", null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void insertAllDatas2(String a, String b) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(TAG_Time, "2");
            cv.put(TAG_LastTime, a);
            cv.put(TAG_DistDif, b);
            cv.put(TAG_CheckTime, "2");
//                cv.put(TAG_LastTime, tempMap.get(TAG_LastTime));
//                cv.put(TAG_DistDif, tempMap.get(TAG_DistDif));
//                cv.put(TAG_CheckTime, tempMap.get(TAG_CheckTime));
            db.insert("imageTb", null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public int checkUsetInfoDatas(String userid, String username, String access_token, String datetime){
        int checkValue = 0;

//        String selectQuery2 = "SELECT * FROM userinfoTb WHERE user_id = " + "'" + userid + "'";
//        SELECT count(user_id) FROM userinfoTb WHERE user_id = '17841447779990516'
        String selectQuery2 = "SELECT count(user_id) FROM userinfoTb WHERE user_id = " + "'" + userid + "'";
//        String selectQuery2 = "SELECT count(user_id) FROM userinfoTb WHERE user_id = " + userid;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);
        Log.d("", "checkUsetInfoDatas user_id : "+ userid);
        Log.d("", "checkUsetInfoDatas : "+ cursor);
//        Log.d("", "checkUsetInfoDatas : " + cursor.getString(0));

        while (cursor.moveToNext()) {
            Log.d("", "checkUsetInfoDatas : "+ selectQuery2);
            Log.d("", "checkUsetInfoDatas : " + cursor.getColumnIndex("user_id"));
            Log.d("", "checkUsetInfoDatas : " + cursor.getString(0));
            checkValue = Integer.parseInt(cursor.getString(0));
        }

        return checkValue;
    }


//    public void updateUsetInfoDatas(String userid, String username, String access_token, String datetime){
//        String selectQuery2 = "UPDATE userinfoTb SET access_token = " + "'" + access_token + "', datetiem = " + "'" + datetime  + "'" + " WHERE user_id = " + "'" + userid + "'";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery2, null);
//        Log.d("", "checkUsetInfoDatas : " + selectQuery2);
////        Log.d("", "checkUsetInfoDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
//    }

    public void updateUsetInfoDatas(String userid, String username, String access_token, String datetime){
        String selectQuery2 = "UPDATE userinfoTb SET access_token = " + "'" + access_token + "', datetiem = " + "'" + datetime  + "'" + " WHERE user_id = " + "'" + userid + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(COLUMN_ACCESSTOKEN, access_token);
        value.put(COLUMN_DATETIME, datetime);

        db.update(USERINFO_TABLE_NAME, value, "user_id=?", new String[]{userid});
//        Cursor cursor = db.rawQuery(selectQuery2, null);
//        Log.d("", "checkUsetInfoDatas : " + selectQuery2);
////        Log.d("", "checkUsetInfoDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
    }


    public void updateFeedDatas(String caption, String media_url, String media_type, String thumbnail_url, String permalink, String timestamp, String username, String userid, String id, String datetime){
        String selectQuery2 = "UPDATE feedTb SET caption = " + "'" + caption + "', media_url = " + "'" + media_url + "', media_type = " + "'" + media_type + "', thumbnail_url = " + "'" + thumbnail_url + "', permalink = " + "'" + permalink + "', timestamp = " + "'" + timestamp  + "'" + "', username = " + "'" + username  + "'" + "', id = " + "'" + id + "', datetime = " + "'" + datetime  + "'" + " WHERE user_id = " + "'" + userid + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(COLUMN_CAPTION, caption);
        value.put(COLUMN_MEDIA, media_url);
        value.put(COLUMN_MEDIATYPE, media_type);
        value.put(COLUMN_THUMNAIL, thumbnail_url);
        value.put(COLUMN_PERMALINK, permalink);
        value.put(COLUMN_TIMESTAMP, timestamp);
        value.put(COLUMN_USERNAME, username);
        value.put(COLUMN_ID, id);
        value.put(COLUMN_DATETIME, datetime);

        db.update(FEED_TABLE_NAME, value, "user_id=?", new String[]{userid});
//        Cursor cursor = db.rawQuery(selectQuery2, null);
//        Log.d("", "checkUsetInfoDatas : " + selectQuery2);
////        Log.d("", "checkUsetInfoDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
    }


    public void deletUsetInfoDatas(String userid, String username, String datetime){
        Log.d("", "deletUsetInfoDatas : check");
//        String selectQuery2 = "DELETE FROM userinfoTb WHERE user_id = " + "'" + userid + "'";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery2, null);
        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues value = new ContentValues();
//        value.put(COLUMN_ACCESSTOKEN, access_token);
//        value.put(COLUMN_DATETIME, datetime);

        db.delete(USERINFO_TABLE_NAME, "user_id" + "=" + userid, null);
    }

    public ArrayList<HashMap<String, String>> checkLoginDatas(){
        Log.d("", "checkLoginDatas : check");

        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();

        String selectQuery2 = "SELECT * FROM userinfoTb";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            HashMap<String,String> hashMap = new HashMap<>();

//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_USERID));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_USERNAME));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_ACCESSTOKEN));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_DATETIME));

            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_ACCESSTOKEN)));
            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));

            hashMap.put(COLUMN_USERID, cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            hashMap.put(COLUMN_USERNAME, cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            hashMap.put(COLUMN_ACCESSTOKEN, cursor.getString(cursor.getColumnIndex(COLUMN_ACCESSTOKEN)));
            hashMap.put(COLUMN_DATETIME, cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));

            // Adding contact to list
            contactList.add(hashMap);
        }

        // return contact list
        return contactList;
    }

    public ArrayList<HashMap<String, String>> selectLoginDatas(String username){
        Log.d("", "checkLoginDatas : check");

        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();

        String selectQuery2 = "SELECT * FROM userinfoTb WHERE username = " + "'" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            HashMap<String,String> hashMap = new HashMap<>();

//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_USERID));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_USERNAME));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_ACCESSTOKEN));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_DATETIME));

            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_ACCESSTOKEN)));
            Log.d("", "checkLoginDatas : " + cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));

            hashMap.put(COLUMN_USERID, cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            hashMap.put(COLUMN_USERNAME, cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            hashMap.put(COLUMN_ACCESSTOKEN, cursor.getString(cursor.getColumnIndex(COLUMN_ACCESSTOKEN)));
            hashMap.put(COLUMN_DATETIME, cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));

            // Adding contact to list
            contactList.add(hashMap);
        }

        // return contact list
        return contactList;
    }


    public ArrayList<HashMap<String, String>> selectFeedDatas(String username){
        Log.d("", "checkLoginDatas : check");

        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();

        String selectQuery2 = "SELECT * FROM "+FEED_TABLE_NAME+ " WHERE username = " + "'" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            HashMap<String,String> hashMap = new HashMap<>();

//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_USERID));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_USERNAME));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_ACCESSTOKEN));
//            Log.d("", "checkLoginDatas : " + cursor.getColumnIndex(COLUMN_DATETIME));

            Log.d("", "COLUMN_CAPTION : " + cursor.getString(cursor.getColumnIndex(COLUMN_CAPTION)));
            Log.d("", "COLUMN_MEDIA : " + cursor.getString(cursor.getColumnIndex(COLUMN_MEDIA)));
            Log.d("", "COLUMN_MEDIATYPE : " + cursor.getString(cursor.getColumnIndex(COLUMN_MEDIATYPE)));
            Log.d("", "COLUMN_THUMNAIL : " + cursor.getString(cursor.getColumnIndex(COLUMN_THUMNAIL)));

            hashMap.put(COLUMN_CAPTION, cursor.getString(cursor.getColumnIndex(COLUMN_CAPTION)));
            hashMap.put(COLUMN_MEDIA, cursor.getString(cursor.getColumnIndex(COLUMN_MEDIA)));
            hashMap.put(COLUMN_MEDIATYPE, cursor.getString(cursor.getColumnIndex(COLUMN_MEDIATYPE)));
            hashMap.put(COLUMN_THUMNAIL, cursor.getString(cursor.getColumnIndex(COLUMN_THUMNAIL)));
            hashMap.put(COLUMN_PERMALINK, cursor.getString(cursor.getColumnIndex(COLUMN_PERMALINK)));
            hashMap.put(COLUMN_TIMESTAMP, cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
            hashMap.put(COLUMN_USERNAME, cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            hashMap.put(COLUMN_USERID, cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            hashMap.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            hashMap.put(COLUMN_DATETIME, cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME)));


            // Adding contact to list
            contactList.add(hashMap);
        }

        // return contact list
        return contactList;
    }


    public void insertUsetInfoDatas(String userid, String username, String access_token, String datetime) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USERID, userid);
            cv.put(COLUMN_USERNAME, username);
            cv.put(COLUMN_ACCESSTOKEN, access_token);
            cv.put(COLUMN_DATETIME, datetime);

            Log.d("", "SQL insert - " + cv);

            db.insert("userinfoTb", null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void insertFeedDatas(String caption, String media_url, String media_type, String thumbnail_url, String permalink, String timestamp, String username, String userid, String id, String datetime) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CAPTION, caption);
            cv.put(COLUMN_MEDIA, media_url);
            cv.put(COLUMN_MEDIATYPE, media_type);
            cv.put(COLUMN_THUMNAIL, thumbnail_url);
            cv.put(COLUMN_PERMALINK, permalink);
            cv.put(COLUMN_TIMESTAMP, timestamp);
            cv.put(COLUMN_USERNAME, username);
            cv.put(COLUMN_USERID, userid);
            cv.put(COLUMN_ID, id);
            cv.put(COLUMN_DATETIME, datetime);

            Log.d("", "SQL insert - " + cv);

            db.insert("feedTb", null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d("", "SQL insert - " + "이미 존재 합니다.");
        } finally {
            db.endTransaction();
        }
        db.close();
    }


    public void insertAllDatas(ArrayList<HashMap<String, String>> datas) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < datas.size(); i++) {
                HashMap<String, String> tempMap = datas.get(i);
                ContentValues cv = new ContentValues();
                cv.put(TAG_Time, tempMap.get(TAG_Time));
                cv.put(TAG_LastTime, tempMap.get(TAG_LastTime));
                cv.put(TAG_DistDif, tempMap.get(TAG_DistDif));
                cv.put(TAG_CheckTime, tempMap.get(TAG_CheckTime));
                db.insert("imageTb", null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public ArrayList<HashMap<String, String>> getAllContacts(String rentalTime, String returnTime) {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TAG_TABLE_NAME;

        SimpleDateFormat format1 = new SimpleDateFormat( "HH:mm:ss");
        //String time1 = "01";
        String time2 = "2019/12/08 02:00:00";
        String time3 = "2019/12/08 02:40:00";


        //String selectQuery = "SELECT * FROM " + TAG_TABLE_NAME +  " WHERE " + "Time" +  " BETWEEN " + time2 + " AND " + time3;
//        String selectQuery1 = "SELECT * FROM getaccel2 WHERE Time BETWEEN " + '"' + rentalTime + '"' + " AND " + '"' +returnTime+'"' ;
//        String selectQuery1 = "SELECT * FROM imageTb WHERE Time BETWEEN " + '"' + rentalTime + '"' + " AND " + '"' +returnTime+'"' ;
        String selectQuery2 = "SELECT * FROM imageTb WHERE Time BETWEEN " + "'" + rentalTime + "'" + " AND " + "'" +returnTime+"'" ;

        SQLiteDatabase db = this.getWritableDatabase();
//        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            HashMap<String,String> hashMap = new HashMap<>();

            Log.d("", "1 : " + cursor.getColumnIndex(TAG_Time));
            Log.d("", "2 : " + cursor.getColumnIndex(TAG_LastTime));
            Log.d("", "3 : " + cursor.getColumnIndex(TAG_DistDif));
            Log.d("", "4 : " + cursor.getColumnIndex(TAG_CheckTime));

//            int time_1 = cursor.getColumnIndex(TAG_Time);


            hashMap.put(TAG_Time, cursor.getString(cursor.getColumnIndex(TAG_Time)));
            hashMap.put(TAG_LastTime, cursor.getString(cursor.getColumnIndex(TAG_LastTime)));
            hashMap.put(TAG_DistDif, cursor.getString(cursor.getColumnIndex(TAG_DistDif)));
            hashMap.put(TAG_CheckTime, cursor.getString(cursor.getColumnIndex(TAG_CheckTime)));
            hashMap.put(TAG_Lati, cursor.getString(cursor.getColumnIndex(TAG_Lati)));
            hashMap.put(TAG_Longti, cursor.getString(cursor.getColumnIndex(TAG_Longti)));

//            hashMap.put(TAG_Time, cursor.getString(0));
//            hashMap.put(TAG_LastTime, cursor.getString(1));
//            hashMap.put(TAG_DistDif, cursor.getString(2));
//            hashMap.put(TAG_CheckTime, cursor.getString(3));
//            HashMap<String, String> tempMap = datas.get(i);

//            ContentValues cv = new ContentValues();
//            cv.put(TAG_Time, tempMap.get(TAG_Time));
//            cv.put(TAG_LastTime, tempMap.get(TAG_LastTime));
//            cv.put(TAG_DistDif, tempMap.get(TAG_DistDif));
//            cv.put(TAG_CheckTime, tempMap.get(TAG_CheckTime));
//            db.insert("imageTb", null, cv);

            // Adding contact to list
            contactList.add(hashMap);
        }

        // return contact list
        return contactList;
    }
}