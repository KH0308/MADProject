package com.example.workshop2test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private List<CartItem> cart;
    Button backbutton,bPlus,bMinus, Checkout;
    ImageView bin;
    EditText quantity;

    double wholetotal=0;
    double overalltotal=0;
    double overallTotalPrice=0;
    TextView textViewTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        String UserID =getIntent().getStringExtra("userID");
        final String JSON_URL= ConnectionClass.CART_URL + UserID;
        //final String JSON_URL="http://10.0.2.2/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.78.248/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.77.158/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.74.201/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://192.168.0.19:8080/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.74.174:8080/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.72.53/courseApp/cartlisting.php?userID="+UserID;


        // get the list of items from the intent
        cart =new ArrayList<>();

        // create the adapter with the list of items
       // adapter = new CartItemAdapter(cart);

        // get the RecyclerView and set the adapter and layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jsonrequest(JSON_URL);
        backbutton = findViewById(R.id.backbutton);
        Checkout = findViewById(R.id.checkout);

        //TextView totalTextView = findViewById(R.id.total_text_view);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to Home" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cart.this, MainPageCustomer.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to Home" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cart.this, PaymentActivity.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });
    }

    private void jsonrequest(String JsonURL) {
        request = new JsonArrayRequest(JsonURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        CartItem cartlist = new CartItem();
                        cartlist.setId(jsonObject.getString("id"));
                        cartlist.setItemName(jsonObject.getString("item_name"));
                        cartlist.setItemDescription(jsonObject.getString("item_description"));
                        cartlist.setItemPrice(jsonObject.getString("item_price"));
                        cartlist.setItemQuantity(jsonObject.getString("item_quantity"));
                        cartlist.setItemImage(jsonObject.getString("item_image"));
                        double itemPriceCart = Double.parseDouble(jsonObject.getString("item_price"));
                        int quantityCart = Integer.parseInt(jsonObject.getString("item_quantity"));
                        double totalPriceItem = itemPriceCart * quantityCart;
                        String totalPriceString = Double.toString(totalPriceItem);
                        cartlist.setItemTotalPrice(totalPriceString);
                        wholetotal += totalPriceItem;

                        cart.add(cartlist);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                textViewTotal= findViewById(R.id.textViewTotal);
                overallTotalPrice = wholetotal;
                textViewTotal.setText(String.format("Total: %.2f", overallTotalPrice));

                setuprecyclerview(cart);
                // progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue= Volley.newRequestQueue(Cart.this);
        requestQueue.add(request);

    }

    private void setuprecyclerview(List<CartItem> cart) {
        CartItemAdapter myadapter=new CartItemAdapter(this,cart, textViewTotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);
    }

    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to Home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Cart.this, MainPageCustomer.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }
}
