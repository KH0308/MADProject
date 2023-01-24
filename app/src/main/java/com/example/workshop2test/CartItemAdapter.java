package com.example.workshop2test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyView> {

    private Context mContext;
    private List<CartItem> mData;
    double overallTotalPrice = 0;
    // member variable for the TextView
    TextView textViewTotal;


    public CartItemAdapter(Context mContext, List<CartItem> mData, TextView textViewTotal) {
        this.mContext = mContext;
        this.mData = mData;
        this.textViewTotal = textViewTotal;

    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cart_item_list, parent, false);


        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {

        int adapterPosition = holder.getAdapterPosition();
        holder.itemNameTextView.setText(mData.get(adapterPosition).getItemName());
        holder.itemPriceTextView.setText(mData.get(adapterPosition).getItemPrice());
        holder.itemDescriptionTextView.setText(mData.get(adapterPosition).getItemDescription());
        holder.itemQuantityTextView.setText(mData.get(adapterPosition).getItemQuantity());
        double totalPrice = Double.parseDouble(mData.get(adapterPosition).getItemTotalPrice());
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        holder.itemTotalPriceTextView.setText(formattedTotalPrice);
        //overallTotalPrice += Double.parseDouble(mData.get(adapterPosition).getItemTotalPrice());
        overallTotalPrice += Double.parseDouble(mData.get(adapterPosition).getItemTotalPrice());

        byte[] bytes = Base64.decode(mData.get(position).getItemImage(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.itemImage.setImageBitmap(decodedBitmap);


        /*
        String formattedTotalOverallPrice = String.format("%.2f", overallTotalPrice);
        textViewTotal.setText(String.format("Total: %.2f", formattedTotalOverallPrice));
        holder.textViewTotal.setText(formattedTotalOverallPrice);
         */
        holder.itemPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = mData.get(adapterPosition).getId();
                int currentQuantity = Integer.parseInt(mData.get(adapterPosition).getItemQuantity());
                mData.get(adapterPosition).setItemQuantity(String.valueOf(currentQuantity + 1));
                ConnectionClass connectionClass = new ConnectionClass();
                connectionClass.updateItemQuantity(id, currentQuantity + 1);

                // update the item total price
                double itemPrice = Double.parseDouble(mData.get(adapterPosition).getItemPrice());
                double totalPrice = itemPrice * (currentQuantity + 1);
                mData.get(adapterPosition).setItemTotalPrice(String.format("%.2f", totalPrice));

                // update the overall total price
                overallTotalPrice = 0;
                for (CartItem item : mData) {
                    overallTotalPrice += Double.parseDouble(item.getItemTotalPrice());
                }

                // update the TextView
                textViewTotal.setText(String.format("Total: %.2f", overallTotalPrice));
                notifyDataSetChanged();
            }
        });
        holder.itemMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(mData.get(adapterPosition).getItemQuantity());
                if (currentQuantity > 1) {
                    String id = mData.get(adapterPosition).getId();
                    mData.get(adapterPosition).setItemQuantity(String.valueOf(currentQuantity - 1));
                    ConnectionClass connectionClass = new ConnectionClass();
                    connectionClass.updateItemQuantity(id, currentQuantity - 1);
                    // update the item total price
                    double itemPrice = Double.parseDouble(mData.get(adapterPosition).getItemPrice());
                    double totalPrice = itemPrice * (currentQuantity - 1);
                    mData.get(adapterPosition).setItemTotalPrice(String.format("%.2f", totalPrice));

                    // update the overall total price
                    overallTotalPrice = 0;
                    for (CartItem item : mData) {
                        overallTotalPrice += Double.parseDouble(item.getItemTotalPrice());
                    }

                    // update the TextView
                    textViewTotal.setText(String.format("Total: %.2f", overallTotalPrice));

                    //updateOverallTotalPrice();
                    notifyDataSetChanged();

                } else {
                    Toast.makeText(mContext, "Item quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                // remove the item from the mData list
                String id = mData.get(adapterPosition).getId();
                mData.remove(adapterPosition);
                // delete the item from the database
                ConnectionClass connectionClass = new ConnectionClass();
                connectionClass.delete(id);
                Toast.makeText(mContext, "Item has been removed", Toast.LENGTH_SHORT).show();

                // notify the adapter that the data has changed
                notifyDataSetChanged();
            }
        });

        holder.itemQuantityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do something before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do something when the text is changed
                // update the item quantity in the mData list
                mData.get(adapterPosition).setItemQuantity(charSequence.toString());

                // update the item price in the mData list
                int quantity = Integer.parseInt(charSequence.toString());
                double price = Double.parseDouble(mData.get(adapterPosition).getItemPrice());
                double totalPrice = price * quantity;
                //String totalPriceString = Double.toString(totalPrice);
                String totalPriceString = String.format("%.2f", totalPrice);

                mData.get(adapterPosition).setItemTotalPrice(totalPriceString);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do something after the text is changed
            }
        });

    }

    public void updateOverallTotalPrice() {
        // update the overall total price
        double overallTotalPrice = 0;
        for (CartItem item : mData) {
            overallTotalPrice += Double.parseDouble(item.getItemTotalPrice());
        }
        // update the total price text view

        textViewTotal.setText(String.format("Total: %.2f", overallTotalPrice));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyView extends RecyclerView.ViewHolder {

        TextView itemNameTextView;
        TextView itemPriceTextView;
        TextView itemDescriptionTextView;
        TextView itemQuantityTextView;
        TextView itemTotalPriceTextView;
        Button itemPlusButton;
        Button itemMinusButton;
        ImageView itemDelete;
        ImageView itemImage;
        EditText itemQuantityEditText;
        //TextView textViewTotal;


        public MyView(View view) {
            super(view);

            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemPriceTextView = itemView.findViewById(R.id.item_price);
            itemTotalPriceTextView = itemView.findViewById(R.id.item_total_price);
            itemDescriptionTextView = itemView.findViewById(R.id.item_description);
            itemQuantityTextView = itemView.findViewById(R.id.item_quantity);
            itemPlusButton = itemView.findViewById(R.id.item_plus);
            itemMinusButton = itemView.findViewById(R.id.item_minus);
            itemQuantityEditText = itemView.findViewById(R.id.item_quantity_edit);
            itemDelete = itemView.findViewById(R.id.item_delete);
            itemImage = itemView.findViewById(R.id.thumbnail);
            //textViewTotal = itemView.findViewById(R.id.textViewTotal);


        }
    }
}
