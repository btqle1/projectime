package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.GregorianCalendar;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        ListView agendaList = findViewById(R.id.agenda_list);
        //ListView agendaList2 = findViewById(R.id.agenda_list2);
        SQLiteOpenHelper openHelper = new PTDatabaseHelper(this);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor eventCursor = db.query(
                "EVENT",
                new String[] { "NAME", "TIME", "TIMEISKNOWN" },
                null,
                null,
                null,
                null,
                "TIME",
                null
        );
        int i = 0;
        String[] eventArray = new String[eventCursor.getCount()];
        //String[] eventArrayName = new String[eventCursor.getCount()];
        //String[] eventArrayDate = new String[eventCursor.getCount()];
        while(eventCursor.moveToNext()) {
            String name = eventCursor.getString(0);
            long time = eventCursor.getLong(1);
            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTimeInMillis(time);
            int year = gCal.get(GregorianCalendar.YEAR);
            int month = gCal.get(GregorianCalendar.MONTH);
            int day = gCal.get(GregorianCalendar.DAY_OF_MONTH);
            eventArray[i++] = String.format("%s \t%d/%d/%d", name, month, day, year);
            //eventArrayName[i] = String.format("%s", name);
            //eventArrayDate[i] = String.format("\t%d/%d/%d", month, day, year);
            //i++;
        }
        eventCursor.close();
        db.close();

        ArrayAdapter<String> eventArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                eventArray
        );
        agendaList.setAdapter(eventArrayAdapter);
        /*ArrayAdapter<String> eventNameArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                eventArrayName
        );
        ArrayAdapter<String> eventDateArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                eventArrayDate
        );
        agendaList.setAdapter(eventNameArrayAdapter);
        agendaList2.setAdapter(eventDateArrayAdapter);*/
    }
}
