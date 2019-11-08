package com.example.projectime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/*
MUST add to project structure:
Design
*/

public class MainActivity extends AppCompatActivity {

    private int testFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.class_options);
        listView.setVisibility(View.INVISIBLE);

        Intent intent =getIntent();
        this.testFlag =intent.getIntExtra("testFlag",0);

        if(this.testFlag == 1){
            listView.setVisibility(View.VISIBLE);
            AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if(position == 0){
                        Intent intent = new Intent(MainActivity.this, PTTabList.class);
                        startActivity(intent);
                    }
                }
            };
            //ListView listView = (ListView) findViewById(R.id.class_options);
            listView.setOnItemClickListener(myListener);
        }
    }

    public void onClickDone(View view){
        Intent intent = new Intent(this, PTModules.class);
        startActivity(intent);
    }
}
