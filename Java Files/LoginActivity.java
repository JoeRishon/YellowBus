package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewsignup;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ToggleButton aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonSignin = findViewById(R.id.buttonSignin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        textViewsignup = findViewById(R.id.textViewsignin);
        progressDialog = new ProgressDialog(this);
        aSwitch = findViewById(R.id.switch3);

        if(firebaseAuth.getCurrentUser()!=null){
            if(aSwitch.isChecked())
            {
                finish();
                startActivity(new Intent(getApplicationContext(),ReadLi.class));
                Toast.makeText(LoginActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
            }
            else{
                finish();
                startActivity(new Intent(getApplicationContext(),location.class));
                Toast.makeText(LoginActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
            }}

        // buttonSignin.setOnClickListener(this);
        //textViewsignup.setOnClickListener(this);
    }

    /*private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       progressDialog.dismiss();

                       if(task.isSuccessful() ){
                            //add map thing
                       }
                    }
                });
    }*/

    @Override
    public void onClick(View v) {
        /*if(v == buttonSignin) {
            userLogin();
        }

        if(v == textViewsignup){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }*/
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("loggin User..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful() ){
                            //user is successfully registered and logged in
                            //we will start the maps activity
                            if(aSwitch.isChecked())
                            {
                                finish();
                                startActivity(new Intent(getApplicationContext(),ReadLi.class));
                                Toast.makeText(LoginActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                finish();
                                startActivity(new Intent(getApplicationContext(),location.class));
                                Toast.makeText(LoginActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            //startActivity(new Intent(getApplicationContext(),ReadLi.class));
                            Toast.makeText(LoginActivity.this,"couldnt login",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void userLogin(View view)
    {
        userLogin();
    }

    public void signup(View view) {
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}
