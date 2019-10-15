package com.example.queueskip.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.queueskip.Database.DataSource.CartRepository;
import com.example.queueskip.Database.Local.CartDataSource;
import com.example.queueskip.Database.Local.CartDatabase;
import com.example.queueskip.Database.ModelDB.Cart;
import com.example.queueskip.R;
import com.example.queueskip.utliz.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
   public  List<Cart> cartList;
    CartRepository cartRepository;

    public CartAdapter(Context context,List<Cart> cartList){
        this.context=context;
        this.cartList=cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        Picasso.with(context).load(cartList.get(position).link).into(holder.img_product);
        holder.txt_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txt_price.setText("Price: "+cartList.get(position).Price);
        holder.txt_product_name.setText("Item: "+cartList.get(position).name);

        //auto save item when user change amount
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart=cartList.get(position);
                cart.amount=newValue;
               // cartRepository.updateCart(cart);
                Common.cartRepository.updateCart(cart);

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView img_product;
        TextView txt_product_name,txt_sugar_ice,txt_price;
        ElegantNumberButton txt_amount;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product =(ImageView) itemView.findViewById(R.id.img_product);
            txt_amount=(ElegantNumberButton) itemView.findViewById(R.id.txt_amount);
            txt_product_name=(TextView) itemView.findViewById(R.id.txt_product_name);
            txt_price=(TextView) itemView.findViewById(R.id.txt_price);
           // txt_amount=(ElegantNumberButton) itemView.findViewById(R.id.txt_amount);

        }
    }
}
