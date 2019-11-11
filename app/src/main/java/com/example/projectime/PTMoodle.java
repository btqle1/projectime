package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class PTMoodle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptmoodle);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onLogin(View view){
        SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Random rng = new Random();

        ContentValues cv = new ContentValues();
        cv.put("NAME", "CSCI"+(rng.nextInt(9000)+1000));
        cv.put("COLOR", rng.nextInt(1<<24));

        db.insert("CALENDAR",null,cv);

        db.close();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
