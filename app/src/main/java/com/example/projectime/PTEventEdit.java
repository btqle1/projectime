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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class PTEventEdit extends AppCompatActivity {

    public static final String EXTRA_TAB_ID = "EXTRA_TAB_ID";
    public static final String EXTRA_CALENDAR_ID = "EXTRA_CALENDAR_ID";
    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Cursor cursor;
    private ContentValues cv;
    private long tabID;
    private long calendarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        long eventID = getIntent().getLongExtra(EXTRA_EVENT_ID, -1) + 1;
        int intEventID = (int)eventID;
        tabID = getIntent().getLongExtra(EXTRA_TAB_ID, -1);
        calendarID = getIntent().getLongExtra(EXTRA_CALENDAR_ID, -1);
        try{
            dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query("EVENT",
                    new String[]{"_id", "NAME","CALENDAR_ID","TAB_ID"},
                    "_id = ? AND CALENDAR_ID = ? AND TAB_ID = ?",
                    new String[]{String.valueOf(eventID), String.valueOf(tabID), String.valueOf(calendarID)},
                    null, null, null);

            if(cursor.moveToPosition(intEventID)){
                String name = cursor.getString(0);
                EditText editText = (EditText) findViewById(R.id.edit_event_name);
                editText.setText(name);
            }
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        String[] strDaysArray = initDayArray();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item, strDaysArray);
        Spinner daySpinner = (Spinner) findViewById(R.id.spinner_event_day);
        daySpinner.setAdapter(spinnerArrayAdapter);
    }

    public void onConfirmEdit(View view){
        EditText eventNameField = (EditText) findViewById(R.id.edit_event_name);
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
        intent.putExtra(EXTRA_TAB_ID, tabID);
        intent.putExtra(EXTRA_CALENDAR_ID, calendarID);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public String[] initDayArray(){
        int days = 31;
        int[] daysArray = new int[days];
        for(int i = 1; i <= daysArray.length; i++){
            daysArray[i-1] = i;
        }
        String[] strDaysArray = Arrays.toString(daysArray).split("[\\[\\]]")[1].split(", ");
        return strDaysArray;
    }
}
