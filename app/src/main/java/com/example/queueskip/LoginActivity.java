package com.example.queueskip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
    private ProgressDialog progressDialog;
    private EditText email;
    private Button passwordReset;
    private Context mContext;
    private Button  closeBtn;
    private TextView Resetpass;
    private Button ResetBtn;
    private EditText resetEdit;

    Button okBtn,cancelBtn;
    TextView dialogMsg;


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

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");

        //underlines text
        SpannableString content = new SpannableString("Sign up!");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        register.setText(content);

        SpannableString content2 = new SpannableString("Forgot password?");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        forgot.setText(content2);



//        attempts.setText("Number of attempts remaining:5");// wrote down number 5 so it doesn't appear empty at first.

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser(); //checks if user already  logged in to direct him to the next activity

        if(user!=null &&user.getEmail().equals("queueskipad1@outlook.com")){
            finish();

            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));

        }
        else
        if (user != null) { //not very much
            finish();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString().trim(), Pass.getText().toString().trim());

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            }
        });

        //RESET PASSWORD
        forgot.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v){

                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.password_dialogue);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                closeBtn=dialog.findViewById(R.id.button_close_forgot);
                Resetpass=dialog.findViewById(R.id.passtxt);
                resetEdit=dialog.findViewById( R.id.reset );
                ResetBtn=dialog.findViewById( R.id.ResetBtn);

                dialog.show();

                ResetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String useremail = resetEdit.getText().toString().trim();

                        if(useremail.equals("")){
                            Toast.makeText(LoginActivity.this, "Please enter your registered email ID", Toast.LENGTH_SHORT).show();
                        }else{
                            firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                                        //finish();
                                        dialog.cancel();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } //end else
                    } //end onclick

                }); //end onCLickListener
                closeBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.cancel();

                                                }//end of onClick
                                            }//end of OnClickListener
                );

            }//end onclick  forgot


        }); //end onCLickListener forgot //END RESET PASSWORD

    }//end on create.

    private void validate(final String name, String pass) {
        progressDialog.show();

        if(!isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(name.trim(), pass.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        //Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(LoginActivity.this,MainActivity.class) );
                        if(name.equals("queueskipad1@outlook.com")){
                            finish();
                            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));

                        }
                        else
                        checkEmailVerification();
                    } else {
                        //attempts.setText("Number of attempts remaining: " + counter);
                        if (counter == 0) {
                            login.setEnabled(false);
                            login.setBackground(getResources().getDrawable(R.drawable.round_shape_btn_gray));
                            attempts.setText("Number of attempts remaining: " + counter);
                        }
                        //Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            createDialog(getResources().getString(R.string.invalid_user));
                            counter--;
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            createDialog(getResources().getString(R.string.invalid_email_or_pass));
                            counter--;

                        } catch (FirebaseNetworkException e) {
                            createDialog(getResources().getString(R.string.network_failed));
                        } catch (IllegalArgumentException e) {
                            createDialog(getResources().getString(R.string.fill_required_fields)); //?maybe no need
                            counter--;
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            counter--;
                        }


//                        counter--;
////                        attempts.setText("Number of attempts remaining: " + counter);
//                        if (counter == 0) {
//                            login.setEnabled(false);
//                            login.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                            attempts.setText("Number of attempts remaining: " + counter);
//                        }
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
            progressDialog.dismiss();
            finish();
            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
        }
           else
        if (emailflag) {
            progressDialog.dismiss();
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        } else {
            progressDialog.dismiss();
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);


        dialogMsg.setText(message);

        okBtn.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         dialog.cancel();
                                     }//end of onClick
                                 }//end of OnClickListener
        );

        cancelBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();

                                         }//end of onClick
                                     }//end of OnClickListener
        );
        cancelBtn.setVisibility(View.INVISIBLE);
      //  cancelBtn.setText("OK");

        dialog.show();

    }//end of createDialog

}//end of LoginActivity
