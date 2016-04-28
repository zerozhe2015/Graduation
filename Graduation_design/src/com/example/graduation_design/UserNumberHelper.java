package com.example.graduation_design;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class UserNumberHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "graduationdesign.db";
	private static final int DATABASE_VERSION = 1;  
	
	
	public UserNumberHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public UserNumberHelper(Context context, String name, CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}
	
	public UserNumberHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		
/*--------------------------------------------------------------------------------------------------------------------------*/
		
		

		
		 
			//创建全部数据表
		db.execSQL("create table alldata("
				+ "userId varchar(20),"
				+ "pm varchar(20),"
				+ "comprehensive varchar(20),"
				+ "temperature varchar(20),"
				+ "humidity varchar(20),"
				+ "wind1 varchar(20),"
				+ "wind2 varchar(20),"
				+ "time varchar(20)"
				+ ");");
		
		
	
		
	
	}

	
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

