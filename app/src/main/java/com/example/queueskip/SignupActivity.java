package com.example.queueskip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private Button registerBtn;
    EditText editName,editEmail,editPass,editConfPass;
    DatabaseReference reff;
    private User user;
    private FirebaseAuth firebaseAuth;
    boolean register = false;
    private String nameInput ;
    private String emailInput;
    private String passInput;
    private String confPassInput;
    private TextView login;
    private String uid;
    Button okBtn,cancelBtn;
    TextView dialogMsg;
    private ProgressDialog progressDialog;


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        reff= FirebaseDatabase.getInstance().getReference().child("User");
        uid = reff.push().getKey();


        //set toolbar
        Toolbar toolbar=findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        editName=findViewById(R.id.signUpNameET);
        editEmail=findViewById(R.id.signUpEmailET);
        editPass=findViewById(R.id.signUpPasswordET);
        editConfPass=findViewById(R.id.confPasswordET);
        registerBtn=findViewById(R.id.registerBtn);
        login=findViewById(R.id.login);

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Please wait...");

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(this);

        //underlines text
        SpannableString content = new SpannableString("Log in");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        login.setText(content);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    } // end of onCreate

    public void onClick(View view) {

         nameInput = editName.getText().toString();
         emailInput = editEmail.getText().toString();
         passInput = editPass.getText().toString();

        if(validate()) {

            firebaseAuth.createUserWithEmailAndPassword(emailInput,passInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendEmailVerification();
                    Toast.makeText(getApplicationContext(),
                            "inside onComplete",
                            Toast.LENGTH_SHORT).show();

//                         ArrayList transList = new ArrayList<Double>(6);
//
//                         Trans t = new Trans();
//                         t.setTransAmount(5);
//
//                         double hi = 5;
                       //  transList.set(0,hi);
//                        transList.add(t);
//                        transList.add(hi);
                        //create user in realtime DB
                        user = new User();
                        user.setUsername(nameInput);
                        user.setEmail(emailInput);
                        user.setPassword(passInput);
                        user.setId(uid);
                        user.setTrans1(0);
                        user.setTrans2(0);
                        user.setTrans3(0);
                        reff.child(uid).setValue(user);

                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                    else{
                        //MAYBE
                        progressDialog.dismiss();
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthUserCollisionException e){
                            progressDialog.dismiss();
                            createDialog(getResources().getString(R.string.already_registered));
                        }catch (FirebaseAuthWeakPasswordException e){
                            progressDialog.dismiss();
                            createDialog("Password should be at least 6 characters");
                        }
                        catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    } //inside onComplete[failed] //this is for if email already exists
                }
            });

        }

    }//end of onClick


    private boolean validate() {

        progressDialog.show();

        nameInput = editName.getText().toString();
        emailInput = editEmail.getText().toString();
        passInput = editPass.getText().toString();
        confPassInput = editConfPass.getText().toString();



     if(nameInput.isEmpty()||emailInput.isEmpty()||passInput.isEmpty()||confPassInput.isEmpty()){
         progressDialog.dismiss();
         createDialog(getResources().getString(R.string.fill_required_fields));
         return false;

     }
     else
     if(!emailValidator(emailInput)) {
         progressDialog.dismiss();
         createDialog(getResources().getString(R.string.invalid_email_address));
         return false;
     }
     else
     if(!passInput.equals(confPassInput)){
         progressDialog.dismiss();
         createDialog(getResources().getString(R.string.passwords_do_not_match));
         return false;

     }

     /*else
          if(!checkEmailExistsOrNot()){
            createDialog(getResources().getString(R.string.already_registered));
            return false;
     }
        */



      //when all validations pass
      return true;

    }

   /* public boolean checkEmailExistsOrNot(){
        final boolean[] exist = new boolean[1];
        firebaseAuth.fetchSignInMethodsForEmail(editEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = !task.getResult().getSignInMethods().isEmpty();
                if(!check){
                    Toast.makeText(getApplicationContext(),
                            "Email not found!",
                            Toast.LENGTH_SHORT).show();
                    exist[0] =true;
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "email already exists",
                            Toast.LENGTH_SHORT).show();
                    exist[0] =false;
                }
            }
        });
        return exist[0];
    }
    */

    /*
    public boolean checkEmailExistsOrNot(){
        boolean isNew=false;
        OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                }
            }
        };
        return isNew;
    }
    */


/*
    public boolean checkEmailExistsOrNot(){
        firebaseAuth.fetchSignInMethodsForEmail(editEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
               // Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                    Toast.makeText(getApplicationContext(), "email not existed", Toast.LENGTH_SHORT).show();
                    register=true;

                }else {
                    // email existed
                    Toast.makeText(getApplicationContext(), "email existed", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        return register;
    }

*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end of onSupportNavigateUp


    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //sendUserData();
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    } //end of sendEmailVerification

    public static boolean emailValidator(String emailStr) {

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }



    private void createDialog(String message){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);
        okBtn.setText("OK");

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

}//end of SignUp Activity


