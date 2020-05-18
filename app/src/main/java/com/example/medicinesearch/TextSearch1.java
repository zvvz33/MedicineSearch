package com.example.medicinesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class TextSearch1 extends AppCompatActivity {
    Button TextSearch1_button;
    EditText mark_front;

    final String TextSearch_TAG = "식별표시 검색";
    Spinner form_sp;
    Spinner shape_sp;
    Spinner color_sp;
    Spinner line_sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search1);


        mark_front = (EditText) findViewById(R.id.mark_text1);
        TextSearch1_button = (Button) findViewById(R.id.search1_search);
        form_sp = (Spinner)findViewById(R.id.sp_form);
        shape_sp = (Spinner)findViewById(R.id.sp_shape);
        color_sp = (Spinner)findViewById(R.id.sp_color);
        line_sp = (Spinner)findViewById(R.id.sp_line);

        TextSearch1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TextSearch_TAG, "스피너박스값: " + form_check + shape_check + color_check + line_check+ front_mark+back_mark);

                Intent intent = new Intent(TextSearch1.this, TextSearch1Result.class);
                intent.putExtra("front_mark",mark_front.getText().toString().trim());
                intent.putExtra("form_check",form_sp.getSelectedItem().toString());
                intent.putExtra("shape_check",shape_sp.getSelectedItem().toString());
                intent.putExtra("color_check",color_sp.getSelectedItem().toString());
                intent.putExtra("line_check",line_sp.getSelectedItem().toString());
                startActivity(intent);
            }
        });


    }

}

