package com.example.projectime.moodle;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.projectime.Calendar;
import com.example.projectime.Event;
import com.example.projectime.PTDatabaseHelper;
import com.example.projectime.PTMoodle;
import com.example.projectime.Tab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DataPopulateTask extends AsyncTask<String, Void, ArrayList<Calendar>> {
    private PTMoodle ptMoodle;

    public DataPopulateTask(PTMoodle ptMoodle) {
        this.ptMoodle = ptMoodle;
    }

    @Override
    protected ArrayList<Calendar> doInBackground(String... strings) {
        try {
            DataSource dataSource = new DataSource(strings[0], strings[1], strings[2]);

            ArrayList<Calendar> calendars = new ArrayList<>();
            ArrayList<String> calendarNames = dataSource.getCalendars();
            for(int i = 0; i < calendarNames.size(); i++) {
                ArrayList<Tab> tabs = new ArrayList<>();
                ArrayList<String> tabNames = dataSource.getTabs(i);
                for (int j = 0; j < tabNames.size(); j++) {
                    ArrayList<Event> events = new ArrayList<>();
                    ArrayList<String> eventNames = dataSource.getEvents(i, j);
                    ArrayList<String> urls = dataSource.getURLs(i, j);

                    for (int k = 0; k < eventNames.size(); k++) {
                        String name = eventNames.get(k);
                        String url = urls.get(k);
                        events.add(new Event(name, url));
                    }

                    tabs.add(new Tab(tabNames.get(j), events));
                }
                calendars.add(new Calendar(calendarNames.get(i), tabs));
            }

            return calendars;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(ArrayList<Calendar> result) {
        ptMoodle.addDatabaseEvents(result);
    }
}
