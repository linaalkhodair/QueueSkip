package com.example.queueskip.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.queueskip.Items;
import com.example.queueskip.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter1 extends RecyclerView.Adapter<SearchAdapter1.MyViewHolder> {
    private Context mContext;

    private List<Items> itemList;
    private ArrayList<Items> arrayList;
    private SearchAdapter1.OnItemClickListener mListener;

    public void setOnItemClickListener(SearchAdapter1.OnItemClickListener listener)
    {
        mListener=listener;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView product_name,product_price,product_expire;
        public ImageView product_img;

        public  MyViewHolder(View view , final OnItemClickListener listener){
            super(view);


            product_price=(TextView) view.findViewById( R.id.product_price);
            product_expire=(TextView) view.findViewById(R.id.product_expire);
            product_img=(ImageView) view.findViewById(R.id.product_img);
            //delete = view.findViewById(R.id.deleteItemAdmin);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position =getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            listener.onItemClick( position );
                        }
                    }

                }
            });
        }
    }
    public SearchAdapter1(List<Items> itemList,Context context){
        this.itemList=itemList;
        this.arrayList=new ArrayList<Items>(  );
        this.arrayList.addAll(itemList);
        this.itemList=itemList;
        mContext=context;
        Log.d(  "TTest", String.valueOf( itemList.get( 0 ) ) );
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent ,int position){
        View itemView= LayoutInflater.from( parent.getContext() ).inflate(R.layout.item_row,parent,false);
       return  new MyViewHolder(itemView, (OnItemClickListener) mListener );
       // return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        Log.d(  "TTest", String.valueOf( position ) );
        Items product=itemList.get(position);

        holder.product_name.setText(product.getName());
       // holder.product_price.setText(product.getPrice()+" SR");
        holder.product_price.setText(product.getPrice());
        holder.product_expire.setText( product.getExpire() );
        Glide.with(mContext).load(product.getPhoto()).into(holder.product_img);
    }
    @Override
    public int getItemCount(){
        return itemList.size();
    }
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
    public void filter(String charText){
        charText = charText.toLowerCase( Locale.getDefault());
        itemList.clear();
        if(charText.length()==0)
            itemList.addAll(arrayList)   ;
        else{
            for(Items item:arrayList){
                if(item.getName().toLowerCase(Locale.getDefault()).contains( charText )){
                    itemList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
