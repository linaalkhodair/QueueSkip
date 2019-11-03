package com.example.queueskip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queueskip.Adapter.CartAdapter;
import com.example.queueskip.ui.home.HomeFragment;
import com.example.queueskip.utliz.Common;

import static com.example.queueskip.ui.dashboard.DashboardFragment.btn_place_order;


public class SuccessActivity extends AppCompatActivity {

    private Button button2;
    RecyclerView recycler_cart;
    CartAdapter cartAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

      button2=findViewById(R.id.button2);

         Common.cartRepository.emptyCart();


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SuccessActivity.this, MainActivity.class));

            }
        });

    }



}
