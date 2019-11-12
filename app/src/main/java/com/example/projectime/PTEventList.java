package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PTEventList extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView eventListView = (ListView)findViewById(R.id.event_list);

        Intent intent = getIntent();
        long calendarId = intent.getLongExtra(MainActivity.EXTRA_CALENDAR_ID, -1);

        SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.query("EVENT", new String[] {"_id", "NAME"}, "CALENDAR_ID = ?", new String[] {String.valueOf(calendarId)}, null, null, null);

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] {"NAME"},
                new int[] {android.R.id.text1},
                0);

        eventListView.setAdapter(cursorAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((SimpleCursorAdapter)adapterView.getAdapter()).getCursor();
                cursor.moveToPosition(position);
                long tabId = cursor.getLong(cursor.getColumnIndex("_id"));
                Intent intent = new Intent(PTEventList.this, PTEventList.class);
                intent.putExtra(EXTRA_EVENT_ID, tabId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

}
