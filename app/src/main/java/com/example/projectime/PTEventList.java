package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PTEventList extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView eventListView = (ListView)findViewById(R.id.event_list);

/*        Intent intent = getIntent();
        long tabId = intent.getLongExtra(PTTabList.EXTRA_TAB_ID, -1);

        SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.query("EVENT",
                new String[] {"_id", "NAME"},
                "TAB_ID = ?",
                new String[] {String.valueOf(tabId)},
                null, null, null);

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
                long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
            }
        });*/
        try {
            SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query("EVENT",
                    new String[] {"_id", "NAME"},
                    null, null, null, null, null);

            SimpleCursorAdapter eventNameListAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"NAME"},
                    new int[]{android.R.id.text1},
                    0);

            eventListView.setAdapter(eventNameListAdapter);

            eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(PTEventList.this, PTEventEdit.class);
                    //Need code to pass in the id of what is clicked
                    startActivity(intent);
                }
            });

        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //MenuItem menuItem = menu.findItem(R.id.add_new_event);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_new_event:
                Intent intent = new Intent(this, PTEventAdd.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}