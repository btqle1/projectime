package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PTEventAdd extends AppCompatActivity {
    private SQLiteDatabase db;
    //private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_add);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onAddEvent(View view){
        EditText eventNameField = (EditText) findViewById(R.id.edittext_add_event_name);
        String eventName = eventNameField.getText().toString();
        try{
            SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("NAME", eventName);
            db.insert("EVENT", null, cv);

        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        db.close();

        Intent intent = new Intent(this, PTEventList.class);
        startActivity(intent);
    }
}
