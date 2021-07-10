package com.hansraj.phonelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityPhoneNumber extends AppCompatActivity {
    EditText pnumber;
    Button continuebutton;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        pnumber=findViewById(R.id.phoneBox);
        continuebutton=findViewById(R.id.continueBtn);
        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber="+91"+pnumber.getText().toString().trim();
                if(phoneNumber.length()<13){
                    pnumber.setError("enter a valid number");
                }
                else{
                    Intent intent=new Intent(ActivityPhoneNumber.this,OtpActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            }
        });


    }
}