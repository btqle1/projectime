package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectime.moodle.DataPopulateTask;
import com.example.projectime.moodle.DataSource;

import java.io.IOException;
import java.util.ArrayList;
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
        EditText usernameEditText = findViewById(R.id.edit_text_username);
        EditText passwordEditText = findViewById(R.id.edit_text_password);
        EditText instanceUrlEditText = findViewById(R.id.edit_text_instance_url);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String instanceUrl = instanceUrlEditText.getText().toString();

        DataPopulateTask dpt = new DataPopulateTask(this);
        dpt.execute(username, password, instanceUrl);
    }

    public void addDatabaseEvents(ArrayList<Calendar> calendars) {
        SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Random rng = new Random();

        ContentValues cv = new ContentValues();
        ContentValues cv2 = new ContentValues();
        ContentValues cv3 = new ContentValues();


        for(int i = 0; i < calendars.size(); i++) {
            Calendar calendar = calendars.get(i);

            cv.put("_id", i);
            cv.put("NAME", calendar.getName());
            cv.put("COLOR", rng.nextInt(1 << 24));

            long calendarId = i;
            db.insert("CALENDAR", null, cv);

            ArrayList<Tab> tabs = calendar.getTabs();
            for(int j = 0; j < tabs.size(); j++) {
                Tab tab = tabs.get(j);

                cv2 = new ContentValues();
                cv2.put("_id", j);
                cv2.put("CALENDAR_ID", calendarId);
                cv2.put("NAME", tab.getName());

                long tabId = j;
                db.insert("TAB", null, cv2);
                Log.i("info", "New tab added");

                ArrayList<Event> events = tab.getEvents();
                for(int k = 0; k < events.size(); k++) {
                    Event event = events.get(k);
                    long time = System.currentTimeMillis() / 1000L + 3600L;

                    cv3 = new ContentValues();
                    cv3.put("_id", k);
                    cv3.put("CALENDAR_ID", calendarId);
                    cv3.put("TAB_ID", tabId);
                    cv3.put("NAME", event.getName());
                    cv3.put("TIME", time);
                    cv3.put("URI", event.getUrl());

                    Log.i("info", "New event added");
                    long eventId = db.insert("EVENT", null, cv3);
                }
            }
        }
        db.close();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
