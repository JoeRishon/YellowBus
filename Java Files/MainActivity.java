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

import static com.example.myapplication.R.id.textViewsignin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextemail;
    private EditText editTextPass;
    private TextView textViewsignin;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ToggleButton aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextemail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        textViewsignin = findViewById(R.id.textViewsignin);
        aSwitch = findViewById(R.id.switch3);

        if(firebaseAuth.getCurrentUser()!=null){
            if(aSwitch.isChecked())
            {
                finish();
                startActivity(new Intent(getApplicationContext(),ReadLi.class));
                Toast.makeText(MainActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
            }
            else{
                finish();
                startActivity(new Intent(getApplicationContext(),location.class));
                Toast.makeText(MainActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
            }}


    }

    /*private void registerUser(){
        String email = editTextemail.getText().toString().trim();
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

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registered and logged in
                            //we will start the profile activity
                            Toast.makeText(MainActivity.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this,"couldnt register",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
*/
    @Override
    public void onClick(View v) {
    /*    if(v == buttonRegister){
            Toast.makeText(this,"hello",Toast.LENGTH_LONG).show();
            registerUser();
        }

        if (v == textViewsignin){
            Toast.makeText(this,"hello",Toast.LENGTH_LONG).show();
        }*/
    }

    public void registerUser(View view) {
        String email = editTextemail.getText().toString().trim();
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

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss() ;
                        if(task.isSuccessful()){
                            //user is successfully registered and logged in
                            //we will start the maps activity
                            if(aSwitch.isChecked())
                            {
                                finish();
                                startActivity(new Intent(getApplicationContext(),ReadLi.class));
                                Toast.makeText(MainActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                finish();
                                startActivity(new Intent(getApplicationContext(),location.class));
                                Toast.makeText(MainActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this,"couldnt register",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public void signin(View view) {
        Toast.makeText(MainActivity.this,"opening signin",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,LoginActivity.class));
    }
}