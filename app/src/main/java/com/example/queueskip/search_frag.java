package com.example.queueskip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.example.queueskip.Adapter.SearchAdapter1;
//import com.example.queueskip.Adapter.SearchAdapter1;
import com.example.queueskip.Items;
import com.example.queueskip.R;
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
import io.reactivex.disposables.CompositeDisposable;

       public   class  search_frag extends AppCompatActivity implements SearchAdapter1.OnItemClickListener {

      //private searchViewModel searchviewmodel;
      DatabaseReference reff;
      private RecyclerView recyclerView;
      private List<Items> productList = new ArrayList<>();
      private SearchAdapter1 productAdapter;
      SearchView searchView;
      View view;

     // final  OnGetDataListener listener;


      public void onCreate(Bundle saveInstanceState) {
          super.onCreate( saveInstanceState );
          setContentView( R.layout.search );

          //set toolbar
          Toolbar toolbar=findViewById(R.id.search_toolbar);
          setSupportActionBar(toolbar);
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          getSupportActionBar().setDisplayShowHomeEnabled(true);
          toolbar.setTitle("");



          recyclerView = (RecyclerView) findViewById( R.id.search );


          recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
          recyclerView.setHasFixedSize( true );
          recyclerView.setItemAnimator( new DefaultItemAnimator() );


          reff = FirebaseDatabase.getInstance().getReference().child( "items");
         // setItem(reff);
          reff.addValueEventListener( new ValueEventListener() {

              @Override

              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  Log.d( "TTest", " I am in OnDataChange!!" );

                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      Items item = snapshot.getValue( Items.class );
                      // Toast.makeText(getActivity(),item.getName(),Toast.LENGTH_SHORT).show();
                      productList.add( item );
                      Log.d( "TTest", productList.get( 0 ).getName() );

                      //just for testing retrieving data

                      // text.setText(item.getName()+" Item retrieved successfully :)");
                  }
                  Log.d( "TTest", productList.get( 0 ).getName() );
                  productAdapter = new SearchAdapter1( productList, search_frag.this );
                  recyclerView.setAdapter( productAdapter );

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }

          } );

          searchView=findViewById( R.id.action_search );

          searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  return false; }

              @Override
              public boolean onQueryTextChange(String newText) {
                  productAdapter.filter(newText);
                  return false;
              }
          });
          //



          //
          ///hhhheeerrrreee

          Log.d( "TTest", " I am here!!" );

          // new OnGetDataListener().onStart();



          ////hhhheeerrrreee

          //  prepareProductLists();
          //setUpRecyclerView();

      }

      public void setItem(DatabaseReference reff){
               readData(reff, new OnGetDataListener() {

                   @Override
                   public void onSuccess(DataSnapshot dataSnapshot) {

                       //whatever you need to do with the data
                       for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                           Items item = snapshot.getValue( Items.class );
                           // Toast.makeText(getActivity(),item.getName(),Toast.LENGTH_SHORT).show();
                           productList.add( item );
                           Log.d( "TTest", productList.get( 0 ).getName() );

                           //just for testing retrieving data

                           // text.setText(item.getName()+" Item retrieved successfully :)");
                       }
                       Log.d( "TTest", productList.get( 0 ).getName() );
                       productAdapter = new SearchAdapter1( productList, search_frag.this );
                       recyclerView.setAdapter( productAdapter );
                   }
                   @Override
                   public void onStart() {
                       //whatever you need to do onStart
                       Log.d("ONSTART", "Started");
                   }

                   @Override
                   public void onFailure() {


                   }
               });
           }
      public void readData(DatabaseReference ref, final OnGetDataListener listener){
          listener.onStart();
          reff.addValueEventListener( new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  Log.d( "TTest", " I am in OnDataChange!!" );

                  listener.onSuccess( dataSnapshot );

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }

          } );
          Log.d( "TTest", " I am here too!!" );
      }



      private void prepareProductLists(){


        Log.d("TTest"," I am here!!");
      reff =FirebaseDatabase.getInstance().

      getReference().

      child( "items");
        reff.addValueEventListener(new

      ValueEventListener() {
          @Override
          public void onDataChange (@NonNull DataSnapshot dataSnapshot){

              for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                  Items item = snapshot.getValue( Items.class );
                  // Toast.makeText(getActivity(),item.getName(),Toast.LENGTH_SHORT).show();
                  productList.add( item );
                  Log.d( "TTest", productList.get( 0 ).getName() );

                  //just for testing retrieving data

                  // text.setText(item.getName()+" Item retrieved successfully :)");
              }
              Log.d( "TTest", productList.get( 0 ).getName() );
              productAdapter = new SearchAdapter1( productList, search_frag.this );
              recyclerView.setAdapter( productAdapter );
          }

          @Override
          public void onCancelled (@NonNull DatabaseError databaseError){

          }
      });
        Log.d("TTest"," I am here too!!");


  }




    @Override
    public void onItemClick(int position) {

    }

           @Override
           public boolean onSupportNavigateUp() {
               onBackPressed();
               return true;
           }//end of onSupportNavigateUp





}