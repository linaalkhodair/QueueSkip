package com.example.queueskip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.queueskip.Adapter.SearchAdapter1;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class EditItem extends AppCompatActivity {
    private EditText editExp, editName, editPrice;
    private ImageView calendar;
    private String name, price, expire;
    private Button save;
    DatabaseReference reff;
    private String id ="";
    private String itemName, itemPrice, itemExpire;
    Button okBtn,cancelBtn;
    TextView dialogMsg;
    private Button upload;
    private FirebaseStorage storage;
    private StorageReference storageReference, storageRef;
    Uri filePath;
    private Uri filepath;
    String ImageLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        reff = FirebaseDatabase.getInstance().getReference().child("items");
        id = getIntent().getExtras().getString("ItemId");
        itemName = getIntent().getExtras().getString("ItemName");
        itemExpire = getIntent().getExtras().getString("ItemExpire");
        itemPrice = getIntent().getExtras().getString("ItemPrice");



        editExp = findViewById(R.id.editExp);
        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        calendar = findViewById(R.id.calendar);
        save = findViewById(R.id.save_btn);
        upload=findViewById(R.id.upload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Toolbar toolbar=findViewById(R.id.edit_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Edit Item");

        editExp.setText(itemExpire);
        editName.setText(itemName);
        editPrice.setText(itemPrice);

        //Calendar settings...
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
                Log.d("Test1", "Inside date setting");
                editExp.setText(sdf.format(myCalendar.getTime()));
                editExp.setEnabled(false);
            }
        };
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),1);
            }
        });



        calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditItem.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


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
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TTest",editName.getText().toString());
                Log.d("TTest","I am in before validate");

                //IMAGE !!!!!!!!!!!!!!!!!!!

                if(filepath!=null){
                    final ProgressDialog progressDialog = new ProgressDialog(EditItem.this);
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
                                    ImageLink = uri.toString();
                                    reff.child(id).child("photo").setValue(ImageLink);
                                    Log.d("testImg", "image link  is : "+ImageLink);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });

                }else {

                    ImageLink= "https://i-love-png.com/images/no-image_7299.png";
                }
                Log.d("Test01", "image link is: "+ ImageLink);
                if (validate()) {
                    reff.child(id).child("name").setValue(editName.getText().toString());
                    reff.child(id).child("price").setValue(editPrice.getText().toString());
                    reff.child(id).child("expire").setValue(editExp.getText().toString());
//                    reff.child(id).child("photo").setValue(ImageLink);
                    Toast.makeText(EditItem.this
                            , "Item updated successfully!", Toast.LENGTH_SHORT).show();
                   finish();
                } //end if
            }
        }); //inner onClick


    } // end onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode== RESULT_OK && data!=null && data.getData() !=null){

            filepath = data.getData();}
//        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
//        imageView.setImageBitmap(bitmap);
//        BitMapToString(bitmap);
    }

    private boolean validate() {
        name = editName.getText().toString().trim();
        price = editPrice.getText().toString().trim();
        expire = editExp.getText().toString().trim();
//        ImageLink();
//        Log.d("Test01", "image link is: "+ ImageLink);

        Log.d("Test2", "i'm here"+editName.getText().toString());

        if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(price)) || (TextUtils.isEmpty(expire)) ) {
            Toast.makeText(EditItem.this, "Please fill in all required fields", Toast.LENGTH_LONG).show();
            Log.d("TTest","The name is empty");
            return false;
        } else if (!TextUtils.isDigitsOnly(price)) {
            Toast.makeText(EditItem.this, "Price should be digits only", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    } //end validate

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
        }//end of onSupportNavigateUp

    private void ImageLink() {

        if(filepath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(EditItem.this);
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
                            ImageLink = uri.toString();
                            Log.d("testImg", "image link  is : "+ImageLink);
                            progressDialog.dismiss();
                        }
                    });
                }
            });

        }else {

            ImageLink= "https://i-love-png.com/images/no-image_7299.png";
        }
    }


    } //end Class




