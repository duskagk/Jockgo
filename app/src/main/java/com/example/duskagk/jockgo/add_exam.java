package com.example.duskagk.jockgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class add_exam extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        String str;
        TextView subname=(TextView)findViewById(R.id.add_sub_name);
        Intent intent = getIntent();
        str=intent.getExtras().getString("Name");
        subname.setText(str);
    }
}
