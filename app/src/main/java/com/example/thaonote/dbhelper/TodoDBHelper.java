package com.example.thaonote.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thaonote.model.CompletedModel;
import com.example.thaonote.model.PendingModel;

import java.util.ArrayList;


public class TodoDBHelper {
    private Context context;
    private DatabaseHelper databaseHelper;

    public TodoDBHelper(Context context){
        this.context=context;
        databaseHelper=new DatabaseHelper(context);
    }

    //add new todos into the database
    public boolean addNewTodo(PendingModel pendingModel){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.COL_TODO_TITLE, pendingModel.getTodoTitle());
        contentValues.put(DatabaseHelper.COL_TODO_CONTENT, pendingModel.getTodoContent());
        contentValues.put(DatabaseHelper.COL_TODO_TAG, pendingModel.getTodoTag());
        contentValues.put(DatabaseHelper.COL_TODO_DATE, pendingModel.getTodoDate());
        contentValues.put(DatabaseHelper.COL_TODO_TIME, pendingModel.getTodoTime());
        contentValues.put(DatabaseHelper.COL_TODO_STATUS,DatabaseHelper.COL_DEFAULT_STATUS);
        sqLiteDatabase.insert(DatabaseHelper.TABLE_TODO_NAME,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }

    //count todos from the database
    public int countTodos(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String count="SELECT " + DatabaseHelper.COL_TODO_ID + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_STATUS+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(count,new String[]{DatabaseHelper.COL_DEFAULT_STATUS});
        return cursor.getCount();
    }

    //count completed todos from the database
    public int countCompletedTodos(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String count="SELECT " + DatabaseHelper.COL_TODO_ID + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_STATUS+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(count,new String[]{DatabaseHelper.COL_STATUS_COMPLETED});
        return cursor.getCount();
    }

    //fetch all the todos from the database
    public ArrayList<PendingModel> fetchAllTodos(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        ArrayList<PendingModel> pendingModels =new ArrayList<>();
        String query="SELECT * FROM " + DatabaseHelper.TABLE_TODO_NAME+" INNER JOIN " + DatabaseHelper.TABLE_TAG_NAME+" ON " + DatabaseHelper.TABLE_TODO_NAME+"."+DatabaseHelper.COL_TODO_TAG+"="+
                DatabaseHelper.TABLE_TAG_NAME+"."+DatabaseHelper.COL_TAG_ID + " WHERE " + DatabaseHelper.COL_TODO_STATUS+"=? ORDER BY " + DatabaseHelper.TABLE_TODO_NAME+"."+DatabaseHelper.COL_TODO_ID + " ASC";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{DatabaseHelper.COL_DEFAULT_STATUS});
        while (cursor.moveToNext()){
            PendingModel pendingModel =new PendingModel();
            pendingModel.setTodoID(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TODO_ID)));
            pendingModel.setTodoTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TITLE)));
            pendingModel.setTodoContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_CONTENT)));
            pendingModel.setTodoTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
            pendingModel.setTodoDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_DATE)));
            pendingModel.setTodoTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TIME)));
            pendingModels.add(pendingModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return pendingModels;
    }

    //fetch all the completed todos from the database
    public ArrayList<CompletedModel> fetchCompletedTodos(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        ArrayList<CompletedModel> completedModels =new ArrayList<>();
        String query="SELECT * FROM " + DatabaseHelper.TABLE_TODO_NAME+" INNER JOIN " + DatabaseHelper.TABLE_TAG_NAME+" ON " + DatabaseHelper.TABLE_TODO_NAME+"."+DatabaseHelper.COL_TODO_TAG+"="+
                DatabaseHelper.TABLE_TAG_NAME+"."+DatabaseHelper.COL_TAG_ID + " WHERE " + DatabaseHelper.COL_TODO_STATUS+"=? ORDER BY " + DatabaseHelper.TABLE_TODO_NAME+"."+DatabaseHelper.COL_TODO_ID + " DESC";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{DatabaseHelper.COL_STATUS_COMPLETED});
        while (cursor.moveToNext()){
            CompletedModel completedModel =new CompletedModel();
            completedModel.setTodoID(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TODO_ID)));
            completedModel.setTodoTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TITLE)));
            completedModel.setTodoContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_CONTENT)));
            completedModel.setTodoTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
            completedModel.setTodoDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_DATE)));
            completedModel.setTodoTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TIME)));
            completedModels.add(completedModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return completedModels;
    }

    //update todos according to the todos id
    public boolean updateTodo(PendingModel pendingModel){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.COL_TODO_TITLE, pendingModel.getTodoTitle());
        contentValues.put(DatabaseHelper.COL_TODO_CONTENT, pendingModel.getTodoContent());
        contentValues.put(DatabaseHelper.COL_TODO_TAG, pendingModel.getTodoTag());
        contentValues.put(DatabaseHelper.COL_TODO_DATE, pendingModel.getTodoDate());
        contentValues.put(DatabaseHelper.COL_TODO_TIME, pendingModel.getTodoTime());
        contentValues.put(DatabaseHelper.COL_TODO_STATUS,DatabaseHelper.COL_DEFAULT_STATUS);
        sqLiteDatabase.update(DatabaseHelper.TABLE_TODO_NAME,contentValues,DatabaseHelper.COL_TODO_ID+"=?",new String[]{String.valueOf(pendingModel.getTodoID())});
        sqLiteDatabase.close();
        return true;
    }

    //make todos completed according to the id
    public boolean makeCompleted(int todoID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.COL_TODO_STATUS,DatabaseHelper.COL_STATUS_COMPLETED);
        sqLiteDatabase.update(DatabaseHelper.TABLE_TODO_NAME,contentValues,DatabaseHelper.COL_TODO_ID+"=?",
                new String[]{String.valueOf(todoID)});
        sqLiteDatabase.close();
        return true;
    }

    //remove todos according to the todos id
    public boolean removeTodo(int todoID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(DatabaseHelper.TABLE_TODO_NAME,DatabaseHelper.COL_TODO_ID+"=?",new String[]{String.valueOf(todoID)});
        sqLiteDatabase.close();
        return true;
    }

    //remove all the completed todos
    public boolean removeCompletedTodos(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(DatabaseHelper.TABLE_TODO_NAME,DatabaseHelper.COL_TODO_STATUS+"=?",new String[]{DatabaseHelper.COL_STATUS_COMPLETED});
        sqLiteDatabase.close();
        return true;
    }

    //fetch todos title from the database according the todos id
    public String fetchTodoTitle(int todoID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_TODO_TITLE + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(todoID)});
        String title="";
        if(cursor.moveToFirst()){
            title=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TITLE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    //fetch todos content from the database according the todos id
    public String fetchTodoContent(int todoID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_TODO_CONTENT + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(todoID)});
        String content="";
        if(cursor.moveToFirst()){
            content=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_CONTENT));
        }
        cursor.close();
        sqLiteDatabase.close();
        return content;
    }

    //fetch todos date from the database according the todos id
    public String fetchTodoDate(int todoID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_TODO_DATE + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(todoID)});
        String date="";
        if(cursor.moveToFirst()){
            date=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_DATE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return date;
    }

    //fetch todos time from the database according the todos id
    public String fetchTodoTime(int todoID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_TODO_TIME + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(todoID)});
        String time="";
        if(cursor.moveToFirst()){
            time=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TIME));
        }
        cursor.close();
        sqLiteDatabase.close();
        return time;
    }
}
