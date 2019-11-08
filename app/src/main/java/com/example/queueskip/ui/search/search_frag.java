package com.example.queueskip.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.queueskip.Adapter.SearchAdapter;
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

public class search_frag extends AppCompatActivity implements SearchAdapter.OnItemClickListener  {
    CompositeDisposable compositionDisposable;
    RecyclerView recycler_search;
    private searchViewModel searchviewmodel;
    DatabaseReference reff;
    private RecyclerView recyclerView;
    private List<Items>  productList=new ArrayList<>( );
    private SearchAdapter productAdapter;
    SearchView searchView;
    View view;
    Context mContext;



    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search);


       // elegantNumberButton=view.findViewById(R.id.txt_amount);
      /*  compositionDisposable = new CompositeDisposable();
        recycler_search = (RecyclerView) view.findViewById( R.id.search );
        recycler_search.setLayoutManager( new LinearLayoutManager( getContext() ) );
        recycler_search.setHasFixedSize( true );*/

       // BottomNavigationView navView = view.findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setTitle("Search");
       // setTitle("Search");
      //  Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.filter_2);
//        drawable.setBounds(0, 0, 50, 50);
      //  navView.getMenu().getItem(1).setChecked(true);
       // toolbar.setOverflowIcon(drawable);
       // setTitle("Search");
//navView.getMenu().getItem( 1 ).setChecked( true );

    setUpRecyclerView();

    }

    private void prepareProductLists()

    {
        reff = FirebaseDatabase.getInstance().getReference().child( "items" );
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Items item = snapshot.getValue( Items.class );
                    // Toast.makeText(getActivity(),item.getName(),Toast.LENGTH_SHORT).show();
                    productList.add( item );


                    //just for testing retrieving data

                    // text.setText(item.getName()+" Item retrieved successfully :)");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
   /* private void prepareProducts(){
       for(int i=0;i<productList.size();i++)
           List<Items> products=productList.get.(i)
    }*/

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById( R.id.search );
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productAdapter= new SearchAdapter( productList,this);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(  search_frag.this );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions( EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                // productAdapter.getFilter().filter(newText);
               return false;
            }
        });
        return true;
    }


   public boolean onOptionsItemSelected( MenuItem item){
       productAdapter.notifyDataSetChanged();
       if(item.getItemId()==R.id.action_search){
           searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
           searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) {
                   return false; }

               @Override
               public boolean onQueryTextChange(String newText) {
                   productAdapter.getFilter().filter(newText);
                   return false;
               }
           });
       }
       return true;

   }


    @Override
    public void onItemClick(int position) {

    }
}
