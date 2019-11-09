package com.example.queueskip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.queueskip.Adapter.CartAdapter;
import com.example.queueskip.ui.home.HomeFragment;
import com.example.queueskip.utliz.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.queueskip.ui.dashboard.DashboardFragment.btn_place_order;
import static com.example.queueskip.ui.dashboard.DashboardFragment.totalAmount;


public class SuccessActivity extends AppCompatActivity {
    DatabaseReference ref;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

//        ref= FirebaseDatabase.getInstance().getReference().child("User");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User userDB = snapshot.getValue(User.class);
//                    if(!userDB.getTransList().get(0).equals(5)){
//                        ref.child(snapshot.getKey()).child("transList").push().setValue(90);
//                        Toast.makeText(SuccessActivity.this, "SUCCESSFULL WORK", Toast.LENGTH_LONG).show();
//
//                    }
//
//                }//end for
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        button2=findViewById(R.id.button2);

        //clear cart
        Common.cartRepository.emptyCart();


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SuccessActivity.this, MainActivity.class));

            }
        });

    }

}
