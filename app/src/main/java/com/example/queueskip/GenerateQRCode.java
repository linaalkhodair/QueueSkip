package com.example.queueskip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class GenerateQRCode extends AppCompatActivity {
    EditText text;
    Button sub_btn;
    ImageView image;
    String text2QR;
    EditText textPrice;
    EditText textExpire;
    DatabaseReference databaseItem;
    Bitmap bitmap;
    private ImageView logout;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);
        text =(EditText) findViewById(R.id.text);
        sub_btn=(Button) findViewById(R.id.sub_btn);
        image=(ImageView) findViewById(R.id.image);
        textPrice=(EditText) findViewById(R.id.text1);
        textExpire=(EditText)findViewById(R.id.text3) ;
        databaseItem = FirebaseDatabase.getInstance().getReference("item");
        firebaseAuth = FirebaseAuth.getInstance();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textExpire.setText(sdf.format(myCalendar.getTime()));
            }

        };

        textExpire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(GenerateQRCode.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        logout = findViewById(R.id.admin_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                text2QR = "Item:" + text.getText().toString().trim() + " " + "Price" + textPrice.getText().toString().trim() + " SR " + "Expiration date" + textExpire.getText().toString().trim();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                if (validate()) {
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        image.setImageBitmap(bitmap);
                        addItem();

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
            }//end if
            }
        });

    }

    public boolean validate(){
        String name=text.getText().toString().trim();
        String price=textPrice.getText().toString().trim();
        String expire=textExpire.getText().toString().trim();
        if((TextUtils.isEmpty(name)) ||(TextUtils.isEmpty(price))||(TextUtils.isEmpty(expire))){
            Toast.makeText(this,"You should fill all fields",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!TextUtils.isDigitsOnly(price)){
            Toast.makeText(this,"Price should be digits only",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

        } //end validate

    public void logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
        // getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
    private void addItem(){
        String name=text.getText().toString().trim();
        String price=textPrice.getText().toString().trim();
        String expire=textExpire.getText().toString().trim();

        if((!TextUtils.isEmpty(name)) &&(! TextUtils.isEmpty(price))&&(! TextUtils.isEmpty(expire))){
            String id=databaseItem.push().getKey();
            Items item=new Items(id,name,price,expire);
            databaseItem.child(id).setValue(item);
            Toast.makeText(this,"The item is added successfully ",Toast.LENGTH_LONG).show();


        }
        else{
            Toast.makeText(this,"You should fill all fields",Toast.LENGTH_LONG).show(); // ithink would be no need?
        }
    }


}