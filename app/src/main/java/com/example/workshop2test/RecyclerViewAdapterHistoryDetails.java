package com.example.workshop2test;

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

public class RecyclerViewAdapterHistoryDetails extends RecyclerView.Adapter<RecyclerViewAdapterHistoryDetails.MyViewHolderDetails>{

    private Context mContext;
    private List<HistoryListDetails> mData;

    public RecyclerViewAdapterHistoryDetails(Context mContext, List<HistoryListDetails>mData){
        this.mContext=mContext;
        this.mData=mData;
    }

    //sambung sini last buat
    @NonNull
    @Override
    public MyViewHolderDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.history_row_list_details, parent, false);

        return new MyViewHolderDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderDetails holder, int position) {
        holder.shpName.setText(mData.get(position).getDetailNameShop());
        holder.itmName.setText(mData.get(position).getDetailItemName());
        holder.price.setText("RM "+mData.get(position).getDetailPrice());
        holder.quantity.setText("QTY: "+mData.get(position).getDetailQuantity());
        holder.status.setText(mData.get(position).getDetailStatus());
        holder.date.setText(mData.get(position).getDetailDate());
        holder.time.setText(mData.get(position).getDetailTime());

        byte[] bytes = Base64.decode(mData.get(position).getDetailImgShop(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.itemImg.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolderDetails extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView shpName, itmName, price, quantity, date, time, status;
        public MyViewHolderDetails(@NonNull View itemView) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.DetailShopIMAGE);
            shpName = itemView.findViewById(R.id.DetailBSNName);
            itmName = itemView.findViewById(R.id.DetailItemName);
            price = itemView.findViewById(R.id.DetailPrice);
            quantity = itemView.findViewById(R.id.DetailQuantity);
            status = itemView.findViewById(R.id.DetailItemStatus);
            date = itemView.findViewById(R.id.DetailDate);
            time = itemView.findViewById(R.id.DetailTime);
        }
    }
}
