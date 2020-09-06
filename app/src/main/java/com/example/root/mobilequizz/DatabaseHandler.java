package com.example.root.mobilequizz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Quiz";
    private static final String TABLE_NAME = "Questions";
    private static final String Key_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_OP1 = "op1";
    private static final String KEY_OP2 = "op2";
    private static final String KEY_OP3 = "op3";
    private static final String KEY_OP4 = "op4";
    private static final String KEY_ANSWER = "answer";
    private static final String[] COLUMNS = { Key_ID, KEY_QUESTION, KEY_OP1 ,KEY_OP2 ,
            KEY_OP3 , KEY_OP4 , KEY_ANSWER};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATION_TABLE = "CREATE TABLE Questions ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "question TEXT, "
                + "op1 TEXT, " + "op2 TEXT, " + "op3 TEXT, " + "op4 TEXT, "+"answer TEXT)";

        sqLiteDatabase.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public Question getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Question question = new Question();
        question.id = cursor.getString(0);
        question.question = cursor.getString(1);
        question.op1 = cursor.getString(2);
        question.op2 = cursor.getString(3);
        question.op3 = cursor.getString(4);
        question.op4 = cursor.getString(5);
        question.answer = cursor.getString(6);
        db.close();
        return question;
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.question);
        values.put(KEY_OP1, question.op1);
        values.put(KEY_OP2, question.op2);
        values.put(KEY_OP3, question.op3);
        values.put(KEY_OP4, question.op4);
        values.put(KEY_ANSWER,question.answer);
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public long getQuestionTotal(){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        db.close();
        return count;
    }

    public void updateQuestion(Question question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.question);
        values.put(KEY_OP1, question.op1);
        values.put(KEY_OP2, question.op2);
        values.put(KEY_OP3, question.op3);
        values.put(KEY_OP4, question.op4);
        values.put(KEY_ANSWER,question.answer);
        // insert
        db.update(TABLE_NAME, values, "id="+question.id,null);
        db.close();
    }
}