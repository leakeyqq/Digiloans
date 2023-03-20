package com.example.digiloans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://digiloans-e4218-default-rtdb.firebaseio.com/");


    TextView alreadyHaveaccount;
    EditText inputEmail, inputPassword, inputConfirmPassword, inputPhoneNumber, inputIdNumber;
    Button btnRegister;
    String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveaccount = findViewById(R.id.wantToLogin);
        inputPhoneNumber = findViewById(R.id.inputTelephone);
        inputIdNumber = findViewById(R.id.inputIdnumber);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        alreadyHaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });

    }

    private void performAuth() {
        String telephone = inputPhoneNumber.getText().toString();
        String idnumber = inputIdNumber.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();



        if(telephone.length() != 10 || !telephone.matches("^[0-9]+$")){
            inputPhoneNumber.setError("Phone number should be 10 digits!");
        }else if(idnumber.length() != 8 || !idnumber.matches("^[0-9]+$")){
            inputIdNumber.setError("National ID number should be 8 digits!");
        }else if(!email.matches(emailPattern)){
            inputEmail.setError("Enter a correct email format!");
        }else if(!password.equals(confirmPassword)){
            inputConfirmPassword.setError("Passwords do not match");
        }else if(password.isEmpty() ||password.length()<6){
            inputPassword.setError("Password cannot be less than 6 characters");
        }else{
            progressDialog.setMessage("Please wait while registration completes...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String theUserId = mUser.getUid();
                        //Store customer information in our database
                        databaseReference.child("users").child(theUserId).child("email").setValue(email);
                        databaseReference.child("users").child(theUserId).child("telephone").setValue(telephone);
                        databaseReference.child("users").child(theUserId).child("idNumber").setValue(idnumber);
                        databaseReference.child("users").child(theUserId).child("crbStatus").setValue("good");



                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}