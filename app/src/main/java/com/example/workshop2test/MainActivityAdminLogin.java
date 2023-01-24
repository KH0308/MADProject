package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.Executor;

public class MainActivityAdminLogin extends AppCompatActivity {

    EditText bsnID, bsnPass;
    Button loginAdmin;
    ConnectionClass connectionClass;
    ProgressDialog progressDialogLogIn;

    private String BsnIdStr, BsnPwdStr;
    private String ID;
    private String x;
    private boolean isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_login);

        (getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bsnID = findViewById(R.id.BusinessID);
        bsnPass = findViewById(R.id.passSeller);
        loginAdmin = findViewById(R.id.loginSeller);

        connectionClass = new ConnectionClass();
        WeakReference<Context> contextWeakReference = new WeakReference<>(this);
        progressDialogLogIn = new ProgressDialog(contextWeakReference.get());


        loginAdmin.setOnClickListener(v -> {
            BsnIdStr = bsnID.getText().toString().trim();
            BsnPwdStr = bsnPass.getText().toString().trim();

            if (TextUtils.isEmpty(BsnIdStr)) {
                bsnID.setError("Please enter your ID");
                bsnID.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(BsnPwdStr)) {
                bsnPass.setError("Please enter your password");
                bsnPass.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(BsnIdStr) || TextUtils.isEmpty(BsnPwdStr)) {
                Toast.makeText(MainActivityAdminLogin.this, "Please insert all details...", Toast.LENGTH_SHORT).show();
            } else {
                DoLoginAdmin logAdmin = new DoLoginAdmin();
                logAdmin.execute("");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class DoLoginAdmin extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {

            BsnIdStr = bsnID.getText().toString();
            BsnPwdStr = bsnPass.getText().toString();

            progressDialogLogIn.setMessage("Loading...");
            progressDialogLogIn.show();
        }

        protected String doInBackground(String... params){

            String stsAdminActive = "Active";
            String stsAdminInactive = "Inactive";
            String stsAdminUnapproved = "Unapproved";
            x = "";

            try {
                Connection con = connectionClass.CONN();
                if (con == null){
                    x = "Please check your internet connection";
                }
                else{
                    String hashedPassword = MD5.hashPassword(BsnPwdStr);
                    String query = "select * from admin where bsnID = ? and bsnPass = ?";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, BsnIdStr);
                    stmt.setString(2, hashedPassword);


                    ResultSet rs = stmt.executeQuery();

                    if(rs.next()){
                        ID = rs.getString(1);
                        String pwd = rs.getString(5);
                        String adminStatus = rs.getString(8);

                        if(ID.equals(BsnIdStr) && pwd.equals(hashedPassword) && stsAdminActive.equals(adminStatus))  {
                            x = "Login success";
                            isSuccess=true;
                            return x;
                        }if(ID.equals(BsnIdStr) && pwd.equals(hashedPassword) && stsAdminInactive.equals(adminStatus)) {
                            x = "Your account is Inactive, please contact SuperAdmin@.google.com.";
                            isSuccess=false;
                            return x;
                        }
                        else if(ID.equals(BsnIdStr) && pwd.equals(hashedPassword) && stsAdminUnapproved.equals(adminStatus))  {
                            x = "Your account is pending approval, please contact SuperAdmin@.google.com.";
                            isSuccess=false;
                            return x;
                        }
                    }
                    else{
                        x = "Invalid login details";
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return x;
        }

        @Override
        protected void onPostExecute(String s) {

            if (progressDialogLogIn != null && progressDialogLogIn.isShowing()) {
                progressDialogLogIn.dismiss();
            }
            if(isSuccess){
                try (Connection con = connectionClass.CONN()) {
                    String query = "INSERT INTO adminlog (bsnID) VALUES (?)";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, ID);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivityAdminLogin.this, MainPageSeller.class);
                intent.putExtra("shopID", ID);
                startActivity(intent);
            }
            else{
                Toast.makeText(getBaseContext(),""+ x, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {

        Toast.makeText(getBaseContext(), "Back to registration page" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityAdminLogin.this, MainActivityAdmin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}