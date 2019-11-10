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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth firebaseAuth;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
//        uid= user.getUid();
        email=user.getEmail();

        ref= FirebaseDatabase.getInstance().getReference().child("User");


//                                         ref= FirebaseDatabase.getInstance().getReference("User").child(uid).child("transList");
//                                         double hi = 90;
//                                         ref.push().setValue(hi);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userDB = snapshot.getValue(User.class);
//                                                     Trans t = new Trans();
//                                                     t.setTransAmount(90);
//                                                     //double hi =90;
                    if(userDB.getEmail().equals(email)){
                        transHistory(userDB);


//

                    } //end if
                    // ref.child(snapshot.getKey()).child("transList").push().setValue(90);
                }//end for
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    private void transHistory(User userDB){

        if(userDB.getTrans1()!=0&&userDB.getTrans2()!=0&&userDB.getTrans3()!=0) {

            ref.child(userDB.getId()).child("trans1").setValue(0);
            ref.child(userDB.getId()).child("trans2").setValue(0);
            ref.child(userDB.getId()).child("trans3").setValue(0);
            ref.child(userDB.getId()).child("trans1").setValue(totalAmount);


        }
//


        if(userDB.getTrans1()==0){
            ref.child(userDB.getId()).child("trans1").setValue(totalAmount);
//
        }

        else if (userDB.getTrans2()==0){
            ref.child(userDB.getId()).child("trans2").setValue(totalAmount);

//
        }
        else if (userDB.getTrans3()==0){
            ref.child(userDB.getId()).child("trans3").setValue(totalAmount);

//
        }





    }
}
