package com.example.workshop2test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class EditProfileAdminFragment extends Fragment {

    EditText ID, Name, Mail, Phone, Password, RePassword;
    ImageView profPic;
    Button BtnSave, BtnClc;
    View edtProfView;
    ConnectionClass connectionClass;
    String x = "";

    private static final int PICK_IMAGE_REQUEST = 99;
    private String image;

    public EditProfileAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        edtProfView = inflater.inflate(R.layout.fragment_edit_profile_admin, container, false);

        ID = edtProfView.findViewById(R.id.ID);
        Name = edtProfView.findViewById(R.id.name);
        Mail = edtProfView.findViewById(R.id.mail);
        Phone = edtProfView.findViewById(R.id.phone);
        Password = edtProfView.findViewById(R.id.pwd);
        RePassword = edtProfView.findViewById(R.id.RePwd);
        profPic = edtProfView.findViewById(R.id.profilePicture);
        BtnSave = edtProfView.findViewById(R.id.saveBtn);
        BtnClc = edtProfView.findViewById(R.id.cancelBtn);

        connectionClass = new ConnectionClass();

        String id = getArguments().getString("ID");
        ID.setText(id);
        ID.setEnabled(false);

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                x = "Please check your internet connection";
            }
            else {
                String query = "select * from admin where bsnID = '" + id + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    String image = rs.getString(7);
                    String bsnName = rs.getString(2);
                    String bsnEmail =rs.getString(3);
                    String bsnPhone =rs.getString(4);
                    String bsnPassword = rs.getString(5);
                    String bsnRePassword = rs.getString(6);

                    byte[] bytes = android.util.Base64.decode(image, android.util.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    profPic.setImageBitmap(bitmap);
                    Name.setText(bsnName);
                    Mail.setText(bsnEmail);
                    Phone.setText(bsnPhone);
                    Password.setText(bsnPassword);
                    Password.setTransformationMethod(new PasswordTransformationMethod());
                    RePassword.setText(bsnRePassword);
                    RePassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdateProf updateProf = new doUpdateProf();
                updateProf.execute("");
            }
        });

        BtnClc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileAdmin.class);
                intent.putExtra("shopID", id);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return edtProfView;
    }

    private void chooseImage(){
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                    data.getData() != null){
                Uri imagePath = data.getData();
                Bitmap imageToStore = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagePath);
                profPic.setImageBitmap(imageToStore);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageInBytes = byteArrayOutputStream.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    image = Base64.getEncoder().encodeToString(imageInBytes);
                }
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class doUpdateProf extends AsyncTask<String, String,String>{

        final String BsnIDStr = ID.getText().toString();
        final String BsnNameStr = Name.getText().toString();
        final String BsnMailStr = Mail.getText().toString();
        final String BsnPhoneStr = Phone.getText().toString();
        final String BsnPassStr = Password.getText().toString();
        final String ReBsnPassStr = RePassword.getText().toString();
        final String imageToStr = image;
        String y = "";
        boolean isSuccess = false;

        @Override
        protected String doInBackground(String... params){

            try {
                Connection con = connectionClass.CONN();
                if (con == null){
                    y = "Please check your internet connection";
                }
                else{
                    String hashedPassword = MD5.hashPassword(BsnPassStr);
                    String hashedPasswordRpt = MD5.hashPassword(ReBsnPassStr);
                    String query = "update admin set bsnName = '"+BsnNameStr+"', bsnMail = '"+BsnMailStr+"', bsnPhone = '"
                            +BsnPhoneStr+"', bsnPass = '"+hashedPassword+"', bsnRePass = '"+hashedPasswordRpt+"', logoShop = '"+imageToStr+"' where bsnID = '"+BsnIDStr+"'";

                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    y = "Update Successfully";
                    isSuccess = true;
                }
            } catch (SQLException e)
            {
                isSuccess = false;
                y = "Exceptions" + e;
            }
            return y;
        }

        @Override
        protected void onPostExecute(String s){

            Toast.makeText(getActivity(), ""+y, Toast.LENGTH_SHORT).show();

        }
    }
}