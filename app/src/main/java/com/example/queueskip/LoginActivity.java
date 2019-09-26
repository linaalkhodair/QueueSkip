package com.example.queueskip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText Name;
    private EditText Pass;
    private TextView attempts;
    private Button login;
    private int counter = 5;//to count the incorrect attempts
    private TextView forgot;//not yet
    private TextView register;
    private FirebaseAuth firebaseAuth;
    //private ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = findViewById(R.id.username);
        Pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        attempts = findViewById(R.id.attempts);
        forgot = findViewById(R.id.forgot);
        register = findViewById(R.id.register);


        attempts.setText("Number of attempts remaining:5");// wrote down number 5 so it doesn't appear empty at first.

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser(); //checks if user already  logged in to direct him to the next activity

        if(user!=null &&user.getEmail().equals("queueskipad1@outlook.com")){
            finish();

            startActivity(new Intent(LoginActivity.this, GenerateQRCode.class));

        }
        else
        if (user != null) { //not very much
            finish();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Pass.getText().toString());

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }
        });

    }//end on create.

    private void validate(final String name, String pass) {

        if(!isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(name.trim(), pass.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(LoginActivity.this,MainActivity.class) );
                        if(name.equals("queueskipad1@outlook.com")){
                            finish();
                            startActivity(new Intent(LoginActivity.this, GenerateQRCode.class));
                        }
                        else
                        checkEmailVerification();
                    } else {
                        //Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            createDialog(getResources().getString(R.string.invalid_user));
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            createDialog(getResources().getString(R.string.invalid_email_or_pass));

                        } catch (FirebaseNetworkException e) {
                            createDialog(getResources().getString(R.string.network_failed));

                        } catch (IllegalArgumentException e) {
                            createDialog(getResources().getString(R.string.fill_required_fields)); //?maybe no need
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        counter--;
                        attempts.setText("Number of attempts remaining: " + counter);
                        if (counter == 0) {
                            login.setEnabled(false);
                        }
                    }
                }
            });

        }
    } //end of validate

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        Name = findViewById(R.id.username);


        if(Name.equals("queueskipad1@outlook.com")&&firebaseUser.getEmail().equals("queueskipad1@outlook.com")){
            finish();
            startActivity(new Intent(LoginActivity.this, GenerateQRCode.class));
        }
           else
        if (emailflag) {
            finish();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        } else {
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    } //end of checkEmailVerification

    public boolean isEmpty() {
        Name = findViewById(R.id.username);
        Pass = findViewById(R.id.pass);
        if (Name.getText().toString().isEmpty() || Pass.getText().toString().isEmpty()) {
            createDialog(getResources().getString(R.string.fill_required_fields));
            return true;
        }
        return false;
    } //end of isEmpty

    private void createDialog(String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setMessage(message);


        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }//end of onClick
                }//end of OnClickListener
        );

        alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }//end of onClick
                }//end of OnClickListener
        );
        alertDialog.show();

    }//end of createDialog

}//end of LoginActivity
