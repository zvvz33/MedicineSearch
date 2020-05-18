package com.example.medicinesearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;

import okio.Utf8;

public class TextSearch2Result extends AppCompatActivity {
    AppCompatDialog progressDialog;
    private ListView list2;
    private MyAdapter myadapter;
    MyItem myItem;
    FirebaseFirestore db;

    Document doc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search2_result);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String item_name = intent.getExtras().getString("item_name");
        String entp_name = intent.getExtras().getString("entp_name");
        String bar_code = intent.getExtras().getString("bar_code");
        String item_seq = intent.getExtras().getString("item_seq");
        String item_permit_date = intent.getExtras().getString("item_permit_date");

        progressON(this,"검색중..");

        String s_url = "http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductItem?serviceKey=lzeP8DsmjYNggDpR3snQu%2BpyBx7VcNMH2YOw4jX4LfF8oCJwOrzeoGeeixqRIF6wrRDZj95QQWx8Soh7HTK1MQ%3D%3D&" +
                "item_name=" + item_name + "&entp_name=" + entp_name + "&item_permit_date=" + item_permit_date + "&entp_no=&pageNo=1&numOfRows=3&bar_code=" + bar_code + "&item_seq=" + item_seq + "&start_change_date=&end_change_date=&";

        Dom_xmlParser parsing_result = (new Dom_xmlParser());
        code_search(parsing_result,s_url);
        list2 = (ListView)findViewById(R.id.list2);
        myadapter = new MyAdapter();
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private class Dom_xmlParser extends AsyncTask<String, Void, Document> {
        protected Document doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
            } catch (Exception e) {
                //Toast.makeText(TextSearch2Result.this, "파싱에러", Toast.LENGTH_SHORT).show();
                Log.d("파싱에러메시지2", "e : " + e);

            }
            return doc;
        }

        protected void onPostExecute(Document doc) {
            String Result = null;
            String CHART = null, ITEM_NAME = null;
            NodeList nodeList = doc.getElementsByTagName("item");
            if(nodeList.getLength()<1){
                progressOFF();
                Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();

            }
            for (int i = 0; i < nodeList.getLength(); i++) {

                // 데이터를 추출
                Node node = nodeList.item(i); //item엘리먼트 노드
                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("ITEM_NAME");
                Element nameElement = (Element) nameList.item(0);
                if (nameElement != null) {
                    nameList = nameElement.getChildNodes();
                    ITEM_NAME = ((Node) nameList.item(0)).getNodeValue();
                }


                NodeList websiteList = fstElmnt.getElementsByTagName("CHART");
                if (websiteList.item(0) != null) {
                    CHART = websiteList.item(0).getChildNodes().item(0).getNodeValue();
                }
                Result = Result + CHART;
                Result = Result + ITEM_NAME + "\n";
                db.collection("Singlemedicine")
                        .whereEqualTo("품목명", ITEM_NAME)//정제형넣어야함
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            QueryDocumentSnapshot result_document;
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int document_count = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document_count = document_count + 1;
                                        myadapter.addItem((String) document.get("큰제품이미지"), (String)document.get("품목명"), (String)document.get("성상"));
                                        Toast.makeText(getApplicationContext(), "검색성공.", Toast.LENGTH_SHORT).show();
                                    }
                                    if(document_count==0) {
                                        progressOFF();
                                        Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressOFF();
                                        list2.setAdapter(myadapter);
                                    }
                                } else {
                                    progressOFF();
                                    Toast.makeText(getApplicationContext(), "검색실패.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Log.d("파싱", "파싱파싱메시지2" + Result + nodeList.getLength());
            }

            super.onPostExecute(doc);
        }
    }
    public void code_search(Dom_xmlParser parsing_result,String s_url){
        parsing_result.execute(s_url);

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
        Log.d("들어오긴","하냐");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
