package com.example.queueskip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.queueskip.Adapter.SearchAdapter1;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditItem extends AppCompatActivity {
    private EditText editExp, editName, editPrice;
    private ImageView calendar;
    private String name, price, expire;
    private Button save;
    DatabaseReference reff;
    private String id ="";
    private String itemName, itemPrice, itemExpire;

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

        calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditItem.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TTest",editName.getText().toString());
                Log.d("TTest","I am in before validate");



                if (validate()) {

                    reff.child(id).child("name").setValue(editName.getText().toString());
                    reff.child(id).child("price").setValue(editPrice.getText().toString());
                    reff.child(id).child("expire").setValue(editExp.getText().toString());
                    Toast.makeText(EditItem.this
                            , "Item updated successfully!", Toast.LENGTH_SHORT).show();
                   finish();
                } //end if
            }
        }); //inner onClick


    } // end onCreate

    private boolean validate() {
        name = editName.getText().toString().trim();
        price = editPrice.getText().toString().trim();
        expire = editExp.getText().toString().trim();
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

    }

