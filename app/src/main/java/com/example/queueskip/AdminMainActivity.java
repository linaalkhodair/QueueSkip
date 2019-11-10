package com.example.queueskip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {

    LinearLayout edit,addProduct;
    ImageView logout;
    private FirebaseAuth firebaseAuth;
    Dialog dialog;
    Button okBtn,cancelBtn;
    TextView dialogMsg;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        edit=findViewById(R.id.edit);
        addProduct=findViewById(R.id.add_product);
        logout = findViewById(R.id.logout_admin);

        firebaseAuth = FirebaseAuth.getInstance();



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, search_frag.class));



            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, GenerateQRCode.class));


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createDialog();

            }
        });





    }

    public void logout(){
        firebaseAuth.signOut();
        //finish();
        dialog.dismiss();
        startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));

    }
    private void createDialog(){


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);


        dialogMsg.setText("Are you sure you want to log out?");

        dialog.show();

        okBtn.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         logout();
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


        /*
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    alertDialog.setMessage("Are you sure you want to logout?");


    alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
            logout();
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
*/
    }//end of createDialog
}
