package com.example.digiloans;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class applyloan extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://digiloans-e4218-default-rtdb.firebaseio.com/");

    EditText amount,mpesaNumber;
    Button applyBtn;
    String periodDays;
    String loanId;
    Integer periodInterest;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyloan);
        amount = findViewById(R.id.inputAmount);
        mpesaNumber = findViewById(R.id.inputMpesaNumber);
        applyBtn = findViewById(R.id.applyLoanBtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });

    }

    private void performAuth() {
        String theAmount = amount.getText().toString();
        String theMpesaNumber = mpesaNumber.getText().toString();

        if(theAmount.isEmpty()){
            amount.setError("Amount should not be empty");
        }else if(theMpesaNumber.isEmpty()){
            mpesaNumber.setError("Mpesa number should not be empty");
        }else if(!theAmount.matches("^[0-9]+$")){
            amount.setError("Amount should be digits only");
        } else if (!theMpesaNumber.matches("^[0-9]+$")) {
            mpesaNumber.setError("Mpesa number should have digits only");
        }else{

            progressDialog.setMessage("Please wait while loan application completes...");
            progressDialog.setTitle("Loan application");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //Store information to db
            String theUserId = mUser.getUid();
            
        }

    }

    private void givenUsingPlainJava_whenGeneratingRandomStringBounded_thenCorrect() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.for14days:
                if (checked)
                    // Pirates are
                    periodDays = "14";
                periodInterest = 10;
                Toast.makeText(applyloan.this, "You choose 14 days", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.for30days:
                if (checked)
                    // Ninjas rule
                    periodDays = "30";
                periodInterest = 20;
                Toast.makeText(applyloan.this, "You choose 30 days", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.for60days:
                if (checked)
                    // Ninjas rule
                    periodDays = "60";
                periodInterest = 35;
                Toast.makeText(applyloan.this, "You choose 60 days", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}