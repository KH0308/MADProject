package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity implements RecyclerViewInterface {

    ConnectionClass connectionClass;
    Button backbutton;
    String x = "";

    //private final String JSON_URL="http://192.168.0.69/courseApp/historylist.php";

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<HistoryList> historyListList;
    private RecyclerView recyclerView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        String UserID =getIntent().getStringExtra("userID");
        final String JSON_URL= ConnectionClass.PAYMENT_URL + UserID;
        //final String JSON_URL="http://10.0.2.2/courseApp/paymentlisting.php?userID="+UserID;
        //final String JSON_URL="http://192.168.0.69/courseApp/paymentlisting.php?userID="+UserID;


        historyListList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        jsonrequest(JSON_URL);

        connectionClass = new ConnectionClass();



        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to home page" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderHistory.this, MainPageCustomer.class);
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
                        HistoryList historyList = new HistoryList();
                        historyList.setPayID(jsonObject.getString("payID"));
                        //historyList.setOdruserID(jsonObject.getString("odruserID"));
                        historyList.setPayOpt(jsonObject.getString("payOpt"));
                        historyList.setTotalPay(jsonObject.getString("totalPay"));
                        historyList.setPayTime(jsonObject.getString("payTime"));
                        historyList.setPayDate(jsonObject.getString("payDate"));
                        historyList.setPayStatus(jsonObject.getString("payStatus"));


                        historyListList.add(historyList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(historyListList);
                // progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue= Volley.newRequestQueue(OrderHistory.this);
        requestQueue.add(request);


    }

    private void setuprecyclerview(List<HistoryList> historyListList) {
        RecyclerViewAdapterHistory myadapter=new RecyclerViewAdapterHistory(this,historyListList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);
    }

    @Override
    public void onItemClick(int position){
        String UserID =getIntent().getStringExtra("userID");
        Intent intent=new Intent(OrderHistory.this, OrderHistoryListDetails.class);
        String PayID= historyListList.get(position).getPayID();
        Toast.makeText(getBaseContext(), "Pay ID : "+PayID+" detail list", Toast.LENGTH_SHORT).show();
        intent.putExtra("userID", UserID);
        //intent.putExtra("payID", PayID);
        intent.putExtra("payID", historyListList.get(position).getPayID());
        startActivity(intent);
    }
    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to Home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(OrderHistory.this, MainPageCustomer.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }

}