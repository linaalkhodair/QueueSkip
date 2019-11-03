package com.example.queueskip.Adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.queueskip.Database.DataSource.CartRepository;
import com.example.queueskip.Database.Local.CartDataSource;
import com.example.queueskip.Database.Local.CartDatabase;
import com.example.queueskip.Database.ModelDB.Cart;
import com.example.queueskip.R;
import com.example.queueskip.ui.dashboard.DashboardFragment;
import com.example.queueskip.utliz.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.queueskip.ui.dashboard.DashboardFragment.totalAmount;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
   public  List<Cart> cartList;
    CartRepository cartRepository;
    ImageView delete;
    private Button okBtn, cancelBtn;
    private TextView dialogMsg;

    public CartAdapter(Context context,List<Cart> cartList){
        this.context=context;
        this.cartList=cartList;
    }
    

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);

        delete=itemView.findViewById(R.id.deleteItem);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        Picasso.with(context).load(cartList.get(position).link).into(holder.img_product);
        holder.txt_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txt_price.setText(cartList.get(position).Price+" SR");
        holder.txt_product_name.setText(cartList.get(position).name);
        Glide.with(context).load(cartList.get(position).link).into(holder.img_product);

        //dialog

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okBtn=dialog.findViewById(R.id.ok_btn_dialog);
        cancelBtn=dialog.findViewById(R.id.cancel_btn_dialog);
        dialogMsg=dialog.findViewById(R.id.dialog_message);
        dialogMsg.setText("Are you sure you want to delete item from cart?");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();

                                         }//end of onClick
                                     }//end of OnClickListener
        );

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.cartRepository.deleteCartItem(cartList.get(position));
                        dialog.cancel();
                    }
                });
               // Common.cartRepository.deleteCartItem(cartList.get(position));
            }
        });

        //auto save item when user change amount
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart=cartList.get(position);
                cart.amount=newValue;
                totalAmount=0;
                DashboardFragment.totalAmount(cartList);
               // cartRepository.updateCart(cart);
                Common.cartRepository.updateCart(cart);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void clear() {

            int size = cartList.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    cartList.remove(0);
                    Common.cartRepository.emptyCart(); //CHANGED
                }
                totalAmount=0;
                notifyItemRangeRemoved(0, size);
            }

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
