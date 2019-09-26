package com.example.queueskip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
                text2QR="Item:"+text.getText().toString().trim()+" "+"Price"+textPrice.getText().toString().trim()+" SR " +"Expiration date"+textExpire.getText().toString().trim();
                MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix =multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                    bitmap=barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                    addItem();

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

    }
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
            Toast.makeText(this,"You should fill all fields",Toast.LENGTH_LONG).show();
        }
    }

}