package com.kbpluse.atendance.atendace.Activity.AddTasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kbpluse.atendance.atendace.Activity.AddTasks.dataModel.AddTasksDataModel;

import java.util.ArrayList;

public class AddTasksDatabaseSource extends DatabaseHelper {
    private static final String TABLE_NAME_TASKS_DETAILS = "tasks_details";
    private static final String COLUMN_TASKS_ID = "tasks_id";
    private static final String COLUMN_TASKS_TITLE = "tasks_title";
    private static final String COLUMN_DECRIPTION_MESSAGE = "dec_message";

    private String TAG = AddTasksDatabaseSource.class.getSimpleName();

    private final String[] allColumns = {COLUMN_TASKS_ID, COLUMN_TASKS_TITLE, COLUMN_DECRIPTION_MESSAGE};

    public static final String CREATE_TABLE_MESSAGE = " CREATE TABLE "
            + TABLE_NAME_TASKS_DETAILS
            + " ("
            + COLUMN_TASKS_ID
            + " INTEGER PRIMARY KEY, "
            + COLUMN_TASKS_TITLE
            + " TEXT NOT NULL, "
            + COLUMN_DECRIPTION_MESSAGE
            + " TEXT NOT NULL"
            + " );";
    private SQLiteDatabase database;

    public AddTasksDatabaseSource(Context context) {
        super(context);
    }

    public void insertNote(AddTasksDataModel addTasksDataModel) {
        Log.e(TAG, "insertNote: " + addTasksDataModel.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASKS_TITLE, addTasksDataModel.getTasksName());
        contentValues.put(COLUMN_DECRIPTION_MESSAGE, addTasksDataModel.getTasksDecription());
        database = getWritableDatabase();
        long insertedId = database.insert(TABLE_NAME_TASKS_DETAILS, null, contentValues);
        Log.e(TAG, "insertNote: " + insertedId);
        database.close();
    }

    public ArrayList<AddTasksDataModel> getAllNotes() {
        Log.e(TAG, "getAllNotes: ");
        ArrayList<AddTasksDataModel> messageDataModelArrayList = new ArrayList<>();
        database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME_TASKS_DETAILS, allColumns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            messageDataModelArrayList.add(cursorToModel(cursor));
        }
        cursor.close();
        database.close();
        return messageDataModelArrayList;
    }

    private AddTasksDataModel cursorToModel(Cursor cursor) {
        Log.e(TAG, "cursorToModel: ");
        AddTasksDataModel addTasksDataModel = new AddTasksDataModel();
        addTasksDataModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_ID)));
        addTasksDataModel.setTasksName(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_TITLE)));
        addTasksDataModel.setTasksDecription(cursor.getString(cursor.getColumnIndex(COLUMN_DECRIPTION_MESSAGE)));
        return addTasksDataModel;
    }

    public void updateNote(AddTasksDataModel addTasksDataModel) {
        Log.e(TAG, "updateNote: " + addTasksDataModel.toString());
        String whereClause = COLUMN_TASKS_ID + " = ?";
        String[] whereArgs = {String.valueOf(addTasksDataModel.getId())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASKS_TITLE, addTasksDataModel.getTasksName());
        contentValues.put(COLUMN_DECRIPTION_MESSAGE, addTasksDataModel.getTasksDecription());
        database = getWritableDatabase();
        long updatedId = database.update(TABLE_NAME_TASKS_DETAILS, contentValues, whereClause, whereArgs);
        Log.e(TAG, "updateNote: " + updatedId);
        database.close();
    }
}
