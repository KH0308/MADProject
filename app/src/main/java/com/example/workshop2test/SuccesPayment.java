package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SuccesPayment extends AppCompatActivity {

    Button HomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_payment);

        String UserID =getIntent().getStringExtra("userID");

        HomeButton = findViewById(R.id.HomeButton);

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drop data dlm cart sini
                Toast.makeText(getBaseContext(), "Back to Home" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SuccesPayment.this, MainPageCustomer.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to Home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SuccesPayment.this, MainPageCustomer.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }
}