package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainPageSeller extends AppCompatActivity {

    TextView shopName, shopEmail, shopNamePage, totalCustomer, totalSales;
    ImageView ShopLogo, iconCustomer, iconSales;
    ConnectionClass connectionClass;
    String x = "";

    List<ImageIHSales> list = new ArrayList<>();
    GridView gvHotItem;
    IHSalesAdapter ImgHotSlsAdapter;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navMainAdmin;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_seller);

        drawerLayout = findViewById(R.id.drawerLayoutMainAdmin);
        navMainAdmin = findViewById(R.id.navMainAdmin);
        shopNamePage = findViewById(R.id.businessName);
        iconCustomer = findViewById(R.id.TotalCustomer);
        iconSales = findViewById(R.id.TotalSales);
        gvHotItem = findViewById(R.id.gridViewHotItem);
        totalCustomer = findViewById(R.id.ttlCustomer);
        totalSales = findViewById(R.id.ttlSales);

        new MainPageSeller.getIHSales().execute("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open_admin,R.string.navigation_drawer_close_admin);
        actionBarDrawerToggle.syncState();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        connectionClass = new ConnectionClass();

        //Admin admin = (Admin) getIntent().getSerializableExtra("objAdmin");
        String shopID = getIntent().getStringExtra("shopID");

        View header = navMainAdmin.getHeaderView(0);
        ShopLogo = header.findViewById(R.id.LogoShop);
        shopName = header.findViewById(R.id.ShopName);
        shopEmail = header.findViewById(R.id.EmailContactShop);

        navMainAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.optHome:
                        intent = new Intent(getApplicationContext(), MainPageSeller.class);
                        intent.putExtra("shopID", shopID);
                        startActivity(intent);
                        return true;
                    case R.id.optProfile:
                        intent = new Intent(getApplicationContext(), ProfileAdmin.class);
                        intent.putExtra("shopID", shopID);
                        startActivity(intent);
                        return true;
                    case R.id.optManageShop:
                        //Toast.makeText(getApplicationContext(), "You navigated to Manage Shop screen", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), MenuManagement.class);
                        intent.putExtra("shopID", shopID);
                        startActivity(intent);
                        return true;
                    case R.id.optReportShop:
                        //Toast.makeText(getApplicationContext(), "You navigated to Report Shop screen", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), ReportAdmin.class);
                        intent.putExtra("shopID", shopID);
                        startActivity(intent);
                        return true;
                    case R.id.optLogOut:
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainPageSeller.this);
                        builder.setTitle("ALERT!");
                        builder.setMessage("Do you want to LogOut?");
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //finish();
                                Toast.makeText(getBaseContext(), "Successfully Logout" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainPageSeller.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                        return true;
                    default:
                        return false;
                }
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

                while (rs.next()) {
                    String image = rs.getString(7);
                    String bsnName = rs.getString(2);
                    String bsnEmail =rs.getString(3);

                    byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ShopLogo.setImageBitmap(bitmap);
                    shopNamePage.setText(bsnName);
                    shopName.setText(bsnName);
                    shopEmail.setText(bsnEmail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connectionClass = new ConnectionClass();
        try {
            Connection con2 = connectionClass.CONN();
            if (con2 == null) {
                x = "Please check your internet connection";
            }
            else{
                String query2 = "SELECT COUNT(DISTINCT userID) AS 'Total Customer', SUM(total_price) AS 'Total Sales' FROM transaction WHERE bsnID = '"
                        +shopID+"'";

                Statement stmt2 = con2.createStatement();
                ResultSet rs2 = stmt2.executeQuery(query2);

                while (rs2.next()) {
                    String TC = rs2.getString("Total Customer");
                    String TS = rs2.getString("Total Sales");
                    String textRM = "RM ";
                    String textUser = " Customer";

                    totalCustomer.setText(String.format("%s%s", TC, textUser));
                    totalSales.setText(String.format("%s%s", textRM, TS));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getIHSales extends AsyncTask<String,String,String>{

        String shopID = getIntent().getStringExtra("shopID");

        @Override
        protected void onPreExecute() {
            list.clear();
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
                    String query = "SELECT mItemID, itemImage, item_name, sum(item_quantity) FROM transaction WHERE bsnID = '"
                            +shopID+"' group by itemImage order by sum(item_quantity) desc limit 3;";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()){
                        String itemID = rs.getString("mItemID");
                        String img = rs.getString("itemImage");
                        String nameImg = rs.getString("item_name");

                        ImageIHSales imageIHSales = new ImageIHSales(itemID, shopID, img, nameImg);
                        imageIHSales.setItemID(itemID);
                        imageIHSales.setShopId(shopID);
                        imageIHSales.setImageItem(img);
                        imageIHSales.setImageName(nameImg);

                        list.add(imageIHSales);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            ImgHotSlsAdapter = new IHSalesAdapter(MainPageSeller.this,list);
            gvHotItem.setAdapter(ImgHotSlsAdapter);
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}