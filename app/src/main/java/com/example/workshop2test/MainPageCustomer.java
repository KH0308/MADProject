package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class MainPageCustomer extends AppCompatActivity implements RecyclerViewInterface{


    //private List<CartItem> cart;
    ConnectionClass connectionClass;
    //String connectionresult="";
    TextView nametextUser,name;
    String x = "";
    Button userProfile, btnHistory, btnCart;
    ListView shopList;

    //private final String JSON_URL="http://192.168.0.69/courseApp/
    private final String JSON_URL= ConnectionClass.SHOP_URL;


    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<ShopList> shopListList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_cutomer);
        //cart = (List<CartItem>) getIntent().getSerializableExtra("cart");
        shopListList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        jsonrequest();


        //nametextUser = findViewById(R.id.hello);
        name = findViewById(R.id.name);

        connectionClass = new ConnectionClass();
        String UserID =getIntent().getStringExtra("userID");


        btnCart = findViewById(R.id.btn2);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Entered Your Cart" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPageCustomer.this, Cart.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });

        btnHistory = findViewById(R.id.btn3);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Entered Order History" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPageCustomer.this, OrderHistory.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });

        userProfile = findViewById(R.id.btn4);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Entered User Profile" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPageCustomer.this, UserProfile.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            } else {
                String query = "select * from user where userID='" + UserID + "'";


                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String userName = rs.getString("userName");
                    name.setText("Welcome,  "+userName);
                    //It should display on name textview as the username from database
                    //And it's worked.
                }


            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ShopList shopList = new ShopList();
                        shopList.setBusinessID(jsonObject.getString("bsnID"));
                        shopList.setshopName(jsonObject.getString("bsnName"));
                        shopList.setLogoShop(jsonObject.getString("logoShop"));
                        shopListList.add(shopList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(shopListList);
                 progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue= Volley.newRequestQueue(MainPageCustomer.this);
        requestQueue.add(request);


    }

    private void setuprecyclerview(List<ShopList> shopListList) {
        RecyclerViewAdapterShop myadapter=new RecyclerViewAdapterShop(this,shopListList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);

    }


    @Override
    public void onItemClick(int position) {
        String UserID =getIntent().getStringExtra("userID");
        Intent intent=new Intent(MainPageCustomer.this,CustomerMenuListByShop.class);
        String ShopName = shopListList.get(position).getshopName();
        Toast.makeText(getBaseContext(), "Entered "+ShopName+" menu list", Toast.LENGTH_SHORT).show();
        intent.putExtra("userID", UserID);
        intent.putExtra("bsnID", shopListList.get(position).getBusinessID());
        intent.putExtra("bsnName", shopListList.get(position).getshopName());
        startActivity(intent);

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

