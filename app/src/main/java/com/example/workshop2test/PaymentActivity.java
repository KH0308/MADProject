package com.example.workshop2test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;


public class PaymentActivity extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView2;
    private AdapterListItemOrder adapter;
    private List<ListItemOrder> list;

    ConnectionClass connectionClass;

    double TaxCharge = 0.06;
    double Subtotal = 0.00;
    double wholetotal = 0.00;
    double tax = 0.00;
    double totalIncludeTax = 0.00;

    String TtlIncTax;

    Button backButton, btnProceed;
    Spinner paySpn;
    TextView taxTxt, subTotalTxt, totalIncludeTxt, paymentID, orderID, dateOdrPay, timeOdrPay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        String UserID =getIntent().getStringExtra("userID");
        final String JSON_URL= ConnectionClass.CART_URL + UserID;
        //final String JSON_URL="http://10.0.2.2/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.78.248/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://10.131.74.174:8080/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://192.168.0.19:8080/courseApp/cartlisting.php?userID="+UserID;
        //final String JSON_URL="http://192.168.0.69/courseApp/cartlisting.php?userID="+UserID;

        connectionClass = new ConnectionClass();

        // get the list of items from the intent
        list = new ArrayList<>();

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setAdapter(adapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        jsonrequest(JSON_URL);

        backButton = findViewById(R.id.backButton);
        btnProceed = findViewById(R.id.btnProceed);
        paySpn = findViewById(R.id.paymentSpinner);
        subTotalTxt = findViewById(R.id.subTotalTxt);
        taxTxt = findViewById(R.id.taxTxt);
        totalIncludeTxt = findViewById(R.id.totalIncTax);
        paymentID = findViewById(R.id.payID);
        orderID = findViewById(R.id.odrID);
        dateOdrPay = findViewById(R.id.datePayOdr);
        timeOdrPay = findViewById(R.id.timePayOdr);

        String ranOdrId = UUID.randomUUID().toString().substring(0, 8);
        String ranPayId = UUID.randomUUID().toString().substring(0, 8);

        ZoneId zone = ZoneId.of("Asia/Kuala_Lumpur");
        LocalDate date = LocalDate.now(zone);
        LocalTime time = LocalTime.now(zone);

        paymentID.setText(ranPayId);
        orderID.setText(ranOdrId);
        dateOdrPay.setText(date.toString());
        timeOdrPay.setText(time.toString());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to Home" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this, Cart.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
                finish();
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String odrIDStr = orderID.getText().toString();

                // user id pastu
                String statusOdr = "Pending";
                String dateStr = dateOdrPay.getText().toString();
                String timeStr = timeOdrPay.getText().toString();
                String TtlPrcOdrStr = totalIncludeTxt.getText().toString();
                //payment start masuk
                String payIDStr = paymentID.getText().toString();

                String payIDsame = payIDStr;
                String OdrIDsame = odrIDStr;
                // user id n bsn id
                String optPay = paySpn.getSelectedItem().toString();
                // masuk total pay include tax like TtlPrcOdrStr lagi n date time lagi
                String stsPay = "Success";
                String stsOdrItem = "Pending";

                Connection con = connectionClass.CONN();
                if (con == null) {
                    Toast.makeText(getBaseContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    PreparedStatement PSOdrUser = null;

                        PreparedStatement PSPayOdr = null;
                        try {
                            PSPayOdr = con.prepareStatement(
                                    "INSERT INTO payment (payID, userID, payOpt, totalPay, payStatus) VALUES (?, ?, ?, ?, ?)");
                                    //"INSERT INTO payment (payID, odruserID, userID, payOpt, totalPay, payTime, payDate, payStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                            for (ListItemOrder data : list){
                                PSPayOdr.setString(1, payIDsame);
                                //PSPayOdr.setString(2, OdrIDsame); this been removed from database
                                PSPayOdr.setString(2, UserID);
                                PSPayOdr.setString(3, optPay);
                                PSPayOdr.setString(4, TtlIncTax);
                                //PSPayOdr.setString(6, timeStr);
                                PSPayOdr.setString(5, stsPay);

                                PSPayOdr.execute();

                                PreparedStatement PSOdrItem = null;
                                PreparedStatement OdrTransaction = null;
                                PreparedStatement removeCart = null;
                                try {

                                    OdrTransaction = con.prepareStatement(
                                            "INSERT INTO transaction (mItemID, item_name, bsnID, bsnName, item_quantity, total_price, status, payID,  userID, itemImage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                                    for (ListItemOrder data2 : list) {
                                        //OdrTransaction.setString(1, data2.getTransID());
                                        OdrTransaction.setString(1, data2.getItmID());
                                        OdrTransaction.setString(2, data2.getItmName());
                                        OdrTransaction.setString(3, data2.getShpID());
                                        OdrTransaction.setString(4, data2.getShopName());
                                        OdrTransaction.setString(5, String.valueOf(data2.getItmQuantity()));
                                        OdrTransaction.setString(6, String.valueOf(data2.getItmTotalPrice()));
                                        OdrTransaction.setString(7, stsOdrItem); //status
                                        //OdrTransaction.setString(8, timeStr); //time
                                        OdrTransaction.setString(8, payIDsame); //payID
                                        //OdrTransaction.setString(9, OdrIDsame); //odrID has been removed
                                        OdrTransaction.setString(9, UserID);
                                        OdrTransaction.setString(10, data2.getItmImage());

                                        OdrTransaction.execute();

                                    }

                                    //remove cart item and send intent back to main page
                                    removeCart = con.prepareStatement(
                                            "delete from cart where userID = ?");
                                    removeCart.setString(1, UserID);

                                    removeCart.execute();
                                    Toast.makeText(getBaseContext(), "Successful add your order", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaymentActivity.this, SuccesPayment.class);
                                    intent.putExtra("userID", UserID);
                                    startActivity(intent);
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                     finally {
                        // Close the connection to the database
                        try {
                            con.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //intent lama sini
            }
        });
    }

    private void jsonrequest(String JsonURL) {
        request = new JsonArrayRequest(JsonURL, new Response.Listener<JSONArray>() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    String ranTransId = UUID.randomUUID().toString().substring(0, 8);
                    try {
                        jsonObject = response.getJSONObject(i);
                        ListItemOrder listItemOrders = new ListItemOrder();
                        //edit sini last
                        //listItemOrders.setTransID(ranTransId);
                        //listItemOrders.setTransID(jsonObject.getString("id"));
                        listItemOrders.setShpID(jsonObject.getString("bsnID"));
                        listItemOrders.setShopName(jsonObject.getString("bsnName"));
                        listItemOrders.setItmID(jsonObject.getString("mItemID"));
                        listItemOrders.setItmName(jsonObject.getString("item_name"));
                        listItemOrders.setItmDescription(jsonObject.getString("item_description"));
                        listItemOrders.setItmPrice(Double.parseDouble(jsonObject.getString("item_price")));
                        listItemOrders.setItmQuantity(Integer.valueOf(jsonObject.getString("item_quantity")));
                        //make use total_price in table cart
                        listItemOrders.setItmTotalPrice(Double.parseDouble(jsonObject.getString("total_price")));
                        double totalPriceItem = Double.parseDouble(jsonObject.getString("total_price"));
                        wholetotal += totalPriceItem;
                        //set item image on Checkout
                        listItemOrders.setItmImage(jsonObject.getString("item_image"));
                        list.add(listItemOrders);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Subtotal = wholetotal;
                tax = wholetotal * TaxCharge;
                totalIncludeTax = wholetotal + tax;
                DecimalFormat dfSubTotal = new DecimalFormat("#.##");
                DecimalFormat dfTax = new DecimalFormat("#.##");
                DecimalFormat dfTtlIncTax = new DecimalFormat("#.##");
                String SubTotal = dfSubTotal.format(Subtotal);
                TtlIncTax = dfTtlIncTax.format(totalIncludeTax);
                String Tax = dfTax.format(tax);
                subTotalTxt.setText(SubTotal);
                taxTxt.setText(Tax);
                totalIncludeTxt.setText(TtlIncTax);

                setuprecyclerview(list);
                // progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue= Volley.newRequestQueue(PaymentActivity.this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<ListItemOrder> listItemOrders) {
        adapter = new AdapterListItemOrder(this, listItemOrders, totalIncludeTxt);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2.setAdapter(adapter);
    }

    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to user profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PaymentActivity.this, Cart.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }

}