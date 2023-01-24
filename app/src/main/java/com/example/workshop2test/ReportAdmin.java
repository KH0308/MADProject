package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReportAdmin extends AppCompatActivity {

    TextView txtSales, vSlsTotal, txtSalesMonth,vSlsMonth;
    Button btnFilterSlc, btnBck;
    RecyclerView rcReport;
    GridView gvReport;
    ScrollView svReport;
    Spinner spnMonthRpt;

    List<Report> reportList = new ArrayList<>();
    List<ImageIHSales> imageIHSalesList = new ArrayList<>();
    ReportAdapter reportAdapter;
    IHSalesAdapter ihSalesAdapter;

    ConnectionClass connectionClass;
    String x = "";
    String StrValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_admin);

        txtSales = findViewById(R.id.textTSales);
        vSlsTotal = findViewById(R.id.TSales);
        txtSalesMonth = findViewById(R.id.textMSales);
        vSlsMonth = findViewById(R.id.MSales);
        btnFilterSlc = findViewById(R.id.btnSelect);
        btnBck = findViewById(R.id.backHomeSeller);
        rcReport = findViewById(R.id.RVTransaction);
        gvReport = findViewById(R.id.GVSalesItem);
        svReport = findViewById(R.id.SVTransaction);
        spnMonthRpt = findViewById(R.id.spinSalesMonth);

        String shopID = getIntent().getStringExtra("shopID");

        connectionClass = new ConnectionClass();

        rcReport.setAdapter(reportAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        rcReport.setLayoutManager(linearLayoutManager);
        gvReport.setAdapter(ihSalesAdapter);

        rcReport.setHasFixedSize(true);

        new ReportAdmin.getReportSales().execute("");

        new ReportAdmin.getReportItemSales().execute("");

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            }
            else {
                String query = "SELECT SUM(total_price) AS 'Total Overall Sales' FROM transaction WHERE bsnID = '"
                        +shopID+"'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    String OvlSales = rs.getString("Total Overall Sales");

                    vSlsTotal.setText(OvlSales);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportAdmin.this, "Back to main page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainPageSeller.class);
                intent.putExtra("shopID", shopID);
                startActivity(intent);
            }
        });

        btnFilterSlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSlsMonth.setText(StrValue);
                new ReportAdmin.getReportSalesMonth().execute("");
            }
        });

        gvReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Populate the details view with the item's data
                Intent intent = new Intent(getApplicationContext(), DetailsHotItemSales.class);
                intent.putExtra("shopID", imageIHSalesList.get(position).getShopId());
                intent.putExtra("itemID", imageIHSalesList.get(position).getItemID());
                intent.putExtra("itemName", imageIHSalesList.get(position).getImageName());
                intent.putExtra("itemImg", imageIHSalesList.get(position).getImageItem());
                startActivity(intent);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public class getReportSales extends AsyncTask<String, String, String> {

        String shopID = getIntent().getStringExtra("shopID");

        @Override
        protected void onPreExecute() {
            reportList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {

            connectionClass =  new ConnectionClass();

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    x = "Please check your internet connection";
                }
                else {
                    String query = "SELECT * FROM transaction WHERE bsnID = '" + shopID + "'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String TransID = rs.getString(1);
                        String ITEMName = rs.getString(3);
                        double ttlPrice = Double.parseDouble(rs.getString(7));
                        String time = rs.getString(9);
                        String date = rs.getString(10);
                        String year = rs.getString(11);

                        Report report = new Report(TransID, ITEMName, time, date, year, ttlPrice);
                        report.setTscId(TransID);
                        report.setItmName(ITEMName);
                        report.setTime(time);
                        report.setDate(date);
                        report.setYear(year);
                        report.setTotalPrice(ttlPrice);

                        reportList.add(report);
                        x = "Successfully refresh list report";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            reportAdapter = new ReportAdapter(ReportAdmin.this,reportList);
            rcReport.setAdapter(reportAdapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getReportItemSales extends AsyncTask<String, String, String>{

        String shopID = getIntent().getStringExtra("shopID");

        @Override
        protected void onPreExecute() {
            imageIHSalesList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {

            connectionClass = new ConnectionClass();

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    x = "Please check your internet connection";
                }
                else {
                    String query = "SELECT itemImage, mItemID, item_name, sum(item_quantity) FROM transaction WHERE bsnID = '"
                            +shopID+"' group by itemImage order by sum(item_quantity) desc limit 3;";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()){
                        String itemID = rs.getString("mItemID");
                        String img = rs.getString("itemImage");
                        String nameImg = rs.getString("item_name");

                        ImageIHSales imageIHSales = new ImageIHSales(itemID, shopID, img, nameImg);
                        imageIHSales.setItemID(itemID);
                        imageIHSales.setShopId(shopID);
                        imageIHSales.setImageItem(img);
                        imageIHSales.setImageName(nameImg);

                        imageIHSalesList.add(imageIHSales);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            ihSalesAdapter = new IHSalesAdapter(ReportAdmin.this,imageIHSalesList);
            gvReport.setAdapter(ihSalesAdapter);

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getReportSalesMonth extends AsyncTask<String, String, String> {

        String shopID = getIntent().getStringExtra("shopID");
        String monthSelected = spnMonthRpt.getSelectedItem().toString();
        //String valueMonth = vSlsMonth.getText().toString();

        @Override
        protected void onPreExecute() {
            reportList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            connectionClass =  new ConnectionClass();

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    x = "Please check your internet connection";
                }
                else {
                    String query = "SELECT * FROM transaction WHERE bsnID = '"
                            +shopID+"' AND monthname(date) = '"+monthSelected+"'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String TransID = rs.getString(1);
                        String ITEMName = rs.getString(3);
                        double ttlPrice = Double.parseDouble(rs.getString(7));
                        String time = rs.getString(9);
                        String date = rs.getString(10);
                        String year = rs.getString(11);

                        Report report = new Report(TransID, ITEMName, time, date, year, ttlPrice);
                        report.setTscId(TransID);
                        report.setItmName(ITEMName);
                        report.setTime(time);
                        report.setDate(date);
                        report.setYear(year);
                        report.setTotalPrice(ttlPrice);

                        reportList.add(report);

                        connectionClass =  new ConnectionClass();
                        try {
                            Connection con2 = connectionClass.CONN();
                            if (con2 == null) {
                                x = "Please check your internet connection";
                            }
                            else {
                                String queryVM = "SELECT SUM(total_price) AS 'Month Sales' FROM transaction WHERE bsnID = '"
                                        +shopID+"' AND monthname(date) = '"+monthSelected+"'";

                                Statement stmtNext = con.createStatement();
                                ResultSet rsNext = stmtNext.executeQuery(queryVM);
                                while (rsNext.next()){
                                    double VSalesMonth = Double.parseDouble(rsNext.getString("Month Sales"));

                                    StrValue = String.valueOf(VSalesMonth);
                                    x = "Report based on month " + monthSelected;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            reportAdapter = new ReportAdapter(ReportAdmin.this,reportList);
            rcReport.setAdapter(reportAdapter);
        }
    }
}