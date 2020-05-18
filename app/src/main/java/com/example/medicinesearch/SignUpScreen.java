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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpScreen extends AppCompatActivity {
    EditText email_input;
    EditText pswd_input;
    EditText nameTxt;
    EditText pswd_input_check;

    Button sign_in_button;

    private FirebaseAuth mAuth;

    FirebaseFirestore db;

    final String FIRESTORE_TAG = "FIRESTORE_TAG";

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email_input = (EditText) findViewById(R.id.email);
        pswd_input = (EditText) findViewById(R.id.signpassword);
        nameTxt = (EditText) findViewById(R.id.name);
        pswd_input_check = (EditText) findViewById(R.id.checkpassword);


        sign_in_button = (Button) findViewById(R.id.button3);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email = email_input.getText().toString().trim();
                final String pswd = pswd_input.getText().toString().trim();
                final String name = nameTxt.getText().toString().trim();
                final String pswd_check = pswd_input_check.getText().toString().trim();

                if ((email == null || email.isEmpty()) || (pswd == null || pswd.isEmpty()) || (name == null || name.isEmpty())
                        || (pswd_check == null || pswd_check.isEmpty()) ) {
                    Toast.makeText(getApplicationContext(), "빈칸을 채워주세요",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!pswd.equals(pswd_check)){
                    Toast.makeText(getApplicationContext(), "비밀번호확인이 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, pswd)
                            .addOnCompleteListener(SignUpScreen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            Toast.makeText(getApplicationContext(), "비밀번호는 영문 숫자 포함 8자리이상으로 해주세요.",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            Toast.makeText(getApplicationContext(), "e메일 형식에 맞지않습니다.",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            Toast.makeText(getApplicationContext(), "이미 존재하는 email입니다.",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), "다시 확인해주세요",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        //Log.d(TAG, "createUserWithEmail:success"); //디버그용
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("email", email);
                                        user.put("name", name);

                                        db.collection("users")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(FIRESTORE_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(FIRESTORE_TAG, "Error adding document", e);
                                                    }
                                                });

                                        Toast.makeText(getApplicationContext(), "가입성공.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpScreen.this, FirstScreen.class);
                                        startActivity(intent);
                                        //updateUI(user);//유저업데이트
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
}