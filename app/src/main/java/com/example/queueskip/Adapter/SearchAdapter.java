package com.example.queueskip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.queueskip.Items;
import com.example.queueskip.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>  {

    //Filterable class has been removed

    private List<Items> productList_full;
    private List<Items> itemList;
    private Context context;


    public SearchAdapter(List<Items> itemList,Context context ) {
        this.itemList = itemList;
        this.context=context;
        productList_full = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new SearchAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder,final int position) {
        Items product = itemList.get(position);

        // Pharmacy pharmacy= product.getPharmacy();
        holder.product_name.setText(product.getName());
        holder.product_price.setText(product.getPrice());
        holder.product_expire.setText( product.getExpire() );
        Glide.with(context).load(product.getPhoto()).into(holder.product_img); //?? here

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

//.........
//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }
    //........

    //......
//    private Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Items> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(productList_full);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (Items item : productList_full) {
//
//                    if (item.getName().toLowerCase().contains(filterPattern)) {
//
//                        filteredList.add(item);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            itemList.clear();
//            itemList.addAll((List)results.values);
//            notifyDataSetChanged();
//        }
//    };
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


}