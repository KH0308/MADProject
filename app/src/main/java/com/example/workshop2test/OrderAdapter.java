package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    List<Order> listOrderItem;
    ConnectionClass connectionClass;
    String y = "";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView transID, nameMenu, Qty, Prc, stsOdr;
        RelativeLayout rl;
        public MyViewHolder(View v) {
            super(v);
            transID = v.findViewById(R.id.transId);
            nameMenu = v.findViewById(R.id.MenuName);
            Qty = v.findViewById(R.id.Quantity);
            Prc = v.findViewById(R.id.Price);
            stsOdr = v.findViewById(R.id.stsOdr);
            rl = v.findViewById(R.id.arrStsRow);
        }
    }

    public OrderAdapter(Context context, List<Order> listOrderItem) {
        this.listOrderItem = listOrderItem;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = listOrderItem.get(position);

        holder.transID.setText("ID: \n" + order.getTransID());
        holder.nameMenu.setText("Name: \n" + order.getMenuName());
        holder.Qty.setText("Quantity: \n" + order.getQuantity());
        holder.Prc.setText("Price: \n" + order.getPrice());
        holder.stsOdr.setText("Status: \n" +order.getStatusOdr());
        //holder.spnSts.setSelection(Integer.parseInt(order.getStatusOdr()));

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_odr_status);

                TextView IDTrans = dialog.findViewById(R.id.transEdtID);
                TextView itmMenuName = dialog.findViewById(R.id.menuEdtName);
                TextView QTY = dialog.findViewById(R.id.quantityEdt);
                TextView PRC = dialog.findViewById(R.id.priceEdt);
                Spinner spnStatus = dialog.findViewById(R.id.StsOdrSpinner);
                Button btnUpd = dialog.findViewById(R.id.btnUpd);

                IDTrans.setText(listOrderItem.get(position).getTransID());
                itmMenuName.setText(listOrderItem.get(position).getMenuName());
                String ShopID = listOrderItem.get(position).getShopID();
                QTY.setText(String.valueOf( listOrderItem.get(position).getQuantity()));
                PRC.setText(String.valueOf(listOrderItem.get(position).getPrice()));
                //spnStatus.setSelected(Boolean.parseBoolean(listOrderItem.get(position).getStatusOdr()));

                btnUpd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String TrnID = IDTrans.getText().toString();
                        String mName = itmMenuName.getText().toString();
                        int Qty = Integer.parseInt(QTY.getText().toString());
                        double Prc = Double.parseDouble(PRC.getText().toString());
                        String statusOdr = spnStatus.getSelectedItem().toString();

                        listOrderItem.set(position, new Order(TrnID, mName, ShopID, Qty, Prc, statusOdr));
                        notifyItemChanged(position);

                        connectionClass = new ConnectionClass();

                        try{
                            Connection con = connectionClass.CONN();
                            if (con == null){
                                y = "Please check your internet connection";
                            } else{
                                String query = "UPDATE transaction SET status = '"+statusOdr+"' WHERE transID = '"+TrnID+"' AND bsnID = '"+ ShopID +"'";

                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);

                                y = "Successfully update status order";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOrderItem.size();
    }
}
