package com.example.digiloans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    CardView borrow,repay,account,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        borrow = findViewById(R.id.borrow);
        repay = findViewById(R.id.repay);
        account = findViewById(R.id.account);
        history = findViewById(R.id.history);

        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, applyloan.class));
            }
        });
    }
}