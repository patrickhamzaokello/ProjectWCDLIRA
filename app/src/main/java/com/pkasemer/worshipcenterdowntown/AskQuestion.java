package com.pkasemer.worshipcenterdowntown;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pkasemer.worshipcenterdowntown.HelperClasses.SharedPrefManager;
import com.pkasemer.worshipcenterdowntown.HttpRequests.RequestHandler;
import com.pkasemer.worshipcenterdowntown.HttpRequests.URLs;
import com.pkasemer.worshipcenterdowntown.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AskQuestion extends AppCompatActivity {

    ActionBar actionBar;
    Button loginbtn;
    TextInputEditText inputTextaskUsername, inputTextAskQuestion;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        actionBar = getSupportActionBar(); // or getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.purple_500)));
            actionBar.setElevation(0);
            actionBar.setTitle("Submit Question");
        }
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        //Hooks
        loginbtn = findViewById(R.id.login_btn);



        inputTextaskUsername = findViewById(R.id.inputTextaskUsername);
        inputTextAskQuestion = findViewById(R.id.inputTextAskQuestion);


        //if user presses on login
        //calling the method login
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });




    }

    private void userLogin() {
        //first getting the values
        username = inputTextaskUsername.getText().toString();
        password = inputTextAskQuestion.getText().toString();




        //validating inputs
        if (TextUtils.isEmpty(username)) {
            username = "Guest";
        }

        if (TextUtils.isEmpty(password)) {
            inputTextAskQuestion.setError(" Question is Required");
            inputTextAskQuestion.requestFocus();
            return;
        }

        //if everything is fine


        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                if(s.isEmpty()){
                    //show network error
                    showErrorAlert();
                    return;
                }

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        inputTextaskUsername.setText("");
                        inputTextAskQuestion.setText("");
                        showSuccess();
                        return;
                    } else {
                        //Invalid Username or Password
                        showInvalidUser();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("question", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_ASK, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    private void showErrorAlert() {
        new SweetAlertDialog(
                this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong!")
                .show();
    }

    private void showInvalidUser() {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("Try again")
                .show();
    }

    private void showSuccess() {

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sent Successfully")
                .setContentText("Your Question has been sent")
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}