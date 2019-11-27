package com.example.queueskip.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
            delete = view.findViewById(R.id.deleteItemAdmin);
            edit = view.findViewById(R.id.editItemAdmin);


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
        final Items product=itemList.get(position);

        holder.product_name.setText(product.getName());
       // holder.product_price.setText(product.getPrice()+" SR");
        holder.product_price.setText(product.getPrice());
        holder.product_expire.setText( product.getExpire() );
        Glide.with(mContext).load(product.getPhoto()).into(holder.product_img);


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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reff.child(product.getId()).removeValue();
                        ((Activity)mContext).finish(); //MAYBE?
                        dialog.cancel();
                        Toast.makeText(mContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                    }
                }); //inner onClick

            }
        });

        //  EDIT ITEM
        final Dialog dialogEdit = new Dialog(mContext);
        dialogEdit.setContentView(R.layout.edit_dialog);
        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okBtn2= dialogEdit.findViewById(R.id.ok_btn_edit_dialog);
        cancelBtn2= dialogEdit.findViewById(R.id.cancel_btn_edit_dialog);
        editExp = dialogEdit.findViewById(R.id.editExp);
        editName = dialogEdit.findViewById(R.id.editName);
        editPrice = dialogEdit.findViewById(R.id.editPrice);
        calendar = dialogEdit.findViewById(R.id.calendar);

        //Calendar settings...
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editExp.setText(sdf.format(myCalendar.getTime()));
                //editExp.setEnabled(false);
            }
        };

        //Expire date event listener

        cancelBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //?    dialog.hide();
                dialogEdit.cancel();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //?    dialog.hide();
                dialogEdit.show();
                okBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (validate()) {
                            reff.child(product.getId()).child("name").setValue(editName.getText().toString());
                            reff.child(product.getId()).child("price").setValue(editPrice.getText().toString());
                            reff.child(product.getId()).child("expire").setValue(editExp.getText().toString());
                            dialogEdit.cancel();
                            ((Activity) mContext).finish();
                            Toast.makeText(mContext, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                        } //end if
                    }
                }); //inner onClick

            }
        }); //outer onClick


    }
    public boolean validate() {
        name = editName.getText().toString();
        price = editPrice.getText().toString();
        expire = editExp.getText().toString();
        if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(price)) || (TextUtils.isEmpty(expire))) {
            Toast.makeText(mContext, "Please fill in all required fields", Toast.LENGTH_LONG).show();
            return false;
        } else if (!TextUtils.isDigitsOnly(price)) {
            Toast.makeText(mContext, "Price should be digits only", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
