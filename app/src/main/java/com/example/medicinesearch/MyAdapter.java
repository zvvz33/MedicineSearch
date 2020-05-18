package com.example.medicinesearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<MyItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        ImageView medicine_picture = (ImageView) convertView.findViewById(R.id.medicine_picture);
        TextView list_medicine_name = (TextView)  convertView.findViewById(R.id.list_medicine_name);
        TextView list_medicine_text = (TextView) convertView.findViewById(R.id.list_medicine_text);

        MyItem myItem = (MyItem) getItem(position);

        Glide.with(context).load(mItems.get(position).getMedicine_picture()).into(medicine_picture);

        //medicine_picture.setImageResource(myItem.getMedicine_picture());
        list_medicine_name.setText(myItem.getMedicine_name());
        list_medicine_text.setText(myItem.getMedicine_text());

        //이벤트 리스너
        return convertView;
    }

    public void addItem(String img, String name, String contents){

        MyItem mItem = new MyItem();

        mItem.setMedicine_picture(img);
        mItem.setMedicine_name(name);
        mItem.setMedicine_text(contents);

        mItems.add(mItem);
    }
    public void addItem_two(String name, String contents){

        MyItem mItem = new MyItem();

        //mItem.setMedicine_picture(img);
        mItem.setMedicine_name(name);
        mItem.setMedicine_text(contents);

        mItems.add(mItem);
    }
}
