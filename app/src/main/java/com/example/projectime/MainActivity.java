package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/*
MUST add to project structure:
Design
*/

public class MainActivity extends AppCompatActivity {
    private static final String EXTRA_CALENDAR_ID = "EXTRA_CALENDAR_ID";
    private static final String TABLE_CALENDAR = "CALENDAR";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "NAME";
    private Cursor calendarNameCursor;
    private SQLiteDatabase db;
    private int testFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.class_options);
        SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        calendarNameCursor = db.query(TABLE_CALENDAR, new String[] {COLUMN_ID, COLUMN_NAME}, null, null, null, null, COLUMN_NAME);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                calendarNameCursor,
                new String[] {COLUMN_NAME},
                new int[]{android.R.id.text1}, 0);

        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((SimpleCursorAdapter) adapterView.getAdapter()).getCursor();
                cursor.moveToPosition(position);
                long calendarID = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                Intent intent = new Intent(MainActivity.this, PTModules.class);
                intent.putExtra(EXTRA_CALENDAR_ID, calendarID);
            }
        };

        listView.setOnItemClickListener(myListener);
    }

    public void onClickDone(View view){
        Intent intent = new Intent(this, PTModules.class);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        calendarNameCursor.close();
        db.close();
    }
}
