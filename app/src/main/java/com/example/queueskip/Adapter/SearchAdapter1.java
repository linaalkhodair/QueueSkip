package com.example.queueskip.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.queueskip.EditItem;
import com.example.queueskip.GenerateQRCode;
import com.example.queueskip.Items;
import com.example.queueskip.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter1 extends RecyclerView.Adapter<SearchAdapter1.MyViewHolder> {
    private Context mContext;

    private List<Items> itemList;
    private ArrayList<Items> arrayList;
    private SearchAdapter1.OnItemClickListener mListener;
    private Button okBtn, cancelBtn;
    private TextView dialogMsg;
    ImageView edit;
    private Button okBtn2, cancelBtn2;
    private ImageView calendar;
    private EditText editExp, editName, editPrice;
    private String name, price, expire;
    ImageView delete;
    DatabaseReference reff;

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

            product_name=(TextView) view.findViewById( R.id.product_name );
            product_price=(TextView) view.findViewById( R.id.product_price);
            product_expire=(TextView) view.findViewById(R.id.product_expire);
            product_img=(ImageView) view.findViewById(R.id.product_img);



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
        delete = itemView.findViewById(R.id.deleteItemAdmin);
        edit = itemView.findViewById(R.id.editItemAdmin);
       return  new MyViewHolder(itemView, (OnItemClickListener) mListener );

//        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        Log.d(  "TTest", String.valueOf( position ) );
        final Items product=itemList.get(position);
//put extra

        holder.product_name.setText(product.getName());
       // holder.product_price.setText(product.getPrice()+" SR");
        holder.product_price.setText(product.getPrice());
        holder.product_expire.setText( product.getExpire() );
        Glide.with(mContext).load(product.getPhoto()).into(holder.product_img);

        //Delete Item
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
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
        reff = FirebaseDatabase.getInstance().getReference().child("items");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff.child(product.getId()).removeValue();
                ((Activity)mContext).finish(); //MAYBE?
                dialog.cancel();
                Toast.makeText(mContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();

            }
        }); //inner onClick

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

//        Edit Item
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditItem.class).putExtra("ItemId", product.getId()).putExtra("ItemName", product.getName())
                        .putExtra("ItemExpire", product.getExpire()).putExtra("ItemPrice", product.getPrice());
                mContext.startActivity(intent);
               // ((Activity)mContext).finish();
            }
        }); //outer onClick

//
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
