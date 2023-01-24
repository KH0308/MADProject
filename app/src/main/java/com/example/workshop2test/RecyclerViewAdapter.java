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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<MenuList>mData;
    private final RecyclerViewInterface recyclerViewInterface;
    //RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<MenuList> mData, RecyclerViewInterface recyclerViewInterface){
        this.mContext=mContext;
        this.mData=mData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.menu_row_list,parent,false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.businessID.setText(mData.get(position).getBusinessID());
        holder.foodName.setText(mData.get(position).getFoodName());
        holder.description.setText(mData.get(position).getDescription());
        holder.price.setText(mData.get(position).getPrice());

        byte[] bytes = Base64.decode(mData.get(position).getFoodPic(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.foodPic.setImageBitmap(decodedBitmap);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView businessID, foodName, description, price;
        ImageView foodPic;

        public MyViewHolder(View view,RecyclerViewInterface recyclerViewInterface){
            super(view);

            businessID= itemView.findViewById(R.id.busID);
            foodName= itemView.findViewById(R.id.sName);
            description= itemView.findViewById(R.id.desc);
            price= itemView.findViewById(R.id.foodPrice);
            foodPic= itemView.findViewById(R.id.thumbnailItem);
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
        }
    }

}
