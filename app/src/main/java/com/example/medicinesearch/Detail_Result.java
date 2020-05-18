package com.example.medicinesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Detail_Result extends AppCompatActivity {
    Document doc = null;
    TextView detail;
    ImageView detail_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__result);
        detail = (TextView)findViewById(R.id.detail_result); //파싱된 결과확인!
        detail_img = (ImageView)findViewById(R.id.detail_img);

        Intent intent = getIntent();

        String img = intent.getExtras().getString("img");
        String name = intent.getExtras().getString("name");

        Log.d("값 테스트",img+name);

        String s_url = "http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductItem?serviceKey=lzeP8DsmjYNggDpR3snQu%2BpyBx7VcNMH2YOw4jX4LfF8oCJwOrzeoGeeixqRIF6wrRDZj95QQWx8Soh7HTK1MQ%3D%3D&item_name=" +
                name + "&entp_name=&item_permit_date=&entp_no=&pageNo=1&numOfRows=3&bar_code=&item_seq=&start_change_date=&end_change_date=&";
        Dom_xmlParser parsing_result = (new Dom_xmlParser());
        parsing_result.execute(s_url);
        Glide.with(this).load(img).into(detail_img);

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
                Log.d("파싱에러메시지2", "e : " + e);

            }
            return doc;
        }

        protected void onPostExecute(Document doc) {
            String Result = null;
            String ITEM_SEQ = null, ITEM_NAME = null, ENTP_NAME=null, ITEM_PERMIT_DATE=null, ETC_OTC_CODE=null, CHART=null, BAR_CODE=null
                    , STORAGE_METHOD=null, VALID_TERM=null, EE_DOC_DATA=null, UD_DOC_DATA=null, NB_DOC_DATA=null;
            NodeList nodeList = doc.getElementsByTagName("item");
            Log.d("파싱에러메시지2", "e :ㄴㅇㄴㄴㅇ"+ nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {

                // 데이터를 추출
                Node node = nodeList.item(i); //item엘리먼트 노드
                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("ITEM_SEQ");
                Element nameElement = (Element) nameList.item(0);
                if (nameElement != null) {
                    nameList = nameElement.getChildNodes();
                    ITEM_SEQ = ((Node) nameList.item(0)).getNodeValue();
                }


                NodeList item_name = fstElmnt.getElementsByTagName("ITEM_NAME");
                if (item_name.item(0) != null) {
                    ITEM_NAME = item_name.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList entp_name = fstElmnt.getElementsByTagName("ENTP_NAME");
                if (entp_name.item(0) != null) {
                    ENTP_NAME = entp_name.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList item_permit_date = fstElmnt.getElementsByTagName("ITEM_PERMIT_DATE");
                if (item_permit_date.item(0) != null) {
                    ITEM_PERMIT_DATE = item_permit_date.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList etc_otc_code = fstElmnt.getElementsByTagName("ETC_OTC_CODE");
                if (etc_otc_code.item(0) != null) {
                    ETC_OTC_CODE = etc_otc_code.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList chart = fstElmnt.getElementsByTagName("CHART");
                if (chart.item(0) != null) {
                    CHART = chart.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList bar_code = fstElmnt.getElementsByTagName("BAR_CODE");
                if (bar_code.item(0) != null) {
                    BAR_CODE = bar_code.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList storage_method = fstElmnt.getElementsByTagName("STORAGE_METHOD");
                if (storage_method.item(0) != null) {
                    STORAGE_METHOD = storage_method.item(0).getChildNodes().item(0).getNodeValue();
                }
                NodeList valid_term = fstElmnt.getElementsByTagName("VALID_TERM");
                if (valid_term.item(0) != null) {
                    VALID_TERM = valid_term.item(0).getChildNodes().item(0).getNodeValue();
                }

                NodeList ee_doc_data = doc.getElementsByTagName("EE_DOC_DATA");
                if (ee_doc_data.item(0) != null) {
                    Node ee_doc_node = ee_doc_data.item(0);
                    Element element = (Element)ee_doc_node;

                    NodeList doc_list = element.getElementsByTagName("DOC");
                    if(doc_list.item(0) != null) {
                        Node ee_doc_data_node = doc_list.item(0);
                        Element DOC_element = (Element) ee_doc_data_node;
                        EE_DOC_DATA = DOC_element.getAttribute("title") + "\n";

                        NodeList article_list = DOC_element.getElementsByTagName("ARTICLE");
                        if (article_list.item(0) != null) {
                            for (int k = 0; k < article_list.getLength(); k++) {
                                Node ee_article = article_list.item(k);
                                Element ee_article_element = (Element) ee_article;
                                EE_DOC_DATA = EE_DOC_DATA + ee_article_element.getAttribute("title") + "\n";

                                NodeList paragraph_list = ee_article_element.getElementsByTagName("PARAGRAPH");
                                if (paragraph_list.item(0) != null) {
                                    for (int a = 0; a < paragraph_list.getLength(); a++) {
                                        EE_DOC_DATA = EE_DOC_DATA + paragraph_list.item(a).getChildNodes().item(0).getNodeValue() + "\n";
                                    }
                                }
                            }
                        }
                    }
                }
                NodeList ud_doc_data = doc.getElementsByTagName("UD_DOC_DATA");
                if (ud_doc_data.item(0) != null) {
                    Node ud_doc_node = ud_doc_data.item(0);
                    Element element = (Element)ud_doc_node;

                    NodeList doc_list = element.getElementsByTagName("DOC");
                    if(doc_list.item(0) != null) {
                        Node ud_doc_data_node = doc_list.item(0);
                        Element DOC_element = (Element) ud_doc_data_node;
                        UD_DOC_DATA = DOC_element.getAttribute("title");

                        NodeList article_list = DOC_element.getElementsByTagName("ARTICLE");
                        if (article_list.item(0) != null) {
                            for (int k = 0; k < article_list.getLength(); k++) {
                                Node ud_article = article_list.item(k);
                                Element ud_article_element = (Element) ud_article;
                                UD_DOC_DATA = UD_DOC_DATA +"\n"+ ud_article_element.getAttribute("title") + "\n";

                                NodeList paragraph_list = ud_article_element.getElementsByTagName("PARAGRAPH");
                                if (paragraph_list.item(0) != null) {
                                    for (int a = 0; a < paragraph_list.getLength(); a++) {
                                        UD_DOC_DATA = UD_DOC_DATA + paragraph_list.item(a).getChildNodes().item(0).getNodeValue() + "\n";
                                    }
                                }
                            }
                        }
                    }
                }
                NodeList nb_doc_data = doc.getElementsByTagName("NB_DOC_DATA");
                if (nb_doc_data.item(0) != null) {
                    Node nb_doc_node = nb_doc_data.item(0);
                    Element element = (Element)nb_doc_node;

                    NodeList doc_list = element.getElementsByTagName("DOC");
                    if(doc_list.item(0) != null) {
                        Node nb_doc_data_node = doc_list.item(0);
                        Element DOC_element = (Element) nb_doc_data_node;
                        NB_DOC_DATA = DOC_element.getAttribute("title");

                        NodeList article_list = DOC_element.getElementsByTagName("ARTICLE");
                        if (article_list.item(0) != null) {
                            for (int k = 0; k < article_list.getLength(); k++) {
                                Node nb_article = article_list.item(k);
                                Element nb_article_element = (Element) nb_article;
                                NB_DOC_DATA = NB_DOC_DATA + "\n" + nb_article_element.getAttribute("title") + "\n";

                                NodeList paragraph_list = nb_article_element.getElementsByTagName("PARAGRAPH");
                                if (paragraph_list.item(0) != null) {
                                    for (int a = 0; a < paragraph_list.getLength(); a++) {
                                        Node nb_para = paragraph_list.item(a);
                                        Element nb_para_element = (Element)nb_para;
                                        if(!(nb_para_element.getAttribute("tagName").equals("table"))) {
                                            NB_DOC_DATA = NB_DOC_DATA + paragraph_list.item(a).getChildNodes().item(0).getNodeValue() + "\n";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                detail.setText(//"품목기준코드 : " + ITEM_SEQ +
                        "\n제품명 : " + ITEM_NAME + "\n회사명 : " + ENTP_NAME +
                                //"\n허가일자 : "+ITEM_PERMIT_DATE +
                        "\n약품분류 : " + ETC_OTC_CODE + "\n성 상 : " + CHART +
                                //"\n표준코드 : " + BAR_CODE +
                                "\n보관방법 : " +  STORAGE_METHOD +
                        "\n유효기간 : " + VALID_TERM + "\n\n" + EE_DOC_DATA + "\n" + UD_DOC_DATA +"\n"+ NB_DOC_DATA);
            }
            super.onPostExecute(doc);
        }
    }


}
