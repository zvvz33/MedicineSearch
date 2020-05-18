package com.example.medicinesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FirstScreen extends AppCompatActivity {

    ImageView search;
    ImageView camera2;
    ImageView Schedule;
    ImageView shape_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, TextSearch2.class);
                startActivity(intent);
            }
        });
        shape_search = (ImageView) findViewById(R.id.pen);
        shape_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, TextSearch1.class);
                startActivity(intent);
            }
        });


        camera2 = (ImageView) findViewById(R.id.camera2);
        camera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, CameraSearch.class);
                startActivity(intent);
            }
        });

        Schedule = (ImageView) findViewById(R.id.clock);
        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
