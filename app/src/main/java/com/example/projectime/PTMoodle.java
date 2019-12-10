package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import java.util.Random;

public class PTMoodle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptmoodle);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void onLogin(View view){
        SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Random rng = new Random();

        ContentValues cv = new ContentValues();
        cv.put("NAME", "CSCI"+(rng.nextInt(9000)+1000));
        cv.put("COLOR", rng.nextInt(1<<24));

        long calendarId = db.insert("CALENDAR",null,cv);

        cv = new ContentValues();
        cv.put("CALENDAR_ID", calendarId);
        cv.put("NAME", "Sample Tab");

        long tabId = db.insert("TAB", null, cv);

        long time = System.currentTimeMillis() / 1000L + 3600L;

        cv = new ContentValues();
        cv.put("TAB_ID", tabId);
        cv.put("NAME", "Sample Event");
        cv.put("TIME", time);
        cv.put("URI", "https://reddit.com/");

        long eventId = db.insert("EVENT",null,cv);

        db.close();


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
