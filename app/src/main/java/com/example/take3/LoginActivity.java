/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //user interface declared
    private EditText email;
    private EditText pass;

    //Firebase authentication declared
    private FirebaseAuth mAuth;

    //progress dialog declared
    private ProgressDialog mDialog;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set Firebase authentication
        mAuth=FirebaseAuth.getInstance();

        //IF a user is already signed in THEN
        if (mAuth.getCurrentUser() != null){
            //open the home page
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        //set Progress Dialog
        mDialog=new ProgressDialog(this);

        //ser variables to ids declared in xml file
        email=findViewById(R.id.email_login);
        pass=findViewById(R.id.password_login);

        //declare user interface
        //local variables set to IDs in login xml
        Button btnLogin = findViewById(R.id.login_btn);
        TextView signUp = findViewById(R.id.signup_txt);
        //listener for when login button is pressed
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declare log in sections
                final String mEmail=email.getText().toString().trim();
                final String mPass=pass.getText().toString().trim();

                //VALIDATION
                //IF email field is empty THEN send error
                if (TextUtils.isEmpty(mEmail)){
                    email.setError("*Email required");
                }

                //IF password field is empty THEN send error
                if (TextUtils.isEmpty(mPass)){
                    pass.setError("*Password required");
                }

                //IF email address does not match the format of an email THEN
                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    //display error message
                    Toast.makeText(getApplicationContext(), "Invalid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //IF length of password is less than 6 characters THEN
                if (mPass.length() < 6) {
                    //display error message
                    Toast.makeText(getApplicationContext(), "Please enter a password at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //processing message appears once all validation has been considered and there are no errors
                mDialog.setMessage("Processing...");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            //display success message to tell user registration has been made
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "WELCOME " + mEmail, Toast.LENGTH_SHORT).show();

                            //test that user is logged in
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user == null) {
                                Log.e(TAG, "NO USER LOGGED IN!!!");
                            }
                            else{
                                Log.e(TAG,"USER LOGGED IN [ " + user.getEmail() + "]");
                            }

                            //progress dialog dismissed
                            mDialog.dismiss();

                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to login! Check your details and try again.", Toast.LENGTH_SHORT).show();
                            //progress dialog dismissed
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });

        //when user presses sign up section, the user is taken to the register page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }
}