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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PTEventEdit extends AppCompatActivity {

    public static final String EXTRA_TAB_ID = "EXTRA_TAB_ID";
    public static final String EXTRA_CALENDAR_ID = "EXTRA_CALENDAR_ID";
    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Cursor cursor;
    private ContentValues cv;
    private long eventID;
    private long tabID;
    private long calendarID;
    private long date;
    private Calendar dueDate;
    Spinner daySpinner;
    Spinner monthSpinner;
    Spinner yearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptevent_edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventID = getIntent().getLongExtra(EXTRA_EVENT_ID, -1) + 1;
        int intEventID = (int)eventID;
        tabID = getIntent().getLongExtra(EXTRA_TAB_ID, -1);
        calendarID = getIntent().getLongExtra(EXTRA_CALENDAR_ID, -1);
        dueDate = new GregorianCalendar();
        String[] strDaysArray = initDayArray();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item, strDaysArray);
        daySpinner = (Spinner) findViewById(R.id.spinner_event_day);
        monthSpinner = (Spinner) findViewById(R.id.spinner_event_month);
        yearSpinner = (Spinner) findViewById(R.id.spinner_event_year);

        daySpinner.setAdapter(spinnerArrayAdapter);
        try{
            dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query("EVENT",
                    new String[]{"_id", "NAME","CALENDAR_ID","TAB_ID", "TIME"},
                    "_id = ? AND TAB_ID = ? AND CALENDAR_ID = ?",
                    new String[]{String.valueOf(eventID), String.valueOf(tabID), String.valueOf(calendarID)},
                    null, null, null);

            if(cursor.moveToFirst()){
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                date = cursor.getLong(cursor.getColumnIndex("TIME"));
                Log.i("info", "Time is " + date);
                GregorianCalendar gCal = new GregorianCalendar();
                gCal.setTimeInMillis(date);
                int year = gCal.get(GregorianCalendar.YEAR);
                int month = gCal.get(GregorianCalendar.MONTH);
                int day = gCal.get(GregorianCalendar.DAY_OF_MONTH);
                Log.i("info", "Year is " + year);
                Log.i("info", "Month is " + month);
                Log.i("info", "Day is " + day);

                daySpinner.setSelection(day -1);
                monthSpinner.setSelection(month);
                yearSpinner.setSelection(0);

                EditText editText = (EditText) findViewById(R.id.edit_event_name);
                editText.setText(name);
            }
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onConfirmEdit(View view){
        EditText eventNameField = (EditText) findViewById(R.id.edit_event_name);
        String eventName = eventNameField.getText().toString();
        int day = daySpinner.getSelectedItemPosition()+1;
        int month = monthSpinner.getSelectedItemPosition();
        int year = 2019;
        Calendar dueDate = new GregorianCalendar(year, month, day);
        long date = dueDate.getTimeInMillis();


        cv = new ContentValues();
        cv.put("NAME", eventName);
        cv.put("TIME", date);
        try{
            SQLiteOpenHelper dbHelper = new PTDatabaseHelper(this);
            db = dbHelper.getWritableDatabase();
            db.update("EVENT",
                    cv,
                    "_id = ? AND TAB_ID = ? AND CALENDAR_ID = ?",
                    new String[]{String.valueOf(eventID), String.valueOf(tabID), String.valueOf(calendarID)});

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
        new PredictDatesTask().doInBackground(db);
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
