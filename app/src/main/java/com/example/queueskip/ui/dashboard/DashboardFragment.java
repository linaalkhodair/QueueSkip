package com.example.queueskip.ui.dashboard;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.queueskip.Adapter.CartAdapter;
import com.example.queueskip.CheckoutActivity;
import com.example.queueskip.Database.ModelDB.Cart;
import com.example.queueskip.R;
import com.example.queueskip.SuccessActivity;
//import com.example.queueskip.Trans;
import com.example.queueskip.User;
import com.example.queueskip.utliz.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DashboardFragment extends Fragment {

    private ElegantNumberButton elegantNumberButton;
    RecyclerView recycler_cart;
    public static Button btn_place_order;
    TextView total;
    CompositeDisposable compositionDisposable;
    public static Button clear_btn;
    CartAdapter cartAdapter;
    public static double totalAmount = 0; //changed to double?
    private Button checkoutBtnDialog;
    private TextView totalPriceDialog;
    private Button closeBtn;
    private Button okBtn, cancelBtn;
    private TextView dialogMsg;
    private static TextView totalP;
    private static TextView vat;
    private static TextView totalPay;
    private static TextView discountAmount;


    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        elegantNumberButton=view.findViewById(R.id.txt_amount);


        //        cartBut = (Button)view.findViewById(R.id.cartAct); //new new new added
//        testText = view.findViewById(R.id.testText); //??new new added
//
//        cartBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), cartActivity.class));
//            }
//        });
        // final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        compositionDisposable = new CompositeDisposable();
        recycler_cart = (RecyclerView) view.findViewById( R.id.recycler_cart );
        recycler_cart.setLayoutManager( new LinearLayoutManager( getContext() ) );
        recycler_cart.setHasFixedSize( true );



        btn_place_order = (Button) view.findViewById( R.id.btn_place_order );

        totalP = view.findViewById(R.id.tp);
        vat = view.findViewById(R.id.vat);
        totalPay = view.findViewById(R.id.totalPay);//Total after VAT
        discountAmount=view.findViewById(R.id.discountAmount);
        loadCartItems(); //it was a comment!!!!!
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();

              //  startActivity(new Intent(getContext(), CheckoutActivity.class));

            }
        });

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);
        dialogMsg.setText("Are you sure you want to clear cart?");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();

                                         }//end of onClick
                                     }//end of OnClickListener
        );

        clear_btn=(Button) view.findViewById( R.id.clear );
        //if cart is empty
        int size = Common.cartRepository.countCartItems();
        if (size==0){
            clear_btn.setEnabled(false);
            clear_btn.setBackground(getResources().getDrawable(R.drawable.round_shape_btn_gray));
        }

        clear_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartAdapter.clear();
                        btn_place_order.setText("Checkout"+"     Total price: "+0+"SR");
                        dialog.cancel();
                        clear_btn.setEnabled(false);
                        clear_btn.setBackground(getResources().getDrawable(R.drawable.round_shape_btn_gray));
                    }
                });


                // then reload the data

            }
        } );
        return view;
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.checkout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        checkoutBtnDialog=dialog.findViewById(R.id.checkout_btn_dialog);
        totalPriceDialog=dialog.findViewById(R.id.checkout_dialog_amount);
        closeBtn=dialog.findViewById(R.id.button_close);

        totalPriceDialog.setText("Total price: "+totalAmount+" SR");

        dialog.show();


        checkoutBtnDialog.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
//
////                                         firebaseAuth = FirebaseAuth.getInstance();
////                                         final FirebaseUser user = firebaseAuth.getCurrentUser();
////                                         Log.d("here", "hi, "+user.getUid());
////                                             String id=user.getUid();
////                                        email = user.getEmail();
//                                        //uid
//
//                                         ref= FirebaseDatabase.getInstance().getReference().child("User");
//
//
////                                         ref= FirebaseDatabase.getInstance().getReference("User").child(uid).child("transList");
////                                         double hi = 90;
////                                         ref.push().setValue(hi);
//                                         ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                             @Override
//                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                                     User userDB = snapshot.getValue(User.class);
////                                                     Trans t = new Trans();
////                                                     t.setTransAmount(90);
////                                                     //double hi =90;
//                                                     if(userDB.getEmail().equals(email)){
//                                                        transHistory(userDB);
//
//
////
//
//                                                     } //end if
//                                                    // ref.child(snapshot.getKey()).child("transList").push().setValue(90);
//                                                 }//end for
//                                             }
//
//                                             @Override
//                                             public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                             }
//                                         });

                                         startActivity(
                                                 new Intent(getContext(), CheckoutActivity.class));
                                      //cartAdapter.clear();
                                     }//end of onClick
                                 }//end of OnClickListener
        );
        closeBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();

                                         }//end of onClick
                                     }//end of OnClickListener
        );

    }

    @Override
    public void onStop() {
        compositionDisposable.clear();
        super.onStop();
    }


    @Override
    public void onDestroy() {
        totalAmount=0;
        compositionDisposable.clear();
        super.onDestroy();
    }

    private void loadCartItems() {

        compositionDisposable.add(

                Common.cartRepository.getCartItems().observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io() )
                        .subscribe( new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) {
                                displayCartItem(carts);
                            }
                        } )



        );


    }

    private void displayCartItem(List<Cart> carts){
        cartAdapter=new CartAdapter(getContext(),carts);
        recycler_cart.setAdapter(cartAdapter);
        totalAmount(carts);
    }

    public static void totalAmount(List<Cart> cartList){
//        int totalAmount = 0;
        int price;
        int amount;
        totalAmount=0;
        for(int i=0; i<cartList.size(); i++){
            //elegantNumberButton.setNumber(String.valueOf(cartList.get(i).amount));

            price= cartList.get(i).Price;
            amount= cartList.get(i).amount;
            totalAmount+=  price*amount;
        }
        double vat1 =totalAmount*0.05;
        totalP.setText(String.format("%.2f", totalAmount)+"SR");
        if(totalAmount>=100){//checcckkk itt!!!
           double discountAmountt;
           discountAmountt=0.10*totalAmount;
           totalAmount=totalAmount-discountAmountt;
            discountAmount.setText("-"+String.format("%.2f", discountAmountt)+"SR");
        }
        else{  discountAmount.setText("-0.00SR");}

        DecimalFormat df = new DecimalFormat("#.###");
        df.format(vat1);
        btn_place_order.setText("Check out");
        vat.setText(String.format("%.2f", vat1)+"SR");
        totalPay.setText(String.format("%.2f",totalAmount+vat1)+"SR");

       totalAmount=totalAmount+vat1; //NOT SURE

//        total.setText(""+totalAmount);
//        return totalAmount;

    }



}