package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DeleteProfileAdminFragment extends Fragment {

    ImageView Hazard;
    EditText username, password, rePassword;
    Button yes, no, proceed;
    View delProfView;
    ConnectionClass connectionClass;

    AlertDialog dialog;

    public DeleteProfileAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        delProfView = inflater.inflate(R.layout.fragment_delete_profile_admin, container, false);

        Hazard = delProfView.findViewById(R.id.warningImg);
        username = delProfView.findViewById(R.id.Id);
        password = delProfView.findViewById(R.id.Pwd);
        rePassword = delProfView.findViewById(R.id.rePwd);
        yes = delProfView.findViewById(R.id.btnDelete);
        no = delProfView.findViewById(R.id.btnCancel);
        proceed = delProfView.findViewById(R.id.btnProceed);

        connectionClass = new ConnectionClass();

        String id = getArguments().getString("ID");
        username.setText(id);
        username.setEnabled(false);
        password.setEnabled(false);
        rePassword.setEnabled(false);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setEnabled(true);
                rePassword.setEnabled(true);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileAdmin.class);
                intent.putExtra("shopID", id);
                startActivity(intent);
            }
        });

        username.addTextChangedListener(RegTextWatcher);
        password.addTextChangedListener(RegTextWatcher);
        rePassword.addTextChangedListener(RegTextWatcher);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                SpannableString title = new SpannableString("Alert Message!!!");
                title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                SpannableString message = new SpannableString("This action will permanently delete your account!");
                message.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, message.length(), 0);
                builder.setTitle(title);
                builder.setMessage(message);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteProf delProf = new doDeleteProf();
                        delProf.execute("");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), ProfileAdmin.class);
                        intent.putExtra("shopID", id);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();
                Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setTextColor(ContextCompat.getColor(requireContext(), R.color.greenYes));
                Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setTextColor(ContextCompat.getColor(requireContext(), R.color.greenNo));
            }
        });

        // Inflate the layout for this fragment
        return delProfView;
    }

    private final TextWatcher RegTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String IDInput = username.getText().toString().trim();
            String PwdInput = password.toString().trim();
            String rePwdInput = rePassword.getText().toString().trim();

            proceed.setEnabled(!IDInput.isEmpty() && !PwdInput.isEmpty() && !rePwdInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @SuppressLint("StaticFieldLeak")
    public class doDeleteProf extends AsyncTask<String, String,String> {

        final String BsnIDStr = username.getText().toString();
        final String BsnPasswordStr = password.getText().toString();
        final String ReBsnPasswordStr = rePassword.getText().toString();
        String hashedPassword = MD5.hashPassword(BsnPasswordStr);
        String hashedPasswordRpt = MD5.hashPassword(ReBsnPasswordStr);
        String y = "";
        boolean isSuccess = false;

        String ID, Pwd, rePwd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null){
                    y = "Please check your internet connection";
                }
                else{
                    String stsAftDel = "Inactive";
                    String query = "select * from admin where bsnID = '"+BsnIDStr+"' and bsnPass = '"
                            +hashedPassword+"' and bsnRePass = '"+hashedPasswordRpt+"'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next())
                    {
                        ID = rs.getString(1);
                        Pwd = rs.getString(5);
                        rePwd = rs.getString(6);

                        if(ID.equals(BsnIDStr)&& Pwd.equals(hashedPassword)&& rePwd.equals(hashedPasswordRpt)){

                            String queryNext = "UPDATE admin set statusAdmin = '"+stsAftDel+"' WHERE admin.bsnID = '"+BsnIDStr+"' AND admin.bsnPass = '"
                                    +hashedPassword+"' AND admin.bsnRePass = '"+hashedPasswordRpt+"'";

                            Statement statement = con.createStatement();
                            statement.executeUpdate(queryNext);

                            y = "Delete Successfully, Your account now is inactive";
                            isSuccess = true;
                            return y;
                        }
                        else if(Pwd.equals(hashedPassword) != rePwd.equals(hashedPasswordRpt))
                        {
                            y = "Both password doesn't same!!";
                            isSuccess = false;
                            return y;
                        }
                    }
                    else{
                        y = "Wrong Password!";
                    }
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return y;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            // Close the dialog if the task is cancelled
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected void onPostExecute(String s){

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if(isSuccess){
                Toast.makeText(getActivity(), ""+y, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(getActivity(), ""+y, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileAdmin.class);
                intent.putExtra("shopID", BsnIDStr);
                startActivity(intent);
            }
        }
    }
}