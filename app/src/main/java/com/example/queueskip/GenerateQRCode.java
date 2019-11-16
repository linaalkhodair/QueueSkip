package com.example.queueskip;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.HashMap;


public class GenerateQRCode extends AppCompatActivity {
    EditText text1;
    Button sub_btn;
    String text2QR, name, price, expire;
    EditText textPrice;
    EditText textExpire;
    DatabaseReference databaseItem;
    Bitmap bitmap;
    ImageView img;
    private ImageView logout;
    String temp;
    Uri filePath;

    private Button btn_upload, btn_choose,sear_btn;
    private ImageView imageView;
    private Uri filepath;
    private FirebaseStorage storage;
    private StorageReference storageReference, storageRef;
    private FirebaseAuth firebaseAuth;
    DataSnapshot dataSnapshot;
    String id;

    //DatabaseReference mDatabase;
    private ImageView calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //you need to chang it

        setContentView(R.layout.activity_generate_qr_code);

        //Find views By ID

        text1 = (EditText) findViewById(R.id.text1);
        sub_btn = (Button) findViewById(R.id.sub_btn);
        textPrice = (EditText) findViewById(R.id.text2);
        textExpire = (EditText) findViewById(R.id.text3);
        btn_choose = (Button) findViewById(R.id.btn_choose);
        img = (ImageView) findViewById(R.id.image);
        //  imageView = (ImageView) findViewById(R.id.myImage);
       // logout = findViewById(R.id.admin_logout);
        calendar =findViewById(R.id.calendar);
       // sear_btn=findViewById( R.id.sear_btn );

        //set toolbar
        Toolbar toolbar=findViewById(R.id.add_product_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Database
        //  databaseItem = FirebaseDatabase.getInstance().getReference();//???????

        databaseItem = FirebaseDatabase.getInstance().getReference("items");//???????
        id = databaseItem.push().getKey();

        firebaseAuth = FirebaseAuth.getInstance();

        //Storage for an image

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //choose image event listener

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),1);
            }
        });
//        sear_btn.setOnClickListener( new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent=new Intent(GenerateQRCode.this, search_frag.class);
//                startActivity(intent);
//            }
//        } );
        //Calendar settings

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
                textExpire.setEnabled(false);
            }
        };

        //Expire date event listener

        calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(GenerateQRCode.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Logout event listener

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logout();
//            }
//        });

        //Add Item event listener

        sub_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //text2QR = "Item:" + text.getText().toString().trim() + " " + "\n"+"Price" + textPrice.getText().toString().trim() + " SR " +"\n"+ "Expiration date" + textExpire.getText().toString().trim();
                // text2QR = text1.getText().toString().trim() + "-" + textPrice.getText().toString().trim() + "-" + textExpire.getText().toString().trim();//?
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                if (validate()) {
                    try {
                        ImageLink();
                        text2QR =text1.getText().toString().trim()+"-" + textPrice.getText().toString().trim() + "-" + textExpire.getText().toString().trim()+"-"+id;
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        img.setImageBitmap(bitmap);
                        //
                        //  ImageLink();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }//end if validated
            }
        });

    } //end onCreate method


    //Validate method

    public boolean validate() {
        name = text1.getText().toString().trim();
        price = textPrice.getText().toString().trim();
        expire = textExpire.getText().toString().trim();
        if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(price)) || (TextUtils.isEmpty(expire))) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_LONG).show();
            return false;
        } else if (!TextUtils.isDigitsOnly(price)) {
            Toast.makeText(this, "Price should be digits only", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //Logout method

    public void logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
        // getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    //IDK :)

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //On choosing an image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode== RESULT_OK && data!=null && data.getData() !=null){

            filepath = data.getData();}
//        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
//        imageView.setImageBitmap(bitmap);
//        BitMapToString(bitmap);
    }

    //Add Image to Database Storage

    private void ImageLink() {

        if(filepath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(GenerateQRCode.this);
            progressDialog.setTitle("Image uploaded");
            progressDialog.show();

            if (progressDialog!= null) {
                progressDialog.dismiss();
            }

            storageRef = storageReference.child("images/" + UUID.randomUUID().toString());

            storageRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            saveToDatabase(uri.toString());
                            progressDialog.dismiss();
                        }
                    });
                }
            });

        }else {

            saveToDatabase("https://i-love-png.com/images/no-image_7299.png");
        }
    }



    //Save Item in database

    public void saveToDatabase(String ImageLink){

        //Item Id

//        id = databaseItem.push().getKey();


        //put Item inside hash map

        Items item=new Items(id,name,price,expire,ImageLink );
        databaseItem.child( id ).setValue( item );

//       String itemId = databaseItem.push().getKey();
//
//
//        //put Item inside hash map
//
//        Items item=new Items(name,price,expire,ImageLink);
//        HashMap<String, Items> hashMap = new HashMap<>();
//        hashMap.put( itemId , item);
//
//       // databaseItem.child(itemId).setValue(hashMap);
//
//        //Toast.makeText(this, "Successful insertion", Toast.LENGTH_LONG).show();
//
//
//        //databaseItem.child("Items").child(itemId).setValue(hashMap); //duplicated id's
//        databaseItem.setValue(hashMap); //two id's diff



        //databaseItem.child("Items").child(itemId).push().setValue(hashMap); //??
        //without push stores only one and overwrites
        //here the retrieve items code

        databaseItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //  Items item = snapshot.child(snapshot.getKey()).getValue(Items.class);
                    Items item = snapshot.getValue(Items.class);
                    //just for testing retrieving data

                    // text1.setText(item.getName() + " Item retrieved successfully :)"); //TESTING PURPOSES
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    } //end saveToDatabase

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end of onSupportNavigateUp

}
