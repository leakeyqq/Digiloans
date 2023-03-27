package com.example.digiloans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class applyloan extends AppCompatActivity {

    EditText amount,mpesaNumber;
    Button applyBtn;
    String periodDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyloan);
        amount = findViewById(R.id.inputAmount);
        mpesaNumber = findViewById(R.id.inputMpesaNumber);
        applyBtn = findViewById(R.id.applyLoanBtn);

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.for14days:
                if (checked)
                    // Pirates are the best
                    periodDays = "14";
                Toast.makeText(applyloan.this, "You choose 14 days", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.for30days:
                if (checked)
                    // Ninjas rule
                    periodDays = "30";
                Toast.makeText(applyloan.this, "You choose 30 days", Toast.LENGTH_SHORT).show();
                    break;
        }
    }
}