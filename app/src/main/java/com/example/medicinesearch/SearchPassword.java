package com.example.medicinesearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SearchPassword extends AppCompatActivity {
    FirebaseAuth mAuth;

    EditText Password_Find_email;

    Button Button_Password_Find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_password);

        mAuth = FirebaseAuth.getInstance();
        Password_Find_email = (EditText) findViewById(R.id.input_email2);

        Button_Password_Find = (Button) findViewById(R.id.search_password2);
        Button_Password_Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email_Password_Find = Password_Find_email.getText().toString().trim();
                final String TAG = "SearchPassword";

                if ((Email_Password_Find == null || Email_Password_Find.isEmpty())) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(Email_Password_Find)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(SearchPassword.this, "메일로 재설정 링크를 보냈습니다 ", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SearchPassword.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SearchPassword.this, " 이메일이 존재하지 않습니다", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }


            }
        });
    }
}
