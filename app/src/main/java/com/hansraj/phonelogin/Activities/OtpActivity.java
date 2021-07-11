package com.hansraj.phonelogin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hansraj.phonelogin.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    String verificationId;

    ProgressDialog dialog;
    String phoneNumber;
    TextView verifyPhoneLbl;
    OtpView otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Sending OTP...");
        dialog.setCancelable(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        verifyPhoneLbl=findViewById(R.id.phoneLbl);
        getSupportActionBar().hide();

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        verifyPhoneLbl.setText("Verify " + phoneNumber);

        otpView=findViewById(R.id.otp_view);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull  PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull  FirebaseException e) {

                            }
                            @Override
                            public void onCodeSent(@NonNull String otpfirebase, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(otpfirebase, forceResendingToken);
                                dialog.dismiss();
                                verificationId = otpfirebase;

                                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                otpView.requestFocus();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                dialog.setMessage("Verifying otp...");
                dialog.setCancelable(false);
                dialog.show();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            dialog.dismiss();
                            Intent intent = new Intent(OtpActivity.this, Set_Up_Profile_Activity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(OtpActivity.this, "ENTER CORRECT OTP.", Toast.LENGTH_SHORT).show();
                            otpView.setText("");
                        }

                    }
                });
            }
        });
    }

}