package com.example.workshop2test;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.security.SecureRandom;
import java.security.MessageDigest;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class MainActivityUser extends AppCompatActivity {

    private DatePickerDialog datePicker;
    private EditText userID, userName, userEmail, userContact, userPass, userRePass, userBirthDate;
    private Button regUser;
    RadioGroup radioGroup;
    private ImageView userPic;
    private ProgressDialog progressDialog;
    private ConnectionClass connectionClass;
    private TextView logUser;

    String strGender="";
    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagePath;
    private Bitmap imageToStore;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    private String imageString;



    private void initViews() {
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userContact = findViewById(R.id.userNumber);
        userPass = findViewById(R.id.userPass);
        userPass.setTransformationMethod(new PasswordTransformationMethod());
        userRePass = findViewById(R.id.userRepass);
        userRePass.setTransformationMethod(new PasswordTransformationMethod());
        userBirthDate = findViewById(R.id.BirthDateReg);
        regUser = findViewById(R.id.registerUser);
        radioGroup = findViewById(R.id.radio_group);
        userPic = findViewById(R.id.Shop_image);
        logUser = findViewById(R.id.logNowBtnn);
        connectionClass = new ConnectionClass();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViews();
        initListeners();
        userBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                datePicker = new DatePickerDialog(MainActivityUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userBirthDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        final RadioButton maleRadioButton = findViewById(R.id.male);
        final RadioButton femaleRadioButton = findViewById(R.id.female);
        RadioGroup radioGroup = findViewById(R.id.radio_group);

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

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private boolean checkUserExist(String id) {
        boolean result = false;
        Connection connection = connectionClass.CONN();
        String query = "SELECT COUNT(*) as count FROM user WHERE userID= '" + id + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private class DoregUser extends AsyncTask<String, String, String> {
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivityUser.this, "Loading", "Please Wait...", true);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String userid = userID.getText().toString();
            String name = userName.getText().toString();
            String email = userEmail.getText().toString();
            String contact = userContact.getText().toString();
            String pass = userPass.getText().toString();
            String repass = userRePass.getText().toString();
            String image = params[0];
            String birthdateReg = userBirthDate.getText().toString();

            if (userid.trim().equals("") || name.trim().equals("") || email.trim().equals("") || contact.trim().equals("") || pass.trim().equals("") || repass.trim().equals("") || image.trim().equals("")|| birthdateReg.trim().equals("")|| strGender.trim().equals(""))
                z = "Please enter all fields...";
            else {
                if(pass.equals(repass)) {
                    boolean isExist = checkUserExist(userid);
                    if(!isExist) {
                        try {
                            Connection con = connectionClass.CONN();
                            if (con == null) {
                                z = "Error in connection with SQL server";
                            } else {
                                String hashedPassword = MD5.hashPassword(pass);
                                String query = "insert into user (userID, userName, userMail, userPhone, password,gender,birthday,userImage,statusUser)  VALUES ('"+userid+"', '"+name+"', '"+email+"', '"+contact+"', '"+hashedPassword+"', '"+strGender+"', '"+birthdateReg+"','"+imageString+"','"+"Active"+"')";
                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);
                                z = "Register successfully";
                                isSuccess = true;
                            }
                        } catch (Exception ex) {
                            z = "Exceptions" + ex;
                        }
                    } else {
                        z = "User ID already exist";
                    }
                } else {
                    z = "Password does not match";
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();
            if(isSuccess) {
                startActivity(new Intent(MainActivityUser.this, MainActivityUserLogin.class));
                finish();
            }
            progressDialog.hide();
        }
    }

    private void initListeners() {
        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReg();
            }
        });

        logUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityUser.this, MainActivityUserLogin.class));
            }
        });

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }


    private void doReg(){
        final String id = userID.getText().toString();
        final String name = userName.getText().toString();
        final String email = userEmail.getText().toString();
        final String contact = userContact.getText().toString();
        final String pass = userPass.getText().toString();
        final String rePass = userRePass.getText().toString();
        final String birthdate = userBirthDate.getText().toString();

        if (TextUtils.isEmpty(id)) {
            userID.setError("Please enter ID");
            userID.requestFocus();
            return;
        }

        if (userID.getText().toString().trim().contains(" ")) {
            Toast.makeText(getBaseContext(), "UserID field cannot contain spaces.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            userName.setError("Please enter name");
            userName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            userEmail.setError("Please enter email");
            userEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Enter a valid email");
            userEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            userContact.setError("Please enter contact number");
            userContact.requestFocus();
            return;
        }

        if (TextUtils.isDigitsOnly(contact)) {
            // contact number is only digits
        } else {
            userContact.setError("Please enter a valid contact number, only numbers are allowed");
            userContact.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            userPass.setError("Please enter password");
            userPass.requestFocus();
            return;
        }
        if (pass.length() < 8) {
            userPass.setError("Password should be at least 8 characters long");
            userPass.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(rePass)) {
            userRePass.setError("Please re-enter password");
            userRePass.requestFocus();
            return;
        }
        if (!pass.equals(rePass)) {
            userRePass.setError("Passwords do not match");
            userRePass.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(birthdate)) {
            userBirthDate.setError("Please enter birthdate");
            userBirthDate.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(strGender)) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }
        //check if user ID already exist
        boolean isExist = checkUserExist(id);
        if(isExist) {
            userID.setError("User ID already exist, please enter another ID");
            userID.requestFocus();
            return;
        }
        //check if image is selected
        if (imageToStore == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

//store image in string format
        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(imageInBytes, Base64.DEFAULT);

//register user
        DoregUser doregUser = new DoregUser();
        doregUser.execute(imageString);

    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                userPic.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {

        Toast.makeText(getBaseContext(), "Back to selection page" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityUser.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}