package com.example.workshop2test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserProfile extends AppCompatActivity {

    ConnectionClass connectionClass;
    Button backbutton, editProf, deleteProf, changePassword;
    TextView txtID, txtFullName, txtEmail, txtNum, txtBod, txtGender;
    ImageView logout,profpic;
    PreparedStatement ps = null;
    String x = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        connectionClass = new ConnectionClass();
        String UserID =getIntent().getStringExtra("userID");
        //String ID = UserID;
        changePassword = findViewById(R.id.changePasswordBtn);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "SETTING | Password" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfile.this, EditPassword.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
            }
        });


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to Customer Main Page" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfile.this, MainPageCustomer.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
            }
        });

        editProf=findViewById(R.id.edt);
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Edit Profile" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfile.this, EditProfile.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
            }
        });

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UserProfile.this);
                builder.setTitle("ALERT!");
                builder.setMessage("Do you want to LogOut?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                        Toast.makeText(getBaseContext(), "Successfully Logout" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfile.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();

            }
        });

        profpic=findViewById(R.id.imageView2);
        txtID = findViewById(R.id.txtID);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmail =  findViewById(R.id.txtEmail);
        txtNum =  findViewById(R.id.txtNum);
        txtBod = findViewById(R.id.txtBod);
        txtGender = findViewById(R.id.txtGender);

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            } else {
                String query = "select * from user where userID='" + UserID + "'";


                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    String image = rs.getString(6);
                    byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    Bitmap imageToResize = Bitmap.createScaledBitmap(decodedImage, 200, 200, true);
                    profpic.setImageBitmap(imageToResize);

                    String userID = rs.getString("userID");
                    txtID.setText(userID);

                    String userName = rs.getString("userName");
                    txtFullName.setText(userName);

                    String userContact = rs.getString("userPhone");
                    txtNum.setText(userContact);

                    String userEmail = rs.getString("userMail");
                    txtEmail.setText(userEmail);

                    String birthday = rs.getString("birthday");
                    txtBod.setText(birthday);

                    String gender = rs.getString("gender");
                    txtGender.setText(gender);
                    //It should display on name textview as the username from database
                    //And it's worked.
                }


            }
        } catch (SQLException e){
            e.printStackTrace();
        }


        deleteProf = findViewById(R.id.deleteAcc);
        deleteProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(UserProfile.this);
                builder.setTitle("WARNING!");
                builder.setMessage("Do you want to DEACTIVATED your account?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            Connection connect = connectionClass.CONN();
                            if (connect == null) {
                                x = "Please check your internet connection";
                            } else {
                                //update user set password = ? where userID = ?
                                //String query = "delete from user where userID = ? ";
                                String query = "update user set statusUser = ? where userID = ? ";
                                ps = connect.prepareStatement(query);
                                ps.setString(1, "Deactivated");
                                ps.setString(2, UserID);
                                ps.executeUpdate();

                                Toast.makeText(getBaseContext(), "Account DEACTIVATED" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                                startActivity(intent);

                            }
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();

            }

        });


    }


    @Override
    public void onBackPressed() {
        String UserID =getIntent().getStringExtra("userID");
        Toast.makeText(getBaseContext(), "Back to Customer Main Page" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserProfile.this, MainPageCustomer.class);
        intent.putExtra("userID", UserID);
        startActivity(intent);
    }
}

