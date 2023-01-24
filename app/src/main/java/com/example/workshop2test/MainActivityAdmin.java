package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Objects;

public class MainActivityAdmin extends AppCompatActivity {

    EditText BsnID,BsnName,BsnMail,BsnContact,BsnPass,ReBsnPass;
    Button regAdmin;
    TextView logAdmin;
    ImageView shopLogo;
    ConnectionClass connectionClass;
    ProgressDialog progressDialogReg;

    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagePath;
    private Bitmap imageToStore;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    private String imageString;

    private void initViews() {
        BsnID = findViewById(R.id.BusinessID);
        BsnName = findViewById(R.id.BusinessName);
        BsnMail = findViewById(R.id.emailSeller);
        BsnContact = findViewById(R.id.BusinessNumber);
        BsnPass = findViewById(R.id.passSeller);
        ReBsnPass = findViewById(R.id.RepassSeller);
        regAdmin = findViewById(R.id.registerSeller);
        logAdmin = findViewById(R.id.logNowBtn);
        shopLogo = findViewById(R.id.Shop_image);

        connectionClass = new ConnectionClass();
        WeakReference<Context> contextWeakReference = new WeakReference<>(this);
        progressDialogReg = new ProgressDialog(contextWeakReference.get());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        (getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViews();
        initListeners();
        shopLogo.setOnClickListener(new View.OnClickListener() {
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
        String query = "SELECT COUNT(*) as count FROM user WHERE bsnID= '" + id + "'";
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                shopLogo.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class DoRegAdmin extends AsyncTask<String,String,String> {

        String y = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialogReg.setMessage("Loading...");
            progressDialogReg.show();
        }

        @Override
        protected String doInBackground(String... params){
            String BsnIDStr = BsnID.getText().toString();
            String BsnNameStr = BsnName.getText().toString();
            String BsnMailStr = BsnMail.getText().toString();
            String BsnPhoneStr = BsnContact.getText().toString();
            String BsnPassStr = BsnPass.getText().toString();
            String ReBsnPassStr = ReBsnPass.getText().toString();
            String image = params[0];
            String statusAdmin = "Unapproved";

            if (BsnIDStr.trim().equals("")|| BsnNameStr.trim().equals("") ||BsnMailStr.trim().equals("")
                    ||BsnPhoneStr.trim().equals("") ||BsnPassStr.trim().equals("") ||ReBsnPassStr.trim().equals("")
                    || image.trim().equals("")) {
                y = "Please insert all details and choose your profile logo business...";
            }
            else
            {
                if(BsnPassStr.equals(ReBsnPassStr)) {
                    boolean isExist = checkUserExist(BsnIDStr);
                    if(!isExist) {
                        try {
                            Connection con = connectionClass.CONN();
                            if (con == null) {
                                y = "Error in connection with SQL server";
                            } else {
                                String hashedPassword = MD5.hashPassword(BsnPassStr);
                                String query = "insert into admin values('"+BsnIDStr+"','"+BsnNameStr+"','"+BsnMailStr+"','"
                                        +BsnPhoneStr+"','"+hashedPassword+"','"+hashedPassword+"','"+imageString+"','"+statusAdmin+"')";
                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);
                                y = "Register successfully";
                                isSuccess = true;
                            }
                        } catch (Exception ex) {
                            y = "Exceptions" + ex;
                        }
                    } else {
                        y = "User ID already exist";
                    }
                }else {
                    y = "Password does not match";
                }
            }
            return y;
        }

        @Override
        protected void onPostExecute(String s){

            if (progressDialogReg != null && progressDialogReg.isShowing()) {
                progressDialogReg.dismiss();
            }

            Toast.makeText(getBaseContext(), "" + y, Toast.LENGTH_SHORT).show();
            progressDialogReg.hide();
        }
    }

    private void initListeners() {
        regAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReg();
            }
        });

        logAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityAdmin.this, MainActivityAdminLogin.class));
            }
        });

        shopLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void doReg(){
        final String BsnIDStr = BsnID.getText().toString();
        final String BsnNameStr = BsnName.getText().toString();
        final String BsnMailStr = BsnMail.getText().toString();
        final String BsnPhoneStr = BsnContact.getText().toString();
        final String BsnPassStr = BsnPass.getText().toString();
        final String ReBsnPassStr = ReBsnPass.getText().toString();

        if (TextUtils.isEmpty(BsnIDStr)) {
            BsnID.setError("Please enter ID");
            BsnID.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(BsnNameStr)) {
            BsnName.setError("Please enter name");
            BsnName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(BsnMailStr)) {
            BsnMail.setError("Please enter email");
            BsnMail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(BsnMailStr).matches()) {
            BsnMail.setError("Enter a valid email");
            BsnMail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(BsnPhoneStr)) {
            BsnContact.setError("Please enter contact number");
            BsnContact.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(BsnPassStr)) {
            BsnPass.setError("Please enter password");
            BsnPass.requestFocus();
            return;
        }
        if (BsnPassStr.length() < 8) {
            BsnPass.setError("Password should be at least 8 characters long");
            BsnPass.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(ReBsnPassStr)) {
            ReBsnPass.setError("Please re-enter password");
            ReBsnPass.requestFocus();
            return;
        }
        if (!BsnPassStr.equals(ReBsnPassStr)) {
            ReBsnPass.setError("Passwords do not match");
            ReBsnPass.requestFocus();
            return;
        }

        //check if user ID already exist
        boolean isExist = checkUserExist(BsnIDStr);
        if(isExist) {
            BsnID.setError("User ID already exist, please enter another ID");
            BsnID.requestFocus();
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
        imageString = android.util.Base64.encodeToString(imageInBytes, android.util.Base64.DEFAULT);

//register admin
        DoRegAdmin doRegAdmin = new DoRegAdmin();
        doRegAdmin.execute(imageString);

    }

    @Override
    public void onBackPressed() {

        Toast.makeText(getBaseContext(), "Back to selection page" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityAdmin.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}