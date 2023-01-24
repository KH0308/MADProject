package com.example.workshop2test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyAdapterReport> {

    Context context;
    List<Report> reportList;

    public static class MyAdapterReport extends RecyclerView.ViewHolder{
        public TextView TrsID, nameItem, TotalPrc, timeSls, dateSls, yearSls;
        public MyAdapterReport(View v){
            super(v);
            TrsID = v.findViewById(R.id.TscID);
            nameItem = v.findViewById(R.id.ItmNm);
            TotalPrc = v.findViewById(R.id.TtlPrice);
            timeSls = v.findViewById(R.id.timeSales);
            dateSls = v.findViewById(R.id.dateSales);
            yearSls = v.findViewById(R.id.yearSales);
        }
    }

    public ReportAdapter (Context context, List<Report> reportList){
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public MyAdapterReport onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list_item, parent, false);
        return new ReportAdapter.MyAdapterReport(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterReport holder, int position) {
        Report report = reportList.get(position);

        holder.TrsID.setText(report.getTscId());
        holder.nameItem.setText(report.getItmName());
        holder.TotalPrc.setText(String.valueOf(report.getTotalPrice()));
        holder.timeSls.setText(report.getTime());
        holder.dateSls.setText(report.getDate());
        holder.yearSls.setText(report.getYear());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
