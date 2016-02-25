package com.gaur.hiker.library;
 
import java.util.ArrayList;
import java.util.HashMap;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // Login table name
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_SEARCHES = "searches";
    private static final String TABLE_SRESULTS = "results";
    private static final String TABLE_BM = "bm";
    private static final String TABLE_NOTES = "notes";
 
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UNAME = "uname";
    private static final String KEY_PASS = "password";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String SEARCH_SID = "sid";
    private static final String SEARCH_NAME = "name";
    private static final String SEARCH_SNAME = "sname";
    private static final String SEARCH_SNUM = "snum";
    private static final String SEARCH_SUB = "suburb";
    private static final String SEARCH_PHONE = "phone";
    private static final String SEARCH_PAGE = "page";
    private static final String RESULTS_SID = "sid";
    private static final String RESULTS_RESID = "resid";
    private static final String RESULTS_MUN = "munc";
    private static final String RESULTS_UNUM = "unitno";
    private static final String RESULTS_SNUM = "snum";
    private static final String RESULTS_SNAME = "sname";
    private static final String RESULTS_SUBURB = "suburb";
    private static final String RESULTS_PCODE = "pcode";
    private static final String RESULTS_OINFO = "oinfo";
    private static final String RESULTS_PHONE = "phone";
    private static final String BM_UID = "uid";
    private static final String BM_RID = "resid";
    private static final String NOTES_UID = "uid";
    private static final String NOTES_RID = "resid";
    private static final String NOTES_NOTE = "note";
    private static final String NOTES_TIME = "created_at";
  
    private static DatabaseHandler sInstance;

    public static DatabaseHandler getInstance(Context context) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (sInstance == null) {
	      sInstance = new DatabaseHandler(context.getApplicationContext());
	    }
	    return sInstance;
	}

    /**
    * Constructor should be private to prevent direct instantiation.
    * make call to static factory method "getInstance()" instead.
    */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
      
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UNAME + " TEXT UNIQUE,"
                + KEY_PASS + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_SEARCHES_TABLE = "CREATE TABLE " + TABLE_SEARCHES + "("
                + SEARCH_SID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SEARCH_NAME + " TEXT,"
                + SEARCH_SNAME + " TEXT,"
                + SEARCH_SNUM + " TEXT,"
                + SEARCH_SUB + " TEXT,"
                + SEARCH_PHONE + " TEXT,"
                + SEARCH_PAGE + " INT" + ")";
        db.execSQL(CREATE_SEARCHES_TABLE);
        String CREATE_SRESULTS_TABLE = "CREATE TABLE " + TABLE_SRESULTS + "("
                + RESULTS_SID + " INTEGER,"
                + RESULTS_RESID + " TEXT PRIMARY KEY,"
                + RESULTS_MUN + " TEXT,"
                + RESULTS_UNUM + " TEXT,"
                + RESULTS_SNUM + " TEXT,"
                + RESULTS_SNAME + " TEXT,"
                + RESULTS_SUBURB + " TEXT,"
                + RESULTS_PCODE + " TEXT,"
                + RESULTS_OINFO + " TEXT,"
                + RESULTS_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_SRESULTS_TABLE);
        String CREATE_BM_TABLE = "CREATE TABLE " + TABLE_BM + "("
                + BM_UID + " TEXT,"
                + BM_RID + " TEXT" + ")";
        db.execSQL(CREATE_BM_TABLE);
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + NOTES_UID + " TEXT,"
                + NOTES_RID + " TEXT,"
                + NOTES_NOTE + " TEXT,"
                + NOTES_TIME + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SRESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Storing user details in database
     * */
    public void addUser(String uname, String password, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_UNAME, uname); // User Name
        values.put(KEY_PASS, password); // Password 
        values.put(KEY_UID, uid); // id
        values.put(KEY_CREATED_AT, created_at); // Created At
 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
    }
    
    public boolean addbm(String uid, String resid) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(BM_UID, uid); // User Name
        values.put(BM_RID, resid); // Password 
 
        try {
            // Inserting Row
            db.insert(TABLE_BM, null, values);
            return true;
    	} catch (SQLException e) {
    	    return false;
    	}
    }
    
    public boolean addnote(String uid, String resid, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean ne = this.note_exists(uid, resid);
        ContentValues values = new ContentValues();
        values.put(NOTES_UID, uid); // User Name
        values.put(NOTES_RID, resid); // Password 
        values.put(NOTES_NOTE, note); // Password 

        if(ne){
            try {
                // Inserting Row
            	String[] whereArgs = new String[]{uid,resid};
                db.update(TABLE_NOTES, values, NOTES_UID+"=? AND "+NOTES_RID+"=?" , whereArgs);
                return true;
        	} catch (SQLException e) {
        	    return false;
        	}        	        	
        } else{
            try {
                // Inserting Row
                db.insert(TABLE_NOTES, null, values);
                return true;
        	} catch (SQLException e) {
        	    return false;
        	}        	
        }
    }
    
    /**
     * Storing user details in database
     * */
    public boolean addSearch(String name, String sname, String snum, String sub, String phone, Integer page) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        int searchcount = getSearchCount();
        if(searchcount>9){
        	deleteSearch();
        }
 
        ContentValues values = new ContentValues();
        values.put(SEARCH_NAME, name); // name
        values.put(SEARCH_SNAME, sname); // street name
        values.put(SEARCH_SNUM, snum); // street number
        values.put(SEARCH_SUB, sub); // suburb
        values.put(SEARCH_PHONE, phone); // phone
        values.put(SEARCH_PAGE, page); // page
 
        try {
            // Inserting Row
            db.insert(TABLE_SEARCHES, null, values);
            return true;
    	} catch (SQLException e) {
    	    return false;
    	}
    }
         
    /**
     * Storing user details in database
     * */
    public boolean addResults(Integer sid, String resid, String mun, String unum, String sname, String snum, String sub, String pcode, String oinfo, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        int searchcount = getSearchCount();
        if(searchcount>29){
        	deleteSearch();
        }
 
        ContentValues values = new ContentValues();
        values.put(RESULTS_SID, sid); 
        values.put(RESULTS_RESID, resid);
        values.put(RESULTS_MUN, mun);
        values.put(RESULTS_UNUM, unum);
        values.put(RESULTS_SNAME, sname);
        values.put(RESULTS_SNUM, snum);
        values.put(RESULTS_SUBURB, sub);
        values.put(RESULTS_PCODE, pcode);
        values.put(RESULTS_OINFO, oinfo);
        values.put(RESULTS_PHONE, phone);
 
        try {
            // Inserting Row
            db.insert(TABLE_SRESULTS, null, values);
            return true;
    	} catch (SQLException e) {
    	    return false;
    	}
    }
         
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        // return user
        return user;
    }

    public ArrayList<HashMap<String, String>> getResults(Integer sid){
        ArrayList<HashMap<String, String>> respList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_SRESULTS + " WHERE " + RESULTS_SID +" = '"+sid+"'";
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> resp = new HashMap<String,String>();
                resp.put("StreetNum", cursor.getString(cursor.getColumnIndex(RESULTS_SNUM)));
                resp.put("UnitNo", cursor.getString(cursor.getColumnIndex(RESULTS_UNUM)));
                resp.put("Phone", cursor.getString(cursor.getColumnIndex(RESULTS_PHONE)));
                resp.put("ResID", cursor.getString(cursor.getColumnIndex(RESULTS_RESID)));
                respList.add(resp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return user
        return respList;
    }
 
    public ArrayList<HashMap<String, String>> getNotes(String uid){
        ArrayList<HashMap<String, String>> respList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " WHERE " + NOTES_UID +" = '"+uid+"'";
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String recid = "";
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> resp = new HashMap<String,String>();
                HashMap<String,String> moreinfo = new HashMap<String,String>();
                recid = cursor.getString(cursor.getColumnIndex(NOTES_RID));
                resp.put("recid", recid);
                moreinfo = this.getinfo(recid);
                resp.put("unum", moreinfo.get("unum"));
                resp.put("snum", moreinfo.get("snum"));
                resp.put("phone", moreinfo.get("phone"));
                resp.put("sname", moreinfo.get("sname"));
                resp.put("note", cursor.getString(cursor.getColumnIndex(NOTES_NOTE)));
                respList.add(resp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return user
        return respList;
    }
 
    public String getNote(String uid, String rid){
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " WHERE " + NOTES_UID +" = '"+uid+"' AND "+NOTES_RID+" = '"+rid+"'";
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        String resp = "";
        if (cursor.moveToFirst()) {
            resp = cursor.getString(cursor.getColumnIndex(NOTES_NOTE));
        }
        cursor.close();
        // return user
        return resp;
    }
 
    public ArrayList<String> getbms(String uid){
        ArrayList<String> respList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_BM + " WHERE " + BM_UID +" = '"+uid+"'";
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                respList.add(cursor.getString(cursor.getColumnIndex(BM_RID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return user
        return respList;
    }
 
    public ArrayList<HashMap<String, String>> getRecents(){
        ArrayList<HashMap<String, String>> respList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCHES + " ORDER BY "+SEARCH_SID+" DESC";
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String tempr = "";
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> resp = new HashMap<String,String>();
                resp.put("sid", cursor.getString(cursor.getColumnIndex(SEARCH_SID)));
                resp.put("name", cursor.getString(cursor.getColumnIndex(SEARCH_NAME)));
                resp.put("snum", cursor.getString(cursor.getColumnIndex(SEARCH_SNUM)));
                resp.put("sname", cursor.getString(cursor.getColumnIndex(SEARCH_SNAME)));
                resp.put("sub", cursor.getString(cursor.getColumnIndex(SEARCH_SUB)));
                resp.put("phone", cursor.getString(cursor.getColumnIndex(SEARCH_PHONE)));
                resp.put("page", cursor.getString(cursor.getColumnIndex(SEARCH_PAGE)));
                tempr = resp.get("name")+", "+resp.get("snum")+", "+resp.get("sname")+", "+resp.get("sub")+", "+resp.get("phone")+", "+resp.get("page");
                tempr = tempr.trim().replaceAll("(, )+", ", ");
                resp.put("summ", tempr);
                respList.add(resp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return user
        return respList;
    }
 
    public HashMap<String, String> getUnamePwd(){
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        HashMap<String,String> resp = new HashMap<String,String>();
        resp.put("username", cursor.getString(cursor.getColumnIndex(KEY_UNAME)));
        resp.put("password", cursor.getString(cursor.getColumnIndex(KEY_PASS)));
        resp.put("uid", cursor.getString(cursor.getColumnIndex(KEY_UID)));
        cursor.close();
        // return user
        return resp;
    }
 
    public HashMap<String, String> getinfo(String resid){
        String selectQuery = "SELECT  * FROM " + TABLE_SRESULTS + " WHERE " + RESULTS_RESID + " = '" + resid +"'";
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        HashMap<String,String> resp = new HashMap<String,String>();
        resp.put("rid", resid);
        resp.put("unum", cursor.getString(cursor.getColumnIndex(RESULTS_UNUM)));
        resp.put("munc", cursor.getString(cursor.getColumnIndex(RESULTS_MUN)));
        resp.put("snum", cursor.getString(cursor.getColumnIndex(RESULTS_SNUM)));
        resp.put("sname", cursor.getString(cursor.getColumnIndex(RESULTS_SNAME)));
        resp.put("pcode", cursor.getString(cursor.getColumnIndex(RESULTS_PCODE)));
        resp.put("oinfo", cursor.getString(cursor.getColumnIndex(RESULTS_OINFO)));
        resp.put("suburb", cursor.getString(cursor.getColumnIndex(RESULTS_SUBURB)));
        resp.put("phone", cursor.getString(cursor.getColumnIndex(RESULTS_PHONE)));
        cursor.close();
        // return user
        return resp;
    }
 
    public int searchExists(String name, String sname, String snum, String sub, String phone, Integer page) {
        String searchQuery = "SELECT  "+SEARCH_SID+" FROM " + TABLE_SEARCHES + " WHERE "+SEARCH_NAME+"='"+name+"' AND "+SEARCH_SNAME+"='"+sname+"' AND "+SEARCH_SNUM+"='"+snum+"' AND "+SEARCH_SUB+"='"+sub+"' AND "+SEARCH_PHONE+"='"+phone+"' AND "+SEARCH_PAGE+"='"+page+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(searchQuery, null);
        int rowCount = cursor.getCount();
        
        if(rowCount>0){
            cursor.moveToFirst();
            int resp = cursor.getInt(0);
            cursor.close();
        	return resp;
        } else{
        	return 0;
        }
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
         
        // return row count
        return rowCount;
    }

    public boolean isrecbm(String uid, String rid) {
        String countQuery = "SELECT  * FROM " + TABLE_BM + " WHERE "+BM_UID+" = '"+uid+"' AND "+BM_RID+" = '"+rid+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        if(rowCount>0){
        	return true;
        } else{
        	return false;
        }
    }

    public boolean note_exists(String uid, String rid) {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES + " WHERE "+NOTES_UID+" = '"+uid+"' AND "+NOTES_RID+" = '"+rid+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        if(rowCount>0){
        	return true;
        } else{
        	return false;
        }
    }

    public String getUsername() {
        String countQuery = "SELECT  "+KEY_UNAME+" FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        String name = "";
        int rowCount = cursor.getCount();
        
        if(rowCount>0){
	        cursor.moveToFirst();
	        name = cursor.getString(0);
	    }
        cursor.close();
        
    	return name;
    }

    public int getSearchCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SEARCHES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
         
        // return row count
        return rowCount;
    }
     
    public int minSearchId() {
        String minQuery = "SELECT min("+SEARCH_SID+") FROM "+TABLE_SEARCHES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(minQuery, null);
        cursor.moveToFirst();
        Integer resp =cursor.getInt(0);
        cursor.close();

        return  resp;
    }
     
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
    }

    public void deleteSearch(){
    	Integer deleteId = minSearchId();
        String deleteQuery = "DELETE FROM "+TABLE_SEARCHES+" WHERE "+SEARCH_SID+" = ("+deleteId+")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
    }

    public boolean removebm(String uid, String resid){
        String deleteQuery = "DELETE FROM "+TABLE_BM+" WHERE "+BM_UID+" = '"+uid+"' AND "+BM_RID+" = '"+resid+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        Boolean is_still = this.isrecbm(uid, resid);
        return !is_still;
    }

    public boolean removenote(String uid, String resid){
        String deleteQuery = "DELETE FROM "+TABLE_NOTES+" WHERE "+NOTES_UID+" = '"+uid+"' AND "+NOTES_RID+" = '"+resid+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
        	db.execSQL(deleteQuery);
            return true;
    	} catch (SQLException e) {
    	    return false;
    	}
    }

}