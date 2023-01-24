package com.example.workshop2test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivityUserLogin extends AppCompatActivity {

    private EditText userID, userPass;
    private Button loginUser;
    private ProgressDialog progressDialog;
    private ConnectionClass connectionClass;
    private String userIdStr;
    private String userPwdStr;
    private String x;
    private boolean isSuccess;
    private String ID, Pwd, userName;
    private String userStatus = "Active";
    private String DEuserStatus = "Deactivated";
    private String userStatusChecker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_login);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userID = findViewById(R.id.userID);
        userPass = findViewById(R.id.userPass);
        userPass.setTransformationMethod(new PasswordTransformationMethod());
        loginUser = findViewById(R.id.loginUser);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        loginUser.setOnClickListener(v -> {
            userIdStr = userID.getText().toString().trim();
            userPwdStr = userPass.getText().toString().trim();

            if (TextUtils.isEmpty(userIdStr)) {
                userID.setError("Please enter user ID");
                userID.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(userPwdStr)) {
                userPass.setError("Please enter password");
                userPass.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(userIdStr) || TextUtils.isEmpty(userPwdStr)) {
                Toast.makeText(MainActivityUserLogin.this, "Please insert all details...", Toast.LENGTH_SHORT).show();
            } else {
                DoLoginUser loginUser = new DoLoginUser();
                loginUser.execute("");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class DoLoginUser extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            userIdStr = userID.getText().toString();
            userPwdStr = userPass.getText().toString();

            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        protected String doInBackground(String... params) {

            x = "";
            userStatus = "Active";
            DEuserStatus = "Deactivated";
            try (Connection con = connectionClass.CONN()){
                if (con == null){
                    x = "Please check your internet connection";
                }else{
                    String hashedPassword = MD5.hashPassword(userPwdStr);
                    String query = "select * from user where userID = ? and password = ?";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, userIdStr);
                    stmt.setString(2, hashedPassword);

                    ResultSet rs = stmt.executeQuery();

                    if(rs.next()){
                        ID = rs.getString("userID");
                        userName = rs.getString("userName");
                        Pwd = rs.getString("password");
                        userStatusChecker = rs.getString("statusUser");



                        if(ID.equals(userIdStr) && Pwd.equals(hashedPassword) && userStatusChecker.equals(userStatus))  {
                            x = "Login success";
                            isSuccess=true;
                            return x;
                        }

                        else if(ID.equals(userIdStr) && Pwd.equals(hashedPassword) && userStatusChecker.equals(DEuserStatus))  {
                            x = "Your account is deactivated, please contact admin.";
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


        protected void onPostExecute(String result){
            progressDialog.dismiss();
            if(isSuccess) {
                try (Connection con = connectionClass.CONN()) {
                    String query = "INSERT INTO userlog (userID) VALUES (?)";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, ID);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivityUserLogin.this, MainPageCustomer.class);
                intent.putExtra("userID", ID);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(),""+ x, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {

        Toast.makeText(getBaseContext(), "Back to registration page" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityUserLogin.this, MainActivityUser.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}