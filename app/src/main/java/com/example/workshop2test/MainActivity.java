package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.workshop2test.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //Button GoCustomer, GoSeller;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCust.setOnClickListener(this::fnGoCustomer);
        binding.btnAdmin.setOnClickListener(this::fnGoAdmin);

    }

    public void fnGoAdmin(View view) {
        Intent intent = new Intent(this,MainActivityAdmin.class);
        startActivity(intent);
        finish();
    }

    public void fnGoCustomer(View view) {
        Intent intent = new Intent(this, MainActivityUser.class);
        startActivity(intent);
        finish();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press back again to exit TapN'Eat", Toast.LENGTH_SHORT).show();
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