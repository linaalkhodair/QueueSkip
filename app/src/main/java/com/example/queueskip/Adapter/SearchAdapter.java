/*package com.example.queueskip.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.queueskip.Items;
import com.example.queueskip.R;
import com.example.queueskip.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    //Filterable class has been removed

    private List<Items> productList_full=null;
    private List<Items> itemList;
    private Context context;
    ImageView delete;
    DatabaseReference ref;
    private Button okBtn, cancelBtn;
    private TextView dialogMsg;
    private List<Items>  productList=new ArrayList<>( );




   // public SearchAdapter(List<Items> itemList,Context context ) {
         public SearchAdapter(List<Items> itemList ) {
        this.itemList = itemList;
        //this.context=context;
        this.productList_full = new ArrayList<Items>();
        this.productList_full.addAll(itemList);
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new SearchAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder holder, final int position) {

        final Items product = itemList.get(position);

        // Pharmacy pharmacy= product.getPharmacy();
        holder.product_name.setText(product.getName());
        holder.product_price.setText(product.getPrice()+" SR");
        holder.product_expire.setText( product.getExpire() );
        Glide.with(context).load(product.getPhoto()).into(holder.product_img); //?? here


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);
        dialogMsg.setText("Are you sure you want to delete item?");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();

                                         }//end of onClick
                                     }//end of OnClickListener
        );


        ref = FirebaseDatabase.getInstance().getReference().child("items");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ref.child(product.getId()).removeValue();
                        ((Activity)context).finish(); //MAYBE?
                        dialog.cancel();
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                    }
                }); //inner onClick

            }
        }); //outer onClick


        //holder.product_img.setImageResource(product.getPhoto());
        // holder.distance.setText(pharmacy.getDistance());
        // holder.productImg.setImageResource(product.getImgId());

     /*   if(product.isAvaliable()) {
            holder.availablity.setText("Available");
            holder.circleIcon.setImageResource(R.drawable.circle_available);
        }
        else{
            holder.availablity.setText("Unavailable");
            holder.circleIcon.setImageResource(R.drawable.circle_unavailable);
        }*/

/*
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.queueskip.Items;
import com.example.queueskip.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;}
   public void removeItems(int position){
             itemList.remove( position );
             notifyItemRemoved( position );
   }

public void restoreIteme(Items item,int position){
             itemList.add(position,item);
             notifyItemInserted( position );

}
public List<Items> getItemList(){
             return itemList;
}

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,product_price,product_expire;
       public ImageView product_img;
        //public RelativeLayout viewForeground;

        public MyViewHolder (View view) {
            super(view);

            product_name = (TextView) view.findViewById( R.id.product_name);

            product_price=(TextView) view.findViewById(R.id.product_price);
            product_expire=(TextView) view.findViewById(R.id.product_expire);
            product_img=(ImageView) view.findViewById(R.id.product_img);
            delete = view.findViewById(R.id.deleteItemAdmin);


            //  viewForeground = view.findViewById(R.id.view_foregroundSearch);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            productListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }








    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void filter(String charText){
        charText = charText.toLowerCase( Locale.getDefault());
        itemList.clear();
        if(charText.length()==0)
            itemList.addAll(productList_full)   ;
        else{
            for(Items item:productList_full){
                if(item.getName().toLowerCase(Locale.getDefault()).contains( charText )){
                    itemList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
//.........
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    //........

    //......
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Items> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productList_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Items item : productList_full) {

                    if (item.getName().toLowerCase().contains(filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
//

        @Override
       protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    //......

    //====================================================================================//

    //........
    private SearchAdapter.OnItemClickListener productListener;
    private Context productContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(SearchAdapter.OnItemClickListener listener) {
        productListener = listener;
    }

//
//    public SearchAdapter(List<Items> productList_full, Context productContext) {
//        this.itemList = productList_full;
//        this.productList_full = new ArrayList<>(productList_full);
//        this.productContext = productContext;
//    }
    //..........


}*/