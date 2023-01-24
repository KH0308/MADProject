package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetailsHotItemSales extends AppCompatActivity {

    TextView itemName, quantity, SalesTotal, priceItem;
    ImageView imageItem;
    Button backReport;

    ConnectionClass connectionClass;
    String x = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_hot_item_sales);

        itemName = findViewById(R.id.nameITEM);
        quantity = findViewById(R.id.qtyITEM);
        priceItem = findViewById(R.id.priceITEM);
        SalesTotal = findViewById(R.id.ttlSalesITEM);
        imageItem = findViewById(R.id.imageItm);
        backReport = findViewById(R.id.btnBckRpt);

        String shopID = getIntent().getStringExtra("shopID");
        String itemID = getIntent().getStringExtra("itemID");
        String ItemName = getIntent().getStringExtra("itemName");
        String itemImg = getIntent().getStringExtra("itemImg");

        connectionClass = new ConnectionClass();

        byte[] bytes = Base64.decode(itemImg, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageItem.setImageBitmap(decodedBitmap);
        itemName.setText(ItemName);

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            }
            else {
                String query = "SELECT menuitem.price AS 'Price Item', SUM(total_price) AS 'Total Sales', SUM(item_quantity) AS 'Total Quantity' FROM transaction, menuitem WHERE transaction.bsnID = '"
                        +shopID+"' AND transaction.mItemID = '"+itemID+"' AND menuitem.bsnID = '"+shopID+"' AND menuitem.mItemID = '"+itemID+"'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    String PricePerItem = rs.getString("Price Item");
                    String TtlSlsItem = rs.getString("Total Sales");
                    String TtlQtySls = rs.getString("Total Quantity");

                    quantity.setText(TtlQtySls);
                    priceItem.setText(PricePerItem);
                    SalesTotal.setText(TtlSlsItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        backReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportAdmin.class);
                intent.putExtra("shopID", shopID);
                startActivity(intent);
            }
        });
    }
}