/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package hashbird.com.newapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "patient";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_DOB = "dob";
	private static final String KEY_DIAB = "diab";
	private static final String KEY_BP = "bp";
	private static final String KEY_HR = "hr";
	private static final String KEY_EP = "ep";
	private static final String KEY_TB = "tb";
	private static final String KEY_DI = "di";
	String imei;
	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		final TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 imei = telephonyManager.getDeviceId();
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT," + KEY_PHONE + " TEXT,"
				+ KEY_DOB + " TEXT," + KEY_DIAB + " TEXT,"+ KEY_BP + " TEXT,"+ KEY_HR + " TEXT,"+ KEY_EP + " TEXT,"+ KEY_TB + " TEXT,"+ KEY_DI + " TEXT "  + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

//	public void  verifyuser(String status,String phone){
//		SQLiteDatabase db = this.getWritableDatabase();
//db.execSQL("UPDATE "+TABLE_USER+" SET "+KEY_UPDATED_AT+" = "+status+" WHERE "+KEY_CREATED_AT+" = "+phone);
//db.close();
//	}



//	public void  update(String status,String phone,String name,String email,String password,String uid){
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues newValues = new ContentValues();
//		newValues.put(KEY_UPDATED_AT, status);
//		newValues.put(KEY_CREATED_AT,phone);
//		newValues.put(KEY_NAME,name);
//		newValues.put(KEY_EMAIL,email);
//		newValues.put(KEY_PASS,password);
//		//db.execSQL("UPDATE "+TABLE_USER+" SET "+KEY_UPDATED_AT+" = "+status+" , "+KEY_CREATED_AT+" = "+phone+" , " +KEY_NAME+" =  "+name+" , "+KEY_EMAIL+" = "+email+" , "+KEY_PASS+" = "+password+" WHERE "+ KEY_UID+" = "+uid);
//
//		db.update(TABLE_USER, newValues, KEY_ID+" = 1", null);
//		db.close();
//
//	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String phone, String dob, String diab, String bp, String hr, String ep, String tb, String di) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_PHONE, phone); // Email
		values.put(KEY_DOB, dob); // Created At
		values.put(KEY_DIAB, diab); // Created At
		values.put(KEY_BP, bp); // Created At
		values.put(KEY_HR, hr); // Created At
		values.put(KEY_EP, ep); // Created At
		values.put(KEY_TB, tb); // Created At
		values.put(KEY_DI, di); // Created At


		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
//	public HashMap<String, String> getUserDetails() {
//		HashMap<String, String> user = new HashMap<String, String>();
//		String selectQuery = "SELECT  * FROM " + TABLE_USER;
//
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		// Move to first row
//		cursor.moveToFirst();
//		if (cursor.getCount() > 0) {
//			user.put("name", cursor.getString(1));
//			user.put("email", cursor.getString(2));
//			user.put("phone", cursor.getString(3));
//			user.put("created_at", cursor.getString(4));
//			user.put("updated_at", cursor.getString(5));
//			user.put("password", cursor.getString(6));
//		}
//		cursor.close();
//		db.close();
//		// return user
//		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
//
//		return user;
//	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}
	public int getProfilesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}

	public List<DataModel> getdata(){
		Security security= new Security();
		// DataModel dataModel = new DataModel();
		List<DataModel> data=new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_USER+" ;",null);
		StringBuffer stringBuffer = new StringBuffer();
		DataModel dataModel = null;
		while (cursor.moveToNext()) {
			dataModel= new DataModel();
			//String name = null;
			try {
				String name = new String(security.decrypt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),imei));
				String email = new String(security.decrypt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),imei));
				String phone = new String(security.decrypt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)),imei));
				String dob =  new String(security.decrypt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DOB)),imei));
				dataModel.setname(name);
				dataModel.setemail(email);
				dataModel.setphone(phone);
				dataModel.setdob(dob);
				stringBuffer.append(dataModel);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// stringBuffer.append(dataModel);
			data.add(dataModel);
		}

		for (DataModel mo:data ) {

			Log.i("Hellomo",""+mo.getname());
		}

		//

		return data;
	}
	public Cursor gettt(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM "+TABLE_USER, null);

		return res;
	}
}
