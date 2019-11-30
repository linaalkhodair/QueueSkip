package com.example.queueskip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AdminMainActivity extends AppCompatActivity {

    LinearLayout edit,addProduct;
    ImageView logout;
    private FirebaseAuth firebaseAuth;
    Dialog dialog;
    Button okBtn,cancelBtn ;
    LinearLayout notification;
    TextView dialogMsg;
    String emails[] = new String [100];
    DatabaseReference databaseReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        edit=findViewById(R.id.edit);
        addProduct=findViewById(R.id.add_product);
        logout = findViewById(R.id.logout_admin);
        notification= findViewById(R.id.notfiy);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //set toolbar
        Toolbar toolbar=findViewById(R.id.admin_main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("QueueSkip");

//        OneSignal.sendTag("email","Raazansaleh@gmail.com");



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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(AdminMainActivity.this);
                dialog.setContentView(R.layout.logout_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                okBtn=dialog.findViewById(R.id.ok_btn_dialog);
                cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
                dialogMsg=dialog.findViewById(R.id.dialog_message);


                dialogMsg.setText("Are you sure you want to send notification?");

                dialog.show();


                okBtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Query query1 = databaseReference.child("User").orderByChild("email");
                                                 query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                         int i = 0;
                                                         for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                             sendNotification(ds.child("email").getValue(String.class).toLowerCase());
                                                             //Log.e("TAG","ENTER111111111111111111");
                                                         }
                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {
                                                         dialog.cancel();

                                                     }
                                                 });
//                if(emails!=null)
//                for(int i = 0; i< emails.length; i++)
//                    if(emails[i]!=null)
//                        sendNotification(emails[i]);
                                                 //sendNotification();

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

    private void sendNotification(final String email)
    {


        dialog.cancel();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);



                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NGJkOTdjMGYtNDc0MC00MDM2LWI2NDItMThiODYyZjRjMjM0");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"a5561ea4-0127-4043-856c-40565cd6ed10\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"email\", \"relation\": \"=\", \"value\": \"" + email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"Dear customer,we have missed you! check out our new offers\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}
