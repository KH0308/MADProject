package com.example.workshop2test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ListEditDeleteMenu extends Fragment {

    View LEDMenu;
    RecyclerView rcView;
    ScrollView scView;

    ImageView img;
    EditText shp, mid, iNm, price, desc;
    Spinner spnMT, spnCtg;
    Button bAdd, addNewMenu;

    ConnectionClass connectionClass;
    String x = "";
    String y = "";

    List<Menu> itemListMenu = new ArrayList<>();
    MenuAdapter menuAdapter;
    Context cxtMenu;

    private static final int PICK_IMAGE_REQUEST = 99;
    private String image;

    public ListEditDeleteMenu(Context cxtMenu) {
        // Required empty public constructor
        this.cxtMenu = cxtMenu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LEDMenu = inflater.inflate(R.layout.fragment_list_edit_delete_menu, container, false);

        rcView = LEDMenu.findViewById(R.id.RVMenuList);
        scView = LEDMenu.findViewById(R.id.SVMenuList);
        addNewMenu = LEDMenu.findViewById(R.id.addMenuNew);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false);
        rcView.setLayoutManager(linearLayoutManager);

        rcView.setHasFixedSize(true);

        connectionClass = new ConnectionClass();

        new ListEditDeleteMenu.getListMenuItem().execute("");

        String id = getArguments().getString("ID");

        addNewMenu.setOnClickListener(this::fnAddNewMenu);

        return LEDMenu;
    }

    @SuppressLint("StaticFieldLeak")
    public class getListMenuItem extends AsyncTask<String,String,String>{

        String id = getArguments().getString("ID");

        @Override
        protected void onPreExecute() {
            itemListMenu.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            connectionClass = new ConnectionClass();

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    x = "Please check your internet connection";
                }
                else {
                    String query = "SELECT * FROM menuitem WHERE bsnID = '"+id+"'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()){
                        String MenuItemID = rs.getString(1);
                        String BSnID = rs.getString(2);
                        String MenuID = rs.getString(3);
                        String CategoryID = rs.getString(4);
                        String NameItem = rs.getString(5);
                        String Desc = rs.getString(6);
                        double Price = Double.parseDouble(rs.getString(7));
                        String ImageItem = rs.getString(8);

                        Menu menu = new Menu(ImageItem, MenuItemID, BSnID, MenuID, CategoryID, NameItem, Desc, Price);
                        menu.setImgItm(ImageItem);
                        menu.setMenuItemId(MenuItemID);
                        menu.setBsnId(BSnID);
                        menu.setMenuId(MenuID);
                        menu.setCategoryId(CategoryID);
                        menu.setNameItem(NameItem);
                        menu.setDescription(Desc);
                        menu.setPrice(Price);

                        itemListMenu.add(menu);
                        x = "Done";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            menuAdapter = new MenuAdapter(getActivity(), itemListMenu);
            rcView.setAdapter(menuAdapter);
            //super.onPostExecute(s);
        }
    }

    private void chooseImageMenu(){
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                    data.getData() != null){
                Uri imagePath = data.getData();
                Bitmap imageToStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                //line 185 only to use to scale image size
                //imageToStore = Bitmap.createScaledBitmap(imageToStore, 150, 150, true);
                img.setImageBitmap(imageToStore);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageInBytes = byteArrayOutputStream.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    image = Base64.getEncoder().encodeToString(imageInBytes);
                }
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void fnAddNewMenu(View view){
        Dialog dialogAddMenu = new Dialog(view.getContext());
        dialogAddMenu.setContentView(R.layout.add_menu_admin);

        Window window = dialogAddMenu.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        String id = getArguments().getString("ID");
        String ranMItemID = UUID.randomUUID().toString().substring(0, 8);

        img = dialogAddMenu.findViewById(R.id.imageItm);
        shp = dialogAddMenu.findViewById(R.id.ShopID);
        mid = dialogAddMenu.findViewById(R.id.menuItmID);
        iNm = dialogAddMenu.findViewById(R.id.ItmName);
        price = dialogAddMenu.findViewById(R.id.prc);
        desc = dialogAddMenu.findViewById(R.id.Desc);
        spnMT = dialogAddMenu.findViewById(R.id.spnMenuId);
        spnCtg = dialogAddMenu.findViewById(R.id.spnMenuCtg);
        bAdd = dialogAddMenu.findViewById(R.id.fabAddMenu);

        mid.setText(ranMItemID);
        shp.setText(id);
        mid.setEnabled(false);
        shp.setEnabled(false);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageMenu();
            }
        });

        bAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String shpStr = shp.getText().toString();
                String midStr = mid.getText().toString();
                String iNmStr = iNm.getText().toString();
                String priceStr = price.getText().toString();
                String descStr = desc.getText().toString();
                String spnMTStr = spnMT.getSelectedItem().toString();
                String spnCtgStr = spnCtg.getSelectedItem().toString();
                String imgStr = image;

                if (shpStr.trim().equals("")|| midStr.trim().equals("") ||iNmStr.trim().equals("")
                        ||priceStr.trim().equals("") ||descStr.trim().equals("") ||spnMTStr.trim().equals("")
                        || spnCtgStr.trim().equals("") ||imgStr == null) {
                    Toast.makeText(getActivity(), "Please insert all details!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        Connection con = connectionClass.CONN();
                        if (con == null){
                            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String query = "insert into menuitem values('"+midStr+"','"+shpStr+"','"+spnMTStr+"','"
                                    +spnCtgStr+"','"+iNmStr+"','"+descStr+"','"+priceStr+"','"+imgStr+"')";

                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                            double prc = Double.parseDouble(priceStr);

                            Menu menu = new Menu(imgStr, midStr, shpStr, spnMTStr, spnCtgStr, iNmStr, descStr, prc);
                            menu.setImgItm(imgStr);
                            menu.setMenuItemId(midStr);
                            menu.setBsnId(shpStr);
                            menu.setMenuId(spnMTStr);
                            menu.setCategoryId(spnCtgStr);
                            menu.setNameItem(iNmStr);
                            menu.setDescription(descStr);
                            menu.setPrice(prc);

                            itemListMenu.add(menu);
                            menuAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Successfully add item in your menu " +
                                    "shop.\n Please refresh your list menu", Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e)
                    {
                        y = "Exceptions" + e;
                    }
                    dialogAddMenu.dismiss();
                }
            }
        });
        dialogAddMenu.show();
    }
}