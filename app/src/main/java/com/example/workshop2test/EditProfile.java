package com.example.workshop2test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import com.example.workshop2test.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class EditProfile extends AppCompatActivity {


    private DatePickerDialog datePicker;
    ConnectionClass connectionClass;
    TextView txtID, txtFullName;
    ImageView profpic,edit;
    EditText userEmail, userContact, userBod;
    Button backbutton, save;
    RadioGroup radioGroup;
    PreparedStatement ps = null;
    String x = "";
    String strGender="";
    private static final int PICK_IMAGE_REQUEST = 99;
    private String image;
    private String longtextImage;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentViewAndInitialize();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_edit_profile_land);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_edit_profile);
        }
    }

    private void setContentViewAndInitialize() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_edit_profile_land);
        } else {
            setContentView(R.layout.activity_edit_profile);
        }
        // Initialize views and data here
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewAndInitialize();

        String UserID = getIntent().getStringExtra("userID");
        connectionClass = new ConnectionClass();

        profpic=findViewById(R.id.imageView2);
        edit=findViewById(R.id.edtPic);
        radioGroup=findViewById(R.id.radio_group);
        txtID = findViewById(R.id.txtID);
        txtFullName = findViewById(R.id.txtFullName);
        userEmail = findViewById(R.id.edtEmail);
        userContact = findViewById(R.id.edtNum);
        save = findViewById(R.id.btnSave);
        userBod=findViewById(R.id.edtBod);
        backbutton = findViewById(R.id.backbutton);

        //List Full Name and Matric/Staff number on Edit Profile page
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
                    String longtextImage = rs.getString(6);
                    byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    Bitmap imageToResize = Bitmap.createScaledBitmap(decodedImage, 200, 200, true);
                    profpic.setImageBitmap(imageToResize);

                    String userID = rs.getString("userID");
                    txtID.setText(userID);

                    String userName = rs.getString("userName");
                    txtFullName.setText(userName);

                    String user_phone = rs.getString("userPhone");
                    userContact.setText(user_phone);

                    String user_mail = rs.getString("userMail");
                    userEmail.setText(user_mail);

                    String user_birthday = rs.getString("birthday");
                    userBod.setText(user_birthday);

                    String user_gender = rs.getString("gender");
                    if (user_gender.equals("Male")) {
                        radioGroup.check(R.id.male);
                        strGender = "Male";
                    } else if (user_gender.equals("Female")) {
                        radioGroup.check(R.id.female);
                        strGender = "Female";
                    }

                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        userBod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                datePicker = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        //userBod.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        userBod.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        final RadioButton maleRadioButton = findViewById(R.id.male);
        final RadioButton femaleRadioButton = findViewById(R.id.female);
        RadioGroup radioGroup = findViewById(R.id.radio_group);

        //set listener for radio group
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male){
                    strGender = maleRadioButton.getText().toString();

                } else if(checkedId == R.id.female){
                    strGender = femaleRadioButton.getText().toString();
                }
            }
        });



        //Back to Profile Page
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Edit profile cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfile.this, UserProfile.class);
                intent.putExtra("userID", UserID);
                startActivity(intent);
            }
        });

        //Update and save Phone number or email
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userMailstr = userEmail.getText().toString();
                final String userContstr = userContact.getText().toString();
                final String userBirthstr = userBod.getText().toString();
                final String userGenderstr = strGender;
                //int selectedId = radioGroup.getCheckedRadioButtonId();
                //RadioButton radioButton = findViewById(selectedId);
                //String userGenderstr = radioButton.getText().toString();

                final String userPic;
                Bitmap image = ((BitmapDrawable) profpic.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                userPic = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String z = "";
                boolean isSuccess = false;

                try {
                    Connection connect = connectionClass.CONN();
                    if (connect == null) {
                        x = "Please check your internet connection";
                    } else {


                        if (userMailstr.trim().equals("")  && userContstr.trim().equals("")  && userBirthstr.trim().equals("")  ) {

                            Toast.makeText(getBaseContext(), "Email, Phone Number and Birthdate cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (userMailstr.trim().equals("")  &&  userBirthstr.trim().equals("")  ) {

                            Toast.makeText(getBaseContext(), "Email and Birthdate cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (userContstr.trim().equals("")  && userBirthstr.trim().equals("")  ) {

                            Toast.makeText(getBaseContext(), "Phone Number and Birthdate cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (userMailstr.trim().equals("")  && userContstr.trim().equals("")   ) {

                            Toast.makeText(getBaseContext(), "Email and Phone Number cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                        else if(userMailstr.trim().equals("")){
                            Toast.makeText(getBaseContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if(userContstr.trim().equals("")){
                            Toast.makeText(getBaseContext(), "Contact Number cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if(userBirthstr.trim().equals("")){
                            Toast.makeText(getBaseContext(), "Date of Birth cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if(userGenderstr.trim().equals("")){
                            Toast.makeText(getBaseContext(), "Gender cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                        else if (!userPic.trim().equals("")  || !userMailstr.trim().equals("")  || !userContstr.trim().equals("")  || !userBirthstr.trim().equals("")  || !userGenderstr.trim().equals("") ) {
                            //update all
                            String query = "update user set userImage = ? , userMail = ? , userPhone = ? , birthday = ? , gender = ? where userID = ? ";
                            ps = connect.prepareStatement(query);
                            if (!userPic.trim().equals("") ) {
                                ps.setString(1, userPic);
                            } else {
                                ps.setString(1, "");
                            }
                            if (!userMailstr.trim().equals("") ) {
                                ps.setString(2, userMailstr);
                            } else {
                                ps.setString(2, "");
                            }
                            if (!userContstr.trim().equals("") ) {
                                ps.setString(3, userContstr);
                            } else {
                                ps.setString(3, "");
                            }
                            if (!userBirthstr.trim().equals("") ) {
                                ps.setString(4, userBirthstr);
                            } else {
                                ps.setString(4, "");
                            }
                            if (!userGenderstr.trim().equals("") ) {
                                ps.setString(5, userGenderstr);
                            } else {
                                ps.setString(5, "");
                            }
                            ps.setString(6, UserID);
                            ps.executeUpdate();

                            Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfile.this, UserProfile.class);
                            intent.putExtra("userID", UserID);
                            startActivity(intent);
                        }

                    }

                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void chooseImage() {

        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                    data.getData() != null){
                Uri imagePath = data.getData();
                Bitmap imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profpic.setImageBitmap(imageToStore);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageInBytes = byteArrayOutputStream.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    image = java.util.Base64.getEncoder().encodeToString(imageInBytes);
                }
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void onBackPressed() {
        String UserID = getIntent().getStringExtra("userID");

        Toast.makeText(getBaseContext(), "Back to user profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditProfile.this, UserProfile.class);
        intent.putExtra("userID", UserID); //session userID
        startActivity(intent);

    }

}