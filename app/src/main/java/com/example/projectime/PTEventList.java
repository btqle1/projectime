package com.example.projectime;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PTEventList extends AppCompatActivity {
    public static final String EXTRA_TAB_ID = "EXTRA_TAB_ID";
    public static final String EXTRA_CALENDAR_ID = "EXTRA_CALENDAR_ID";
    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private SQLiteDatabase db;
    private Cursor cursor;
    private static long calendarID;
    private static long tabID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);


        ListView eventListView = (ListView) findViewById(R.id.event_list);

        Intent intent = getIntent();
        tabID = intent.getLongExtra(PTTabList.EXTRA_TAB_ID, -1);
        calendarID = intent.getLongExtra(PTTabList.EXTRA_CALENDAR_ID, -1);
        try{
            SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query("EVENT",
                    new String[]{"_id", "CALENDAR_ID", "TAB_ID", "NAME", "TIME", "TIMEISKNOWN"},
                    "TAB_ID = ? AND CALENDAR_ID = ?",
                    new String[]{String.valueOf(tabID), String.valueOf(calendarID)},
                    null, null, null);

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            cursor.moveToFirst();

            eventListView.setAdapter(cursorAdapter);

            eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Cursor cursor = ((SimpleCursorAdapter) adapterView.getAdapter()).getCursor();
                    cursor.moveToPosition(position);
                    long eventId = cursor.getLong(cursor.getColumnIndex("_id"));

                    Intent intent = new Intent(PTEventList.this, PTEventEdit.class);
                    intent.putExtra(EXTRA_EVENT_ID, eventId-1);
                    intent.putExtra(EXTRA_TAB_ID, tabID);
                    intent.putExtra(EXTRA_CALENDAR_ID, calendarID);
                    startActivity(intent);
                }
            });
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}