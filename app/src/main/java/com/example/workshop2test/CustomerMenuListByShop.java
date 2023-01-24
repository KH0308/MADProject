package com.example.workshop2test;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerMenuListByShop extends AppCompatActivity implements RecyclerViewInterface {
    ConnectionClass connectionClass;

    ListView menuList;
    Button backToMenu;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    List<MenuList> menuListList;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu_list_by_shop);

        backToMenu = findViewById(R.id.bBtnMenuList);
        // Get the shop ID from the intent
        String bsnID = getIntent().getStringExtra("bsnID");

        // Get the user ID from the intent
        String UserID =getIntent().getStringExtra("userID");

        // Construct the URL for the JSON request
        //final String JSON_URL = "http://192.168.0.69/courseApp/menulisting.php?bsnID=" + bsnID;

        final String JSON_URL = ConnectionClass.MENU_URL + bsnID;

        menuListList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerViewMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        jsonrequest(JSON_URL);

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerMenuListByShop.this, MainPageCustomer.class);

                intent.putExtra("userID", UserID); //session userID
                //intent for remaining item in cart
                startActivity(intent);
            }
        });
    }

    private void jsonrequest(String JsonURL) {
        // create a new request object using the JsonArrayRequest class
        request = new JsonArrayRequest(JsonURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // create a JSONObject to store data from the response
                JSONObject jsonObject = null;

                // loop through the response array
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // get the current JSON object
                        jsonObject = response.getJSONObject(i);
                        // create a new MenuList object
                        MenuList menuList = new MenuList();
                        // set the values for the object
                        menuList.setBusinessID(jsonObject.getString("bsnID"));
                        menuList.setFoodId(jsonObject.getString("mItemID"));
                        menuList.setDescription(jsonObject.getString("description"));
                        menuList.setFoodName(jsonObject.getString("name"));
                        menuList.setPrice(jsonObject.getString("price"));
                        menuList.setFoodPic(jsonObject.getString("ItemImage"));
                        // add the object to the menuListList
                        menuListList.add(menuList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // set up the RecyclerView with the list of menu items
                setuprecyclerview(menuListList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handle any errors that may occur while making the request
            }
        });

        // create a new request queue
        requestQueue= Volley.newRequestQueue(CustomerMenuListByShop.this);
        // add the request to the queue
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<MenuList> menuListList) {
        RecyclerViewAdapter myadapter=new RecyclerViewAdapter(this,menuListList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);

    }

    @Override
    public void onItemClick(int position) {
        String UserID =getIntent().getStringExtra("userID");
        String ShopName = getIntent().getStringExtra("bsnName");
        Intent intent=new Intent(CustomerMenuListByShop.this,AddToCart.class);
        intent.putExtra("userID", UserID);
        intent.putExtra("bsnName", ShopName);
        intent.putExtra("itemID", menuListList.get(position).getFoodId());
        intent.putExtra("bsnID", menuListList.get(position).getBusinessID());
        intent.putExtra("description", menuListList.get(position).getDescription());
        intent.putExtra("name", menuListList.get(position).getFoodName());
        intent.putExtra("price", menuListList.get(position).getPrice());
        intent.putExtra("itemIMG", menuListList.get(position).getFoodPic());
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        String UserID =getIntent().getStringExtra("userID");
        Toast.makeText(getBaseContext(), "Back to Home Page" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CustomerMenuListByShop.this, MainPageCustomer.class);
        intent.putExtra("userID", UserID);
        startActivity(intent);
    }
}