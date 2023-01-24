package com.example.workshop2test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telecom.Call;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterShop extends RecyclerView.Adapter<RecyclerViewAdapterShop.MyViewHolderShop> {

    private Context mContext;
    private List<ShopList>mData;
    private final RecyclerViewInterface recyclerViewInterface;
    //RequestOptions option;

    public RecyclerViewAdapterShop(Context mContext, List<ShopList> mData, RecyclerViewInterface recyclerViewInterface){
        this.mContext=mContext;
        this.mData=mData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolderShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.shop_row_list,parent,false);

        return new MyViewHolderShop(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderShop holder, int position) {
        holder.businessID.setText(mData.get(position).getBusinessID());
        holder.shopName.setText(mData.get(position).getshopName());

        byte[] bytes = Base64.decode(mData.get(position).getLogoShop(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.shopPic.setImageBitmap(decodedBitmap);

        //holder.itemView.setOnClickListener(view ->){
        //mItem.onItemClick(mData.get(position));
        //};

        //Glide.with(mContext).load(mData.get(position).getFoodPic()).apply(option).into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolderShop extends RecyclerView.ViewHolder{

        TextView businessID, shopName;
        ImageView shopPic;

        public MyViewHolderShop(View view,RecyclerViewInterface recyclerViewInterface){
            super(view);

            businessID= itemView.findViewById(R.id.busID);
            shopName= itemView.findViewById(R.id.sName);
            shopPic = itemView.findViewById(R.id.thumbnailShop);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface !=null){
                        int pos=getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }

                    }
                }
            });
            //foodPic= itemView.findViewById(R.id.shopName);
        }
    }

}
