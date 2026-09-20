package com.example.coffeemanager.ui;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.coffeemanager.data.Drink;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    public interface OnDrinkClickListener {
        void onDrinkClick(Drink drink);
    }

    private List<Drink> drinks = new ArrayList<>();
    private final OnDrinkClickListener listener;
    private int selectedPosition = -1;

    public DrinkAdapter(OnDrinkClickListener listener) {
        this.listener = listener;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinks.get(position);
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        holder.tvName.setText(drink.getName());
        holder.tvCategory.setText(drink.getCategory());
        holder.tvPrice.setText(formatter.format(drink.getPrice()) + " đ");

        if (drink.isAvailable()) {
            holder.tvStatus.setText("✓ Có");
            holder.tvStatus.setTextColor(Color.parseColor("#00897B"));
        } else {
            holder.tvStatus.setText("✗ Hết");
            holder.tvStatus.setTextColor(Color.parseColor("#E53935"));
        }

        if (drink.getImageUri() != null && !drink.getImageUri().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(drink.getImageUri()))
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.card.setStrokeWidth(position == selectedPosition ? 3 : 0);

        holder.card.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (prev >= 0) {
                notifyItemChanged(prev);
            }
            notifyItemChanged(selectedPosition);
            listener.onDrinkClick(drink);
        });
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    static class DrinkViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView ivImage;
        TextView tvName;
        TextView tvCategory;
        TextView tvPrice;
        TextView tvStatus;

        DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_drink);
            ivImage = itemView.findViewById(R.id.iv_item_image);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvCategory = itemView.findViewById(R.id.tv_item_category);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
        }
    }
}
