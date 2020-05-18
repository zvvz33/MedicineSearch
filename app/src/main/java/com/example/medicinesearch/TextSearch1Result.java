package com.example.medicinesearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TextSearch1Result extends AppCompatActivity {

    private ListView list;
    private MyAdapter myadapter;
    AppCompatDialog progressDialog;


    FirebaseFirestore db;
    final String TextSearch_Result_TAG = "식별표시 검색 결과";
    MyItem myItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search1_result);

        progressON(this,"검색중..");
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        search(intent);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Detail_Result.class);
                myItem = (MyItem) myadapter.getItem(position);
                intent.putExtra("img",myItem.getMedicine_picture());
                intent.putExtra("name",myItem.getMedicine_name());

                startActivity(intent);
            }
        });

    }
    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }
    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }


        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    public void search(Intent intent){
        final String front_mark = intent.getExtras().getString("front_mark");
        String form_check = intent.getExtras().getString("form_check");
        String shape_check = intent.getExtras().getString("shape_check");
        String color_check = intent.getExtras().getString("color_check");
        String line_check = intent.getExtras().getString("line_check");
        list = (ListView)findViewById(R.id.list);
        myadapter = new MyAdapter();

        if(front_mark.isEmpty()) {
            db.collection("Singlemedicine")
                    .whereEqualTo("의약품제형", shape_check)//정제형넣어야함
                    .whereEqualTo("색상앞", color_check).whereEqualTo("분할선앞", line_check)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int document_count = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document_count = document_count + 1;
                                    Log.d(TextSearch_Result_TAG, document.getId() + " => " + document.getData());
                                    Log.d(TextSearch_Result_TAG, "색상앞" + document.get("색상앞") + "의약품제형" + document.get("의약품제형"));
                                    myadapter.addItem((String) document.get("큰제품이미지"), (String)document.get("품목명"), (String)document.get("성상"));
                                    Toast.makeText(getApplicationContext(), "검색성공.", Toast.LENGTH_SHORT).show();
                                }
                                if(document_count==0) {
                                    progressOFF();
                                    Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    progressOFF();
                                    list.setAdapter(myadapter);
                                }
                            } else {
                                progressOFF();
                                Log.d(TextSearch_Result_TAG, "Error getting documents: ", task.getException());
                                Toast.makeText(getApplicationContext(), "검색실패.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            db.collection("Singlemedicine")
                    .whereEqualTo("의약품제형", shape_check)//정제형넣어야함
                    .whereEqualTo("색상앞", color_check).whereEqualTo("분할선앞", line_check)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int document_count2 = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document_count2 = document_count2 + 1;
                                    String mark_check = (String)document.get("표시앞");
                                    if(mark_check.contains(front_mark.toUpperCase())) {
                                        Log.d(TextSearch_Result_TAG, document.getId() + " => " + document.getData());
                                        Log.d(TextSearch_Result_TAG, "색상앞" + document.get("색상앞") + "의약품제형" + document.get("의약품제형"));
                                        myadapter.addItem((String) document.get("큰제품이미지"), (String)document.get("품목명"), (String)document.get("성상"));
                                        Toast.makeText(getApplicationContext(), "검색성공.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(document_count2==0) {
                                    progressOFF();
                                    Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    progressOFF();
                                    list.setAdapter(myadapter);
                                }
                            } else {
                                progressOFF();
                                Log.d(TextSearch_Result_TAG, "Error getting documents: ", task.getException());
                                Toast.makeText(getApplicationContext(), "검색실패.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
