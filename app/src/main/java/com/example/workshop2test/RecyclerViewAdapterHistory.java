package com.example.workshop2test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.MyView> {

    private Context mContext;
    private List<HistoryList> mData;
    private final RecyclerViewInterface recyclerViewInterface;

    public RecyclerViewAdapterHistory(Context mContext, List<HistoryList>mData, RecyclerViewInterface recyclerViewInterface){
        this.mContext=mContext;
        this.mData=mData;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.history_row_list,parent,false);

        return new MyView(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {

        holder.OdrPayID.setText(mData.get(position).getPayID());
        //holder.OdrUserID.setText(mData.get(position).getOdruserID());
        holder.OdrPayOption.setText(mData.get(position).getPayOpt());
        holder.OdrTotalPay.setText("RM "+mData.get(position).getTotalPay());
        holder.OdrTime.setText(mData.get(position).getPayTime());
        holder.OdrDate.setText(mData.get(position).getPayDate());
        holder.OdrStatus.setText(mData.get(position).getPayStatus());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyView extends RecyclerView.ViewHolder{

        TextView OdrPayID, OdrUserID, OdrPayOption, OdrTotalPay, OdrTime, OdrDate ,OdrStatus;

        public MyView(View view,RecyclerViewInterface recyclerViewInterface){
            super(view);

            OdrPayID= itemView.findViewById(R.id.ODRPayID);
            //OdrUserID= itemView.findViewById(R.id.ODRUserID);
            OdrPayOption= itemView.findViewById(R.id.payOption);
            OdrTotalPay= itemView.findViewById(R.id.totalPrice);
            OdrTime= itemView.findViewById(R.id.timestamp);
            OdrDate= itemView.findViewById(R.id.timestampDate);
            OdrStatus= itemView.findViewById(R.id.status);

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
