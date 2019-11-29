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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PTEventEdit extends AppCompatActivity {
    public static final String EXTRA_EVENT_ID = "eventID";
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Cursor cursor;
    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int eventID = getIntent().getIntExtra(EXTRA_EVENT_ID, -1) + 1;
        try{
            dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query("EVENT",
                    new String[]{"NAME"},
                    "_id = ?",
                    new String[]{Integer.toString(eventID)},
                    null, null, null);

            if(cursor.moveToFirst()){
                String name = cursor.getString(0);
                EditText editText = (EditText) findViewById(R.id.edittext_edit_event_name);
                editText.setText(name);
            }
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        //MenuItem menuItem = menu.findItem(R.id.add_new_event);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_event:
                Intent intent = new Intent(this, PTEventList.class);
                deleteCurrentEvent();
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

    public void onConfirmEdit(View view){
        EditText eventNameField = (EditText) findViewById(R.id.edittext_edit_event_name);
        String eventName = eventNameField.getText().toString();
        int eventID = getIntent().getIntExtra(EXTRA_EVENT_ID, -1) + 1;
        cv = new ContentValues();
        cv.put("NAME", eventName);
        try{
            SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getWritableDatabase();
            db.update("EVENT",
                    cv,
                    "_id = ?",
                    new String[]{Integer.toString(eventID)});

        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        db.close();

        Intent intent = new Intent(this, PTEventList.class);
        startActivity(intent);
    }

    public void deleteCurrentEvent(){
        int eventID = getIntent().getIntExtra(EXTRA_EVENT_ID, -1) +1;
        try{
            dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getWritableDatabase();
            db.delete("EVENT",
                     "_id = ?",
                    new String[]{Integer.toString(eventID)});

            /*if(cursor.moveToFirst()){
                String name = cursor.getString(0);
                EditText editText = (EditText) findViewById(R.id.edittext_edit_event_name);
                editText.setText(name);
            }*/
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
