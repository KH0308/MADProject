package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionClass {

    //private static final String IPADDRESSCONFIG = "192.168.137.1";
    //IP Testing from Mobile Hotspot feature on Windows 10
    //IP LOCALHOST EMULATOR
    //private static final String IPADDRESSCONFIG = "10.0.2.2";
    //Simply edit this to easily update
    //khai ip
    private static final String IPADDRESSCONFIG = "192.168.105.105";
    //private static final String IPADDRESSCONFIG = "192.168.0.15";
    //private static final String IPADDRESSCONFIG = "10.131.78.148";

    public static final String CART_URL = "http://"+IPADDRESSCONFIG+"/courseApp/cartlisting.php?userID=";
    //CART WILL BE REQUIRED BY cart CLASS AND PaymentActivity CLASS
    public static final String PAYMENT_URL = "http://"+IPADDRESSCONFIG+"/courseApp/paymentlisting.php?userID=";
    //PAYMENT WILL BE REQUIRED BY History feature in Customer on OrderHistory CLASS
    public static final String HISTORY_URL = "http://"+IPADDRESSCONFIG+"/courseApp/historylisting.php?payID=";
    //HISTORY URL WILL BE REQUIRED BY History feature in Customer on OrderHistoryDetails CLASS
    public static final String SHOP_URL = "http://"+IPADDRESSCONFIG+"/courseApp/shoplisting.php";
    //THIS WILL BE MAINLY BE USED TO LISTING SHOPLIST ON MainPageCustomer CLASS
    public static final String MENU_URL = "http://"+IPADDRESSCONFIG+"/courseApp/menulisting.php?bsnID=";
    //THIS WILL BE MAINLY BE USED TO LISTING MENULIST ON CustomerMenuListByShop CLASS

    private static final String classs = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://"+IPADDRESSCONFIG+":3306/ws";
    private static final String un = "SuperAdmin";
    private static final String password = "AdminSup123";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //String ConnURL = null;
        Connection conn = null;
        try {

            Class.forName(classs).newInstance();

            conn = DriverManager.getConnection(url, un, password);


            //conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }

    public void delete(String id) {
        Connection conn = CONN();
        try {
            String query = "DELETE FROM Cart WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateItemQuantity(String id, int newQuantity ) {
        Connection conn = CONN();
        try {
            String query = "UPDATE Cart SET item_quantity = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
