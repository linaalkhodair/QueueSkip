//package com.example.queueskip;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.queueskip.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*public class Search  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchfield;
    private ImageButton searchbtn;
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.search );

        reff = FirebaseDatabase.getInstance().getReference().child( "items" );

        searchfield = (EditText) findViewById( R.id.searchfield );
        searchbtn = (ImageButton) findViewById( R.id.searchbtn );

        recyclerView = (RecyclerView) findViewById( R.id.search );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );


        searchbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchfield.getText().toString();
                firebaseItemsSearch( searchText );
            }
        } );
    }




private void firebaseItemsSearch(String searchText){

    Query firebaseSearchQuery =reff.orderByChild( "name" ).startAt( searchText ).endAt(searchText+"\uf8ff");

    FirebaseRecyclerAdapter<Items,ItemsViewHolder> FirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Items,ItemsViewHolder>(
            Items.class,
            R.layout.item_row,
            ItemsViewHolder.class,
            reff
    ){
        @Override
        protected void populateViewHolder(ItemsViewHolder viewHolder,Items model,int position){
            viewHolder.setDetails( model.getName(),model.getPrice(),model.getExpire(),model.getPhoto() );
    }
};
    recyclerView.setAdapter( FirebaseRecyclerAdapter );
}
    public static class ItemsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ItemsViewHolder (View itemView){
            super(itemView);
            mView=itemView;
        }
        public void setDetails( String name, String price ,String exire, String img){

            TextView product_name = (TextView) mView.findViewById( R.id.product_name);
            TextView product_price=(TextView) mView.findViewById(R.id.product_price);
            TextView product_expire=(TextView) mView.findViewById(R.id.product_expire);
            ImageView product_img=(ImageView) mView.findViewById(R.id.product_img);


            product_name.setText( name );
            product_price.setText( price );
            product_expire.setText( exire );
            //here image

        }
    }
}
//*/
