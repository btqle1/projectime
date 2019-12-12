package com.example.projectime;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class PredictDatesTask extends AsyncTask<SQLiteDatabase, Void, Void> {
    @Override
    protected Void doInBackground(SQLiteDatabase... dbs) {
        SQLiteDatabase db = dbs[0];
        Cursor calendarCursor = db.query(
            "CALENDAR",
            new String[] { "_id" },
            null,
            null,
            null,
            null,
            null,
            null
        );
        while(calendarCursor.moveToNext()) {
            long calendar = calendarCursor.getLong(0);
            Cursor tabCursor = db.query(
                "TAB",
                new String[] { "_id" },
                "CALENDAR_ID = ?",
                new String[] { String.valueOf(calendar) },
                null,
                null,
                null,
                null
            );
            while(tabCursor.moveToNext()) {
                long tab = tabCursor.getLong(0);
                Cursor unknownEventCursor = db.query(
                    "EVENT",
                    new String[] { "_id", "TIME" },
                    "CALENDAR_ID = ? AND TAB_ID = ? AND TIMEISKNOWN = FALSE",
                    new String[] {
                        String.valueOf(calendar),
                        String.valueOf(tab)
                    },
                    null,
                    null,
                    null,
                    null
                );
                Cursor knownEventCursor = db.query(
                    "EVENT",
                    new String[] {
                        "_id",
                        "TIME"
                    },
                    "CALENDAR_ID = ? AND TAB_ID = ? AND TIMEISKNOWN = TRUE",
                    new String[] {
                        String.valueOf(calendar),
                        String.valueOf(tab)
                    },
                    null,
                    null,
                    null,
                    null
                );
                double slope = 86400000.0d;
                double intercept = System.currentTimeMillis();
                if(knownEventCursor.getCount() >= 2) {
                    SimpleRegression simpleRegression = new SimpleRegression();
                    while(knownEventCursor.moveToNext()) {
                        simpleRegression.addData(
                            knownEventCursor.getLong(0),
                            knownEventCursor.getLong(1)
                        );
                    }
                    slope = simpleRegression.getSlope();
                    intercept = simpleRegression.getIntercept();
                }
                while(unknownEventCursor.moveToNext()) {
                    long id = unknownEventCursor.getLong(0);
                    ContentValues newTime = new ContentValues();
                    newTime.put("TIME", (long)(id*slope+intercept));
                    db.update(
                        "EVENT",
                        newTime,
                        "CALENDAR_ID = ? AND TAB_ID = ? AND _id = ?",
                        new String[] {
                            String.valueOf(calendar),
                            String.valueOf(tab),
                            String.valueOf(id)
                        }
                    );
                }
            }
        }
        db.close();
        return null;
    }
}