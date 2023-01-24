package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutApplication extends AppCompatActivity {
    TextView abtUsTxt, AbtUs, NeedHelp, Contact;
    Button backSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        Fragment fragment = new Map_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frmLayout,fragment).commit();

        String shopID = getIntent().getStringExtra("shopID");

        abtUsTxt = findViewById(R.id.ABOUTUS);
        AbtUs = findViewById(R.id.aboutUS);
        NeedHelp = findViewById(R.id.ndHelp);
        Contact = findViewById(R.id.ctcUs);
        backSeller = findViewById(R.id.btnBckSlr);

        backSeller.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainPageSeller.class);
            intent.putExtra("shopID", shopID);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        String shopID = getIntent().getStringExtra("shopID");
        Intent intent = new Intent(AboutApplication.this, ProfileAdmin.class);
        intent.putExtra("shopID", shopID);
        startActivity(intent);
    }
}