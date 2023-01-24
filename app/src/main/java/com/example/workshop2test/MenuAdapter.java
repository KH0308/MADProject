package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyMenuViewHolder> {

    Context contextMenu;
    List<Menu> menuList;
    ConnectionClass connectionClass;
    private String image;

    public static class MyMenuViewHolder extends RecyclerView.ViewHolder{
        public TextView MnItmID, ShoPID, MnID, CtgID, nm, desc, price;
        public ImageView ItmImg;
        RelativeLayout rlMenu;
        public MyMenuViewHolder(View view){
            super(view);
            MnItmID = view.findViewById(R.id.MItmID);
            ShoPID = view.findViewById(R.id.BsnID);
            MnID = view.findViewById(R.id.MID);
            CtgID = view.findViewById(R.id.CID);
            nm = view.findViewById(R.id.NAME);
            desc = view.findViewById(R.id.DESC);
            price = view.findViewById(R.id.PRICE);
            ItmImg = view.findViewById(R.id.ItmImg);
            rlMenu = view.findViewById(R.id.arrListMenu);
        }
    }

    public MenuAdapter(Context contextMenu, List<Menu> menuList){
        this.contextMenu = contextMenu;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MyMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item, parent,
                false);
        return new MyMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMenuViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Menu menu = menuList.get(position);

        holder.MnItmID.setText(menu.getMenuItemId());
        holder.ShoPID.setText(menu.getBsnId());
        holder.MnID.setText(menu.getMenuId());
        holder.CtgID.setText(menu.getCategoryId());
        holder.nm.setText(menu.getNameItem());
        holder.desc.setText(menu.getDescription());
        holder.desc.setMaxLines(Integer.MAX_VALUE);
        holder.desc.setHorizontallyScrolling(false);
        holder.price.setText(String.valueOf(menu.getPrice()));
        //Glide.with(contextMenu).load(menuList.get(position).getImgItm()).into(holder.ItmImg);

        //resize make image not clear only line 81&82 suggested to use
        byte[] bytes = Base64.decode(menu.getImgItm(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        holder.ItmImg.setImageBitmap(bitmap);

        holder.rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(contextMenu);
                dialog.setContentView(R.layout.update_delete_menu);

                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

                ImageView imgITEM = dialog.findViewById(R.id.imgItm);
                TextView  mnIDItem = dialog.findViewById(R.id.menuIdItem);
                TextView  SHOPid = dialog.findViewById(R.id.IDShop);
                TextView  MENUidTxt = dialog.findViewById(R.id.idMenu);
                TextView  CATEGORYIdTxt = dialog.findViewById(R.id.idCategory);
                Spinner MISpin = dialog.findViewById(R.id.idSpnMenuItm);
                Spinner CGTSpin = dialog.findViewById(R.id.idSpnCgtItm);
                EditText NAMEItm = dialog.findViewById(R.id.itemName);
                EditText DESCItem = dialog.findViewById(R.id.desc);
                EditText PRICEItm = dialog.findViewById(R.id.valPrice);
                Button btnEDIT = dialog.findViewById(R.id.btnEdtMenu);
                Button btnUPDATE = dialog.findViewById(R.id.btnUpdMenu);
                Button btnCANCEL = dialog.findViewById(R.id.btnClc);
                Button btnDELETE = dialog.findViewById(R.id.btnDelMenu);

                byte[] bytes = Base64.decode(menuList.get(position).getImgItm(), Base64.DEFAULT);
                Bitmap bitmapIN = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                //Glide.with(contextMenu).load(menuList.get(position).getImgItm()).into(imgITEM);
                imgITEM.setImageBitmap(bitmapIN);
                mnIDItem.setText(menuList.get(position).getMenuItemId());
                SHOPid.setText(menuList.get(position).getBsnId());
                MENUidTxt.setText(menuList.get(position).getMenuId());
                CATEGORYIdTxt.setText(menuList.get(position).getCategoryId());
                NAMEItm.setText(menuList.get(position).getNameItem());
                DESCItem.setText(menuList.get(position).getDescription());
                PRICEItm.setText(String.valueOf(menuList.get(position).getPrice()));

                btnUPDATE.setEnabled(false);
                MISpin.setEnabled(false);
                CGTSpin.setEnabled(false);
                NAMEItm.setEnabled(false);
                DESCItem.setEnabled(false);
                PRICEItm.setEnabled(false);

                //just decode to string but not re-upload / re-update image
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapIN.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageInBytes = byteArrayOutputStream.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    image = java.util.Base64.getEncoder().encodeToString(imageInBytes);
                }

                btnEDIT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnUPDATE.setEnabled(true);
                        MISpin.setEnabled(true);
                        CGTSpin.setEnabled(true);
                        NAMEItm.setEnabled(true);
                        DESCItem.setEnabled(true);
                        PRICEItm.setEnabled(true);

                    }
                });

                btnUPDATE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String MItmStr = mnIDItem.getText().toString();
                        String shopIDStr = SHOPid.getText().toString();
                        String menuIDStr = MISpin.getSelectedItem().toString();
                        String cgtIDStr = CGTSpin.getSelectedItem().toString();
                        String nameITEMStr = NAMEItm.getText().toString();
                        String descITEMStr = DESCItem.getText().toString();
                        double priceITEMStr = Double.parseDouble(PRICEItm.getText().toString());
                        String imageITMStr = image;

                        menuList.set(position, new Menu(imageITMStr, MItmStr, shopIDStr, menuIDStr, cgtIDStr,
                                nameITEMStr, descITEMStr, priceITEMStr));
                        notifyItemChanged(position);

                        connectionClass = new ConnectionClass();

                        try{
                            Connection con = connectionClass.CONN();
                            if (con == null){
                                Toast.makeText(contextMenu, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            } else{
                                //String query to update all except image
                                String query = "UPDATE menuitem SET menuID = '"+menuIDStr+"',mCgtryID = '"
                                        +cgtIDStr+"',name = '"+nameITEMStr+"',description = '"
                                        +descITEMStr+"',price = '"+priceITEMStr+"' WHERE mItemID = '"
                                        +MItmStr+"' and bsnID = '"+shopIDStr+"'";

                                //String query to update image together with others details
                                /*String query = "UPDATE menuitem SET menuID = '"+menuIDStr+"',mCgtryID = '"
                                        +cgtIDStr+"',name = '"+nameITEMStr+"',description = '"
                                        +descITEMStr+"',price = '"+priceITEMStr+"',ItemImage = '"
                                        +imageITMStr+"' WHERE mItemID = '"+MItmStr+"' and bsnID = '"
                                        +shopIDStr+"'";*/

                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);

                                Toast.makeText(contextMenu, "Successfully update menu item with ID" + MItmStr, Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                btnCANCEL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnDELETE.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        String MItmStr = mnIDItem.getText().toString();
                        String shopIDStr = SHOPid.getText().toString();
                        String menuIDStr = MISpin.getSelectedItem().toString();
                        String cgtIDStr = CGTSpin.getSelectedItem().toString();
                        String nameITEMStr = NAMEItm.getText().toString();
                        String descITEMStr = DESCItem.getText().toString();
                        double priceITEMStr = Double.parseDouble(PRICEItm.getText().toString());
                        String imageITMStr = image;

                        /*menuList.set(position, new Menu(imageITMStr, MItmStr, shopIDStr, menuIDStr, cgtIDStr,
                                nameITEMStr, descITEMStr, priceITEMStr));
                        notifyItemChanged(position);*/

                        notifyDataSetChanged();

                        connectionClass = new ConnectionClass();
                        try{
                            Connection con = connectionClass.CONN();
                            if (con == null){
                                Toast.makeText(contextMenu, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            } else{
                                String query = "DELETE FROM menuitem WHERE menuitem.mItemID = '"
                                        +MItmStr+"' AND menuitem.bsnID = '"+shopIDStr+"'";

                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);

                                Toast.makeText(contextMenu, "Successfully delete menu item with ID" + MItmStr, Toast.LENGTH_SHORT).show();

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
        return menuList.size();
    }
}
