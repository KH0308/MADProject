package com.example.workshop2test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class ProfileAdmin extends AppCompatActivity {

    DrawerLayout drawerLayoutAdmin;
    ActionBarDrawerToggle toggleAdminProfile;
    NavigationView navigationViewAdmin;
    TextView shopName, shopEmail, Name, Email, Phone, Password;
    ImageView ShopLogo, profile;
    ScrollView scrollView;
    ConnectionClass connectionClass;
    String x = "";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggleAdminProfile.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);

        drawerLayoutAdmin = findViewById(R.id.drawerLayoutAdmin);
        navigationViewAdmin = findViewById(R.id.navProfileAdmin);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.mail);
        Phone = findViewById(R.id.phone);
        Password = findViewById(R.id.pwd);
        profile = findViewById(R.id.profilePicture);
        scrollView = findViewById(R.id.SVProfile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggleAdminProfile = new ActionBarDrawerToggle(this,
                drawerLayoutAdmin, R.string.navigation_drawer_open_admin,
                R.string.navigation_drawer_close_admin);
        toggleAdminProfile.syncState();

        drawerLayoutAdmin.addDrawerListener(toggleAdminProfile);

        connectionClass = new ConnectionClass();

        String shopID = getIntent().getStringExtra("shopID");

        View header = navigationViewAdmin.getHeaderView(0);
        ShopLogo = header.findViewById(R.id.LogoShop);
        shopName = header.findViewById(R.id.ShopName);
        shopEmail = header.findViewById(R.id.EmailContactShop);

        navigationViewAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.optHome){
                    Intent intent = new Intent(getApplicationContext(), MainPageSeller.class);
                    intent.putExtra("shopID", shopID);
                    startActivity(intent);
                }


                else if (id == R.id.optEditProfile){

                    loadFragment(new EditProfileAdminFragment());

                }else if (id == R.id.optDeleteProfile){

                    loadFragment(new DeleteProfileAdminFragment());
                    //Toast.makeText(ProfileAdmin.this, "Setting", Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent = new Intent(getApplicationContext(), AboutApplication.class);
                    intent.putExtra("shopID", shopID);
                    startActivity(intent);
                }

                drawerLayoutAdmin.closeDrawer(GravityCompat.START);

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
                    String bsnPhone =rs.getString(4);
                    String bsnPassword = rs.getString(5);


                    byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ShopLogo.setImageBitmap(bitmap);
                    shopName.setText(bsnName);
                    shopEmail.setText(bsnEmail);

                    profile.setImageBitmap(bitmap);
                    Name.setText("Business Name: " + bsnName);
                    Email.setText("Business Email: " +bsnEmail);
                    Phone.setText("Business Contact: " +bsnPhone);
                    Password.setText("Business Password: " +bsnPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutAdmin.isDrawerOpen(GravityCompat.START)){
            drawerLayoutAdmin.closeDrawer(GravityCompat.START);
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
        ft.replace(R.id.contProfileAdmin, fragment);
        ft.commit();
    }
}