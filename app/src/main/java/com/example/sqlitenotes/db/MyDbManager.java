package com.example.sqlitenotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitenotes.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private final MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb() {
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String title, String desc) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESC, desc);
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public void updateItem(String title, String desc, int id) {
        String selection = MyConstants._ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESC, desc);
        db.update(MyConstants.TABLE_NAME, cv, selection, null);
    }

    public void delete(int id) {
        String selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME, selection, null);
    }

    public void getFromDb(String searchText, OnDataReceived onDataReceived) {
        final List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstants.TITLE + " like ?";
        final Cursor cursor = db.query(MyConstants.TABLE_NAME, null, selection,
                new String[]{"%" + searchText + "%"}, null, null, null);

        while (cursor.moveToNext()) {
            ListItem item = new ListItem();
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(MyConstants.DESC));
            int _id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            item.setTitle(title);
            item.setDesc(desc);
            item.setId(_id);
            tempList.add(item);

        }
        cursor.close();
        onDataReceived.onReceived(tempList);
    }

    public void closeDb() {
        myDbHelper.close();
    }

}
