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

public class OrderHistoryListDetails extends AppCompatActivity {

    //ConnectionClass connectionClass;
    Button btnBack;
    String x = "";

    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    List<HistoryListDetails> historyListDetails;
    //RecyclerView rcHLD;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_history_list);

        String UserID =getIntent().getStringExtra("userID");
        //String odrUserID =getIntent().getStringExtra("ODRUserId");
        String PayID =getIntent().getStringExtra("payID");

        //connectionClass = new ConnectionClass();
        final String JSON_URL= ConnectionClass.HISTORY_URL + PayID;
        //final String JSON_URL="http://10.0.2.2/courseApp/historylisting.php?payID="+PayID;
        //final String JSON_URL="http://10.131.78.248/courseApp/historylisting.php?payID="+PayID;

        historyListDetails = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        jsonrequest(JSON_URL);

        btnBack = findViewById(R.id.BACKBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to order list page" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderHistoryListDetails.this, OrderHistory.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });
    }

    private void jsonrequest(String JsonURL) {
        jsonArrayRequest = new JsonArrayRequest(JsonURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        HistoryListDetails HLDetails = new HistoryListDetails();

                        //HLDetails.setDetailItemID(jsonObject.getString("mItemID"));
                        HLDetails.setDetailItemName(jsonObject.getString("item_name"));
                        //HLDetails.setDetailBsnID(jsonObject.getString("bsnID"));
                        HLDetails.setDetailNameShop(jsonObject.getString("bsnName"));
                        HLDetails.setDetailQuantity(jsonObject.getString("item_quantity"));
                        HLDetails.setDetailPrice(jsonObject.getString("total_price"));
                        HLDetails.setDetailStatus(jsonObject.getString("status"));
                        HLDetails.setDetailTime(jsonObject.getString("time"));
                        HLDetails.setDetailDate(jsonObject.getString("date"));
                        HLDetails.setDetailImgShop(jsonObject.getString("itemImage"));
                        //HLDetails.setDetailPayID(jsonObject.getString("payID"));

                        historyListDetails.add(HLDetails);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(historyListDetails);
                // progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue= Volley.newRequestQueue(OrderHistoryListDetails.this);
        requestQueue.add(jsonArrayRequest);
    }

    private void setuprecyclerview(List<HistoryListDetails> historyListDetails) {
        //buat java class for adapter sambung...
        RecyclerViewAdapterHistoryDetails RVAHistoryDetails = new RecyclerViewAdapterHistoryDetails(this,historyListDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(RVAHistoryDetails);
    }

    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to History", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(OrderHistoryListDetails.this, OrderHistory.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }

}