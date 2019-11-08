package com.example.queueskip.ui.search;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.bumptech.glide.Glide;
import com.example.queueskip.Adapter.CartAdapter;
import com.example.queueskip.Adapter.SearchAdapter;
import com.example.queueskip.Items;
import com.example.queueskip.R;
import com.example.queueskip.ui.dashboard.DashboardViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class search_frag extends Fragment {
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

    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){


        ViewModelProviders.of(this).get( DashboardViewModel.class);
         view = inflater.inflate( R.layout.search, container, false);
       // elegantNumberButton=view.findViewById(R.id.txt_amount);
      /*  compositionDisposable = new CompositeDisposable();
        recycler_search = (RecyclerView) view.findViewById( R.id.search );
        recycler_search.setLayoutManager( new LinearLayoutManager( getContext() ) );
        recycler_search.setHasFixedSize( true );*/

       // BottomNavigationView navView = view.findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar=view.findViewById(R.id.toolbar);

        ((AppCompatActivity)mContext).setSupportActionBar(toolbar);
       // setTitle("Search");
      //  Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.filter_2);
//        drawable.setBounds(0, 0, 50, 50);
      //  navView.getMenu().getItem(1).setChecked(true);
       // toolbar.setOverflowIcon(drawable);
       // setTitle("Search");
//navView.getMenu().getItem( 1 ).setChecked( true );
setUpRecyclerView();
return view;
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
        recyclerView = (RecyclerView) view.findViewById( R.id.search );
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productAdapter= new SearchAdapter( productList,getContext());
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);

      //  productAdapter.setOnItemClickListener(search_frag.);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
         inflater = getActivity().getMenuInflater();
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
    }


   public boolean onOptionsItemSelected( MenuItem item){
       productAdapter.notifyDataSetChanged();
       if(item.getItemId()==R.id.action_search){
           searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
           searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) { return false; }

               @Override
               public boolean onQueryTextChange(String newText) {
                   productAdapter.getFilter().filter(newText);
                   return false;
               }
           });
       }
       return true;

   }

   /* @Override
    public void onItemClick(int position) {
        Log.i("log","ll");
        productName=productList.get(position).getName();
        price=productList.get(position).getPrice();
        pharmacyName = productList.get(position).getPharmacy().getName();
        distance = productList.get(position).getPharmacy().getDistance();
        productImg  = productList.get(position).getImgId();
        pharmcyLogo=productList.get(position).getPharmacy().getLogo();
        workingHours = productList.get(position).getPharmacy().getHours();
        phoneNum=productList.get(position).getPharmacy().getPhone();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.product_custom_dialog);
        productNameTxt=dialog.findViewById(R.id.product_name_dialog);
        priceTxt=dialog.findViewById(R.id.product_price_dialog);
        pharmcyNameTxt=dialog.findViewById(R.id.pharmacy_name_product_dialog);
        distanceTxt=dialog.findViewById(R.id.product_distance_dialog);
        productImgView=dialog.findViewById(R.id.product_img_dialog);
        pharmcyLogoView=dialog.findViewById(R.id.pharmacy_logo_product_dialog);
        workingHoursTxt=dialog.findViewById(R.id.hours);
        phoneNumTxt=dialog.findViewById(R.id.phone_num);

        closeBtn=dialog.findViewById(R.id.button_close);

        productNameTxt.setText(productName);
        priceTxt.setText(price);
        pharmcyNameTxt.setText(pharmacyName);
        distanceTxt.setText(distance);
        productImgView.setImageResource(productImg);
        pharmcyLogoView.setImageResource(pharmcyLogo);
        workingHoursTxt.setText(workingHours);
        phoneNumTxt.setText(phoneNum);


        closeBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }//end of onClick
                                    }//end of OnClickListener
        );
        dialog.show();

    }*/
}
