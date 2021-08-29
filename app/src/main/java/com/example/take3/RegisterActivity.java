/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    //user interface declared
    private EditText email;
    private EditText pass;
    private EditText passconfirm;

    //Firebase authentication declared
    private FirebaseAuth mAuth;

    //progress dialog declared
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set Firebase authentication
        mAuth=FirebaseAuth.getInstance();

        //set Progress Dialog
        mDialog=new ProgressDialog(this);

        //set variables to ids declared in in xml file
        email=findViewById(R.id.email_reg);
        pass=findViewById(R.id.password_reg);
        passconfirm=findViewById(R.id.confirmpassword_reg);

        //local variables
        Button btnReg = findViewById(R.id.register_btn);
        TextView signin = findViewById(R.id.signin_txt);

        //listener for when register button is pressed
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //declare register sections
                final String mEmail=email.getText().toString().trim();
                String mPass=pass.getText().toString().trim();
                String mPassConfirm=passconfirm.getText().toString().trim();

                //VALIDATION
                //IF email address field is empty THEN
                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("*Email required");
                    //displays error message saying email is required to continue
                    return;
                }
                //IF email address supplied does not match the format of an email address THEN
                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    //display error message
                    Toast.makeText(getApplicationContext(), "Invalid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //IF password field is empty THEN
                if (TextUtils.isEmpty(mPass)) {
                    pass.setError("*Password required");
                    //displays error message saying password is required to continue
                    return;
                }
                //IF length of password is less than 6 characters THEN
                if (mPass.length() < 6) {
                    //display error message
                    Toast.makeText(getApplicationContext(), "Please enter a password at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //IF confirm password field is empty THEN
                if (TextUtils.isEmpty(mPassConfirm)) {
                    //display error message saying confirmed password is required to continue
                    passconfirm.setError("*Confirmed password required");
                    return;
                }
                //IF confirmed password is not equal to password THEN
                if (!mPassConfirm.equals(mPass)) {
                    //display error message
                    Toast.makeText(getApplicationContext(), "Confirmed password does not match initial password.", Toast.LENGTH_SHORT).show();
                    //hault registration progress
                    return;
                }

                //SET PROGRESS MESSAGE AND DISPLAY
                mDialog.setMessage("Processing Registration Details...");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //IF the registration is successful THEN
                        if (task.isSuccessful()) {
                            //take user to home page
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            //display success message to tell user registration has been made
                            Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "WELCOME " + mEmail, Toast.LENGTH_SHORT).show();
                            //progress dialog dismissed
                            mDialog.dismiss();
                        } else {
                            //display error message to tell user registration has failed and that user must already be registered
                            Toast.makeText(getApplicationContext(), "Registration Failed! Email already in use.", Toast.LENGTH_SHORT).show();
                            //progress dialog dismissed
                            mDialog.dismiss();
                        }
                    }
                });

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when user clicks sign in text view, the user is taken to the login page
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
