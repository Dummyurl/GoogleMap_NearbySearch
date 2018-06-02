package com.example.waterflower124.googlemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String str = getIntent().getStringExtra("place_id");
        ((TextView)findViewById(R.id.txt)).setText(str);
    }
}
