package com.example.digiloans;

import static java.lang.Math.round;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

            //Generate a random string for unique loan ID
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

            loanId = generatedString;
            //Store information to db
            String theUserId = mUser.getUid();
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


            int numberOfDays = Integer.parseInt(periodDays);
            String dateBefore = currentDate;

            // create instance of the SimpleDateFormat that matches the given date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            //create instance of the Calendar class and set the date to the given date
            Calendar cal = Calendar.getInstance();
            try{
                cal.setTime(sdf.parse(dateBefore));
            }catch(ParseException e){
                e.printStackTrace();
            }
            // use add() method to add the days to the given date
            cal.add(Calendar.DAY_OF_MONTH, numberOfDays);
            String dateAfter = sdf.format(cal.getTime());

            String dueDate = dateAfter;


            //Calculating the interest earned
            Float periodDays_as_Float = Float.parseFloat(periodDays);
            Float periodInterest_as_Float = (float) periodInterest;
            Float amount_as_Float = Float.parseFloat(theAmount);
            Integer interest = round(amount_as_Float * (periodInterest_as_Float/100));

            String final_interest = Integer.toString(interest);
            int loanAmount_as_Int = Integer.parseInt(theAmount);
            int loan_plus_interest = loanAmount_as_Int + interest;

            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("amountPaidBack").setValue(0);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("amountTaken").setValue(theAmount);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("balance").setValue(loan_plus_interest);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("dueDate").setValue(dueDate);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("fines").setValue(0);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("initialDate").setValue(currentDate);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("interest").setValue(interest);
            databaseReference.child("users").child(theUserId).child("activeLoans").child(loanId).child("withdrawnBy").setValue(theMpesaNumber);

            progressDialog.dismiss();
            sendUserToNextActivity();
            Toast.makeText(applyloan.this, "Loan successful!", Toast.LENGTH_SHORT).show();

        }

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(applyloan.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            /*
            case R.id.for14days:
                if (checked)
                    periodDays = "14";
                periodInterest = 10;
                Toast.makeText(applyloan.this, "You choose 14 days", Toast.LENGTH_SHORT).show();
                    break;

             */
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
            default:
                periodDays = "14";
                periodInterest = 10;
                Toast.makeText(applyloan.this, "You choose 14 days", Toast.LENGTH_SHORT).show();
        }
    }
}