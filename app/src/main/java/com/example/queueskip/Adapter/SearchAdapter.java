package com.example.queueskip.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.queueskip.GenerateQRCode;
import com.example.queueskip.Items;
import com.example.queueskip.R;
import com.example.queueskip.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    //Filterable class has been removed

    private List<Items> productList_full;
    private List<Items> itemList;
    private Context context;
    ImageView delete;
    ImageView edit;
    DatabaseReference ref;
    private Button okBtn, cancelBtn;
    private Button okBtn2, cancelBtn2;
    private TextView dialogMsg;
    private ImageView calendar;
    private EditText editExp, editName, editPrice;
    private String name, price, expire;
    private List<Items>  productList=new ArrayList<>( );




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
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder holder, final int position) {
        final Items product = itemList.get(position);

        // Pharmacy pharmacy= product.getPharmacy();
        holder.product_name.setText(product.getName());
        holder.product_price.setText(product.getPrice()+" SR");
        holder.product_expire.setText( product.getExpire() );
        Glide.with(context).load(product.getPhoto()).into(holder.product_img); //?? here

        //  DELETE ITEM
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
                        Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();

                    }
                }); //inner onClick

            }
        }); //outer onClick


        //  EDIT ITEM
        final Dialog dialogEdit = new Dialog(context);
        dialogEdit.setContentView(R.layout.edit_dialog);
        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okBtn2= dialogEdit.findViewById(R.id.ok_btn_dialog);
        cancelBtn2= dialogEdit.findViewById(R.id.cancel_btn_dialog);
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
                editExp.setEnabled(false);
            }
        };

        //Expire date event listener

        cancelBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit.cancel();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

       edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialogEdit.show();
               okBtn2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       if (validate()) {
                           ref.child(product.getId()).child("name").setValue(editName.getText().toString());
                           ref.child(product.getId()).child("price").setValue(editPrice.getText().toString());
                           ref.child(product.getId()).child("expire").setValue(editExp.getText().toString());
                           dialogEdit.cancel();
                           ((Activity) context).finish();
                           Toast.makeText(context, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                       } //end if
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
            edit = view.findViewById(R.id.editItemAdmin);




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



    public boolean validate() {

        name = editName.getText().toString().trim();
        price = editPrice.getText().toString().trim();
        expire = editExp.getText().toString().trim();
        if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(price)) || (TextUtils.isEmpty(expire))) {
            Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_LONG).show();
            return false;
        } else if (!TextUtils.isDigitsOnly(price)) {
            Toast.makeText(context, "Price should be digits only", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }




    @Override
    public int getItemCount() {
        return itemList.size();
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


}