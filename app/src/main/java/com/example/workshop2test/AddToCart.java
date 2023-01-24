package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.example.workshop2test.Cart;

public class AddToCart extends AppCompatActivity {

    ConnectionClass connectionClass;
    TextView descript, itemname, itemprice;
    ImageView imgItm;
    Button bBtn, bAdd, bPlus, bMinus;
    EditText quantityEdit;
    String x = "";
    int quantity = 1;
    private List<CartItem> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        connectionClass = new ConnectionClass();
        bBtn = findViewById(R.id.bBtn);
        bAdd = findViewById(R.id.bAddToCart);
        bMinus = findViewById(R.id.bMinus);
        bPlus = findViewById(R.id.bPlus);
        itemname = findViewById(R.id.fName);
        itemprice = findViewById(R.id.pFood);
        descript = findViewById(R.id.descript);
        quantityEdit = findViewById(R.id.qFood2);
        imgItm = findViewById(R.id.imageView3);
        String UserID = getIntent().getStringExtra("userID");
        String ShopName = getIntent().getStringExtra("bsnName");
        String BusinessID = getIntent().getStringExtra("bsnID");
        String ItemID = getIntent().getStringExtra("itemID");
        String Description = getIntent().getStringExtra("description");
        String ItemName = getIntent().getStringExtra("name");
        String ItemPrice = getIntent().getStringExtra("price");
        String ImageITEM = getIntent().getStringExtra("itemIMG");

        itemname.setText(ItemName);
        itemprice.setText(" Price : RM " + ItemPrice);
        descript.setText(" Description : \n" + Description);

        byte[] bytes = Base64.decode(ImageITEM, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imgItm.setImageBitmap(decodedBitmap);

        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to " + ShopName + " menu list", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddToCart.this, CustomerMenuListByShop.class);
                intent.putExtra("userID", UserID); //session userID
                intent.putExtra("bsnID", BusinessID);
                intent.putExtra("bsnName", ShopName);
                //intent for remaining item in cart
                startActivity(intent);
            }
        });


        bMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int currentQuantity=Integer.parseInt(quantityEdit.getText().toString());
                //currentQuantity--;
                //quantityEdit.setText(String.valueOf(currentQuantity));
                if (quantity <= 0) {
                    Toast.makeText(getBaseContext(), "Quantity cannot be less than 0", Toast.LENGTH_SHORT).show();
                } else {
                    int currentQuantity=Integer.parseInt(quantityEdit.getText().toString());
                    currentQuantity--;
                    // update the quantity EditText with the new value
                    quantityEdit.setText(String.valueOf(currentQuantity));
                }
            }
        });

        bPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity=Integer.parseInt(quantityEdit.getText().toString());
                currentQuantity++;
                quantityEdit.setText(String.valueOf(currentQuantity));
            }
        });

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemname.getText().toString();
                String itemQuantity = quantityEdit.getText().toString();

                // Open a connection to the database
                Connection con = connectionClass.CONN();
                if (con == null) {
                    Toast.makeText(getBaseContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a prepared statement to insert the item into the cart table
                    PreparedStatement preparedStatement = null;
                    try {
                        String sql = "INSERT INTO cart (item_name, mItemID, item_price, item_description, item_quantity, userID, bsnID, bsnName, item_image) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE item_quantity = item_quantity + ?";
                        preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, itemName);
                        preparedStatement.setString(2, ItemID);
                        preparedStatement.setString(3, ItemPrice);
                        preparedStatement.setString(4, Description);
                        preparedStatement.setString(5, itemQuantity);
                        preparedStatement.setString(6, UserID);
                        preparedStatement.setString(7, BusinessID);
                        preparedStatement.setString(8, ShopName);
                        preparedStatement.setString(9, ImageITEM);
                        preparedStatement.setString(10, itemQuantity);

                        // Execute the prepared statement
                        preparedStatement.executeUpdate();
                        Toast.makeText(getBaseContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                        // Intent intent = new Intent(AddToCart.this, Cart.class);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        // Close the connection to the database
                        try {
                            con.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed(String UserID, String BusinessID,String ShopName ) {
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        if (doubleBackToExitPressedOnce) {
            Toast.makeText(getBaseContext(), "Back to " + ShopName + " menu list", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddToCart.this, CustomerMenuListByShop.class);
            intent.putExtra("userID", UserID); //session userID
            intent.putExtra("bsnID", BusinessID);
            intent.putExtra("bsnName", ShopName);
            //intent for remaining item in cart
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

    @Override
    public void onBackPressed() {
        String UserID =getIntent().getStringExtra("userID");
        String ShopName = getIntent().getStringExtra("bsnName");
        String BusinessID = getIntent().getStringExtra("bsnID");
        Toast.makeText(getBaseContext(), "Back to " + ShopName + " menu list", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddToCart.this, CustomerMenuListByShop.class);
        intent.putExtra("userID", UserID); //session userID
        intent.putExtra("bsnID", BusinessID);
        intent.putExtra("bsnName", ShopName);
        //intent for remaining item in cart
        startActivity(intent);
    }


}