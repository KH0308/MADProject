package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterListItemOrder extends RecyclerView.Adapter<AdapterListItemOrder.MyOdrItm> {

    private Context context;
    private List<ListItemOrder> data;
    double overallTotalPrice = 0.00;
    TextView tvTotal;

    public AdapterListItemOrder(Context context, List<ListItemOrder> data, TextView tvTotal) {
        this.context = context;
        this.data = data;
        this.tvTotal = tvTotal;
    }

    @NonNull
    @Override
    public MyOdrItm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.list_order_item, parent, false);

        return new MyOdrItm(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOdrItm holder, int position) {
        int adapterPosition = holder.getAdapterPosition();

        holder.nameFood.setText(data.get(adapterPosition).getItmName());
        holder.nameShop.setText(data.get(adapterPosition).getShopName());
        holder.foodDesc.setText(data.get(adapterPosition).getItmDescription());
        holder.qty.setText(String.valueOf(data.get(adapterPosition).getItmQuantity()));
        holder.prc.setText(String.valueOf(data.get(adapterPosition).getItmPrice()));
        double totalPrice = Double.parseDouble(String.valueOf(data.get(adapterPosition).getItmTotalPrice()));
        @SuppressLint("DefaultLocale") String formattedTotalPrice = String.format("%.2f", totalPrice);
        holder.TtlPrcItem.setText(formattedTotalPrice);
        overallTotalPrice += Double.parseDouble(String.valueOf(data.get(adapterPosition).getItmTotalPrice()));

        byte[] bytes = Base64.decode(data.get(position).getItmImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.itemImage.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyOdrItm extends RecyclerView.ViewHolder{

        TextView nameFood, nameShop, foodDesc, qty, prc, TtlPrcItem;
        ImageView itemImage;

        public MyOdrItm(@NonNull View view) {
            super(view);

            nameFood = view.findViewById(R.id.itemNameOdr);
            nameShop = view.findViewById(R.id.shopNameOdr);
            foodDesc = view.findViewById(R.id.itemDescriptionOdr);
            qty = view.findViewById(R.id.itemQuantityOdr);
            prc = view.findViewById(R.id.itemPrice);
            TtlPrcItem = view.findViewById(R.id.itemTotalPrice);
            itemImage = view.findViewById(R.id.CheckoutThumbnail);

        }
    }
}
