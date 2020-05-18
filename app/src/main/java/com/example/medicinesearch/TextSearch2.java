package com.example.medicinesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextSearch2 extends AppCompatActivity{
    Button txt2_httpButton;
    EditText item_name;
    EditText entp_name;
    EditText bar_code;
    EditText item_seq;
    EditText item_permit_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search2);

        item_name = (EditText) findViewById(R.id.editText1);
        entp_name = (EditText) findViewById(R.id.editText2);
        bar_code = (EditText) findViewById(R.id.editText3);
        item_seq = (EditText) findViewById(R.id.editText4);
        item_permit_date = (EditText) findViewById(R.id.editText5);

        txt2_httpButton = findViewById(R.id.n_button2);
        txt2_httpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medi_name = item_name.getText().toString().trim();
                String medi_code = item_seq.getText().toString().trim();
                //Log.d(TextSearch_TAG, "스피너박스값: " + form_check + shape_check + color_check + line_check+ front_mark+back_mark);
                if(medi_name.isEmpty() && medi_code.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "제품명또는 품목 기준 코드를 채워주세요", Toast.LENGTH_LONG).show();

                }
                else{
                    Intent intent = new Intent(TextSearch2.this, TextSearch2Result.class);
                    intent.putExtra("item_name", medi_name);
                    intent.putExtra("entp_name", entp_name.getText().toString().trim());
                    intent.putExtra("bar_code", bar_code.getText().toString().trim());
                    intent.putExtra("item_seq", medi_code);
                    intent.putExtra("item_permit_date", item_permit_date.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }
}
