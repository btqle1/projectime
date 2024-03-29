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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PTTabList extends AppCompatActivity {
    public static final String EXTRA_TAB_ID = "EXTRA_TAB_ID";
    public static final String EXTRA_CALENDAR_ID = "EXTRA_CALENDAR_ID";

    private SQLiteDatabase db;
    private Cursor cursor;
    private static long calendarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pttab_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        ListView tabListView = (ListView)findViewById(R.id.tab_list);

        Intent intent = getIntent();
        calendarID = intent.getLongExtra(MainActivity.EXTRA_CALENDAR_ID, -1);

        try {
            SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();

            cursor = db.query("TAB",
                    new String[] {"_id", "CALENDAR_ID", "NAME"},
                    "CALENDAR_ID = ?",
                    new String[] { String.valueOf(calendarID) },
                    null, null, null);

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"NAME"},
                    new int[] {android.R.id.text1},
                    0);

            tabListView.setAdapter(cursorAdapter);

            tabListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Cursor cursor = ((SimpleCursorAdapter)adapterView.getAdapter()).getCursor();
                    cursor.moveToPosition(position);
                    long tabId = cursor.getLong(cursor.getColumnIndex("_id"));

                    Intent intent = new Intent(PTTabList.this, PTEventList.class);
                    intent.putExtra(EXTRA_TAB_ID, tabId);
                    intent.putExtra(EXTRA_CALENDAR_ID, PTTabList.calendarID);
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
}
