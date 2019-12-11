package com.example.projectime.moodle;

import android.os.AsyncTask;
import android.util.Log;

import com.example.projectime.Calendar;
import com.example.projectime.Event;
import com.example.projectime.PTMoodle;
import com.example.projectime.Tab;

import java.io.IOException;
import java.util.ArrayList;

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
                    ArrayList<Long> times = dataSource.getTimes(i, j);

                    for (int k = 0; k < eventNames.size(); k++) {
                        String name = eventNames.get(k);
                        String url = urls.get(k);
                        long time = times.get(k);

                        events.add(new Event(name, url, time));
                    }

                    tabs.add(new Tab(tabNames.get(j), events));
                }
                Log.i("info", "New calendar added");
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
