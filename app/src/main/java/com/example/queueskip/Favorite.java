package com.example.queueskip;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.queueskip.Adapter.FavoriteAddapter;
import com.example.queueskip.Adapter.SearchAdapter1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Favorite extends AppCompatActivity implements FavoriteAddapter.OnItemClickListener {

    DatabaseReference reff;
    DatabaseReference refFav;
    private RecyclerView recyclerView;
    private List<Items> favoriteList = new ArrayList<>();
    private FavoriteAddapter favoriteAddapter;
    private FirebaseAuth firebaseAuth;
    private String email;
    View view;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate( saveInstanceState );
        setContentView( R.layout.favorite );




        recyclerView = (RecyclerView) findViewById( R.id.favorite);


        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setHasFixedSize( true );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser userAuth = firebaseAuth.getCurrentUser();
        email = userAuth.getEmail();
        refFav= FirebaseDatabase.getInstance().getReference("FavoriteList");



        reff= FirebaseDatabase.getInstance().getReference("User");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getEmail().equals(email)){

                        refFav= refFav.child(user.getId()).child("itemsList");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        refFav.addValueEventListener( new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d( "TTest", " I am in OnDataChange!!" );

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Items item = snapshot.getValue( Items.class );
                    // Toast.makeText(getActivity(),item.getName(),Toast.LENGTH_SHORT).show();
                    favoriteList.add( item );


                    //just for testing retrieving data

                    // text.setText(item.getName()+" Item retrieved successfully :)");
                }
               
                favoriteAddapter = new FavoriteAddapter( favoriteList, Favorite.this );
                recyclerView.setAdapter( favoriteAddapter );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );





    }
    @Override
    public void onItemClick(int position) {

    }
}
