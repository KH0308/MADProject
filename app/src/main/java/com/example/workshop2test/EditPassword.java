package com.example.workshop2test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditPassword extends AppCompatActivity {


    private String oldPwd,newPwd,newPwdCo, oldPasswordSQL;
    ConnectionClass connectionClass;
    Button backButton, saveButton;
    TextView currentUserID, currentUserName;
    EditText edtOldPassword,edtNewPassword,edtNewPasswordConfirm;
    String x = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        String UserID =getIntent().getStringExtra("userID");
        connectionClass = new ConnectionClass();

        currentUserID =findViewById(R.id.currentUserID);
        currentUserName =findViewById(R.id.currentUserName);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtOldPassword.setTransformationMethod(new PasswordTransformationMethod());
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        edtNewPasswordConfirm = findViewById(R.id.edtNewPasswordConfirm);
        edtNewPasswordConfirm.setTransformationMethod(new PasswordTransformationMethod());

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            } else {

                String query = "select * from user where userID='" + UserID + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    String userID = rs.getString("userID");
                    currentUserID.setText(userID);

                    String userName = rs.getString("userName");
                    currentUserName.setText(userName);

                    oldPasswordSQL = rs.getString("password");

                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        }


        backButton = findViewById(R.id.backButtonPassword);
        backButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to User Profile" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditPassword.this, UserProfile.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
            }
        });

        saveButton = findViewById(R.id.btnSavePassword);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String oldPassword = edtOldPassword.getText().toString();
                final String newPassword = edtNewPassword.getText().toString();
                final String newPasswordConfirm = edtNewPasswordConfirm.getText().toString();

                String hashedOldPassword = MD5.hashPassword(oldPassword);
                String hashedNewPassword = MD5.hashPassword(newPassword);
                String hashedNewPasswordConfirm = MD5.hashPassword(newPasswordConfirm);

                oldPwd = edtOldPassword.getText().toString().trim();
                newPwd = edtNewPassword.getText().toString().trim();
                newPwdCo = edtNewPassword .getText().toString().trim();

                if (TextUtils.isEmpty(oldPwd)) {
                    edtOldPassword.setError("Please enter current password");
                    edtOldPassword.requestFocus();
                    return;
                }

                if (!hashedOldPassword.equals(oldPasswordSQL)) {
                    edtOldPassword.setError("Current password does not match with database.");
                    edtOldPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPwd)) {
                    edtNewPassword.setError("Please enter new password");
                    edtNewPassword.requestFocus();
                    return;
                }

                if (newPwd.length() < 8) {
                    edtNewPassword.setError("Password should be at least 8 characters long");
                    edtNewPassword.requestFocus();
                    return;
                }

                if (hashedNewPassword.equals(oldPasswordSQL)) {
                    edtNewPassword.setError("Do not use the same password");
                    edtNewPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPwdCo)) {
                    edtNewPasswordConfirm.setError("Please re-enter password");
                    edtNewPasswordConfirm.requestFocus();
                    return;
                }

                if (!hashedNewPassword.equals(hashedNewPasswordConfirm)) {
                    edtNewPasswordConfirm.setError("Passwords do not match");
                    edtNewPasswordConfirm.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwdCo))
                {
                    Toast.makeText(EditPassword.this, "Please insert all details...", Toast.LENGTH_SHORT).show();
                }

                else {
                    //execute code
                    try (Connection conUpdate = connectionClass.CONN()){
                        if (conUpdate == null){
                            x = "Please check your internet connection";
                        }else{

                            String updateQuery = "UPDATE user SET password = ? WHERE userID = ?";
                            PreparedStatement preparedStatement = conUpdate.prepareStatement(updateQuery);
                            preparedStatement.setString(1, hashedNewPassword);
                            preparedStatement.setString(2, UserID);
                            preparedStatement.executeUpdate();
                            Toast.makeText(getBaseContext(), "Password updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditPassword.this, UserProfile.class);
                            intent.putExtra("userID", UserID);
                            startActivity(intent);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            }
        });



    }
    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to user profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditPassword.this, UserProfile.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }
}