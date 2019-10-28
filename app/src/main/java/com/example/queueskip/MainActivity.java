package com.example.queueskip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.queueskip.ui.dashboard.DashboardFragment;
import com.example.queueskip.ui.home.HomeFragment;
//import com.example.queueskip.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity  {

    private FirebaseAuth firebaseAuth;
    //private Button logout;
    BottomNavigationView navView;
    MenuItem logoutBtn;
    Button okBtn,cancelBtn;
    TextView dialogMsg;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         navView = findViewById(R.id.nav_view);
         logoutBtn=navView.getMenu().getItem(3);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //.........

       // navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       // getSupportFragmentManager().beginTransaction().replace(R.id.container,new DashboardFragment()).commit();

        //.........



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.navigation_cart,R.id.navigation_camera,R.id.navigation_profile,
                R.id.navigation_logout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        firebaseAuth = FirebaseAuth.getInstance();
        logoutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            createDialog();
            return true;
            }
        });

//        logout = (Button)view.findViewById(R.id.logoutBtn);
//        logout.setOnClickListener(this);

    }
//
//    @Override
//    public void onClick(View v) {
//            logout();
//    }



    public void logout(){
        firebaseAuth.signOut();
        //finish();
        dialog.dismiss();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }

//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_cart:
//
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new DashboardFragment()).commit();
//                    navView.getMenu().getItem(0).setChecked(true);
//
//                    return true;
//                case R.id.navigation_camera:
//
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
//                   // navView.getMenu().getItem(1).setChecked(true);
//
//                    return true;
//                    /*
//                    //   getSupportFragmentManager().beginTransaction().replace(R.id.container,new SearchFragment()).commit();
//                    Intent intent = new Intent(getApplicationContext(),search_frag.class);
//                    startActivity(intent);
//                    overridePendingTransition(0,0);
//
//                     */
//
//                case R.id.navigation_logout:
//
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new NotificationsFragment()).commit();
//                   // navView.getMenu().getItem(2).setChecked(true);
//
//                    finish();
//
//                    return true;
//            }
//            return false;
//        }
//    };
//----------------------------------
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


}
