package com.example.workshop2test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuManagement extends AppCompatActivity {

    TextView shopName,shopEmail;
    ImageView ShopLogo;
    Button refresh;

    List<Order> OrderListItem = new ArrayList<>();
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    ScrollView scrollView;

    ConnectionClass connectionClass;
    String x = "";

    DrawerLayout drawerLayoutMenuOrder;
    ActionBarDrawerToggle toggleAdminMM;
    NavigationView NavMMAdmin;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggleAdminMM.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_management);

        drawerLayoutMenuOrder = findViewById(R.id.drawerLayoutMenu);
        scrollView = findViewById(R.id.SVMenuOrder);
        recyclerView = findViewById(R.id.RVMenuOrder);
        NavMMAdmin = findViewById(R.id.navMMAdmin);
        refresh = findViewById(R.id.refreshBtn);

        //scrollView.addView(linearLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        connectionClass = new ConnectionClass();

        String shopID = getIntent().getStringExtra("shopID");

        new MenuManagement.getOrderItemList().execute("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggleAdminMM = new ActionBarDrawerToggle(this,
                drawerLayoutMenuOrder, R.string.navigation_drawer_open_admin,
                R.string.navigation_drawer_close_admin);
        toggleAdminMM.syncState();

        drawerLayoutMenuOrder.addDrawerListener(toggleAdminMM);

        View header = NavMMAdmin.getHeaderView(0);
        ShopLogo = header.findViewById(R.id.LogoShop);
        shopName = header.findViewById(R.id.ShopName);
        shopEmail = header.findViewById(R.id.EmailContactShop);

        NavMMAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.optHome){
                    Intent intent = new Intent(getApplicationContext(), MainPageSeller.class);
                    intent.putExtra("shopID", shopID);
                    startActivity(intent);
                }
                else if (id == R.id.optLiveOrderRefresh){
                    Toast.makeText(MenuManagement.this, "Live order success refresh", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MenuManagement.class);
                    intent.putExtra("shopID", shopID);
                    startActivity(intent);

                }else{
                    Toast.makeText(MenuManagement.this, "Menu Management", Toast.LENGTH_SHORT).show();
                    loadFragment(new ListEditDeleteMenu(getApplicationContext()));
                }

                drawerLayoutMenuOrder.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            }
            else {
                String query = "select * from admin where bsnID = '" + shopID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    String image = rs.getString(7);
                    String bsnName = rs.getString(2);
                    String bsnEmail =rs.getString(3);


                    byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    ShopLogo.setImageBitmap(bitmap);
                    shopName.setText(bsnName);
                    shopEmail.setText(bsnEmail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuManagement.this, "Live order success refresh", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuManagement.class);
                intent.putExtra("shopID", shopID);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class getOrderItemList extends AsyncTask<String, String, String> {

        String shopID = getIntent().getStringExtra("shopID");

        @Override
        protected void onPreExecute() {
            OrderListItem.clear();
        }

        @Override
        protected void onPostExecute(String s) {
            orderAdapter = new OrderAdapter(MenuManagement.this,OrderListItem);
            recyclerView.setAdapter(orderAdapter);
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
                    String query = "SELECT * FROM transaction WHERE bsnID = '" + shopID + "'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()){
                        String TransOdrID = rs.getString(1);
                        String menuName = rs.getString(3);
                        String shopID = rs.getString(4);
                        int qty = Integer.parseInt(rs.getString(6));
                        double prc = Double.parseDouble(rs.getString(7));
                        String StsOdr = rs.getString(8);

                        Order order = new Order(TransOdrID, menuName, shopID, qty, prc, StsOdr);
                        order.setTransID(TransOdrID);
                        order.setMenuName(menuName);
                        order.setShopID(shopID);
                        order.setQuantity(qty);
                        order.setPrice(prc);
                        order.setStatusOdr(StsOdr);

                        OrderListItem.add(order);
                        x = "Successfully refresh list order";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutMenuOrder.isDrawerOpen(GravityCompat.START)){
            drawerLayoutMenuOrder.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fragment) {

        String shopID = getIntent().getStringExtra("shopID");
        Bundle args = new Bundle();
        args.putString("ID", shopID);
        fragment.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.mainPgMM, fragment);
        ft.commit();

    }
}