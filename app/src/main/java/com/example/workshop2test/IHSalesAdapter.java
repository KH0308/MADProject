package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class IHSalesAdapter extends BaseAdapter {

    Context context;
    List<ImageIHSales> listDataItem;

    public IHSalesAdapter(Context context, List<ImageIHSales> listDataItem){
        this.context = context;
        this.listDataItem = listDataItem;
    }

    @Override
    public int getCount() {
        return listDataItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listDataItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_hot_sales_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_image);
        TextView textView = convertView.findViewById(R.id.item_name);

        byte[] imageBytes = Base64.decode(listDataItem.get(position).getImageItem(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
        textView.setText(listDataItem.get(position).getImageName());
        return convertView;
    }
}
