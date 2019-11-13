package com.example.queueskip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;

import static com.example.queueskip.SignupActivity.VALID_EMAIL_ADDRESS_REGEX;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    DatabaseReference ref;
    private EditText editEmail;
    private EditText editUsername;
    private EditText editPass;
    private Button save;
    private Button cancel;
    String email;
    Button okBtn,cancelBtn;
    TextView dialogMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ref= FirebaseDatabase.getInstance().getReference("User");

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPass);

        editUsername = findViewById(R.id.editUser);
        save = findViewById(R.id.saveBtn);
//        cancel = findViewById(R.id.cancelBtn);

        editEmail.setText(user.getEmail());
        email = user.getEmail();
        final String id = user.getUid();

        //set toolbar
        Toolbar toolbar=findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if(user.getEmail().equals(email)){
                        editPass.setText(user.getPassword());
                        editUsername.setText(user.getUsername());
                    }
//                    Toast.makeText(EditProfile.this,"SUCCESSFULL EDIT", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //dialog

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);
        dialogMsg.setText("Are you sure you want to save changes ?");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();

                                         }//end of onClick
                                     }//end of OnClickListener
        );

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                if(validate()) {

                    //updating in Auth
//                user.updateEmail(editEmail.getText().toString());
//                user.updatePassword(editPass.getText().toString());

                    AuthCredential authCredential = EmailAuthProvider.getCredential(editEmail.getText().toString(), editPass.getText().toString());
                    user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

//                            FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateEmail(editEmail.getText().toString());
                            user.updatePassword(editPass.getText().toString());
                        }
                    });

                    //update in DB
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User userDB = snapshot.getValue(User.class);

                                if (userDB.getEmail().equals(email)) {

                                    ref.child(snapshot.getKey()).child("email").setValue(editEmail.getText().toString());
                                    ref.child(snapshot.getKey()).child("password").setValue(editPass.getText().toString());
                                    ref.child(snapshot.getKey()).child("username").setValue(editUsername.getText().toString());

                                }
                                Toast.makeText(EditProfile.this, "Changes successfully saved!", Toast.LENGTH_SHORT).show();
                            }
                        } //end onDataChange

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //end of DB save
                }//end of ifValidate()
                        dialog.dismiss();
                    } //inner onClick
                });
            } // end onClick
        }); //end SetOnClickListner



    } //end onCreate


    private boolean validate() {

        String nameInput = editUsername.getText().toString();
        String emailInput = editEmail.getText().toString();
        String passInput = editPass.getText().toString();
        //confPassInput = editConfPass.getText().toString();


        //if(nameInput.isEmpty()||emailInput.isEmpty()||passInput.isEmpty()||confPassInput.isEmpty()){
        if(nameInput.isEmpty()||emailInput.isEmpty()||passInput.isEmpty()){
            createDialog(getResources().getString(R.string.fill_required_fields));
            return false;

        }
        else
        if(!emailValidator(emailInput)) {
            createDialog(getResources().getString(R.string.invalid_email_address));
            return false;
        }

        else
            if(passInput.length()<6){
                createDialog("Password should be at least 6 characters");
                return false;
            }
        /*
        else
        if(!passInput.equals(confPassInput)){
            createDialog(getResources().getString(R.string.passwords_do_not_match));
            return false;

        }
        */
     /*else
          if(!checkEmailExistsOrNot()){
            createDialog(getResources().getString(R.string.already_registered));
            return false;
     }
        */



        //when all validations pass
        return true;

    }//end of validate

    private void createDialog(String message){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);


        dialogMsg.setText(message);

        okBtn.setText("OK");
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

    public static boolean emailValidator(String emailStr) {

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }//end of emailValidator

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end of onSupportNavigateUp
    } //end class
