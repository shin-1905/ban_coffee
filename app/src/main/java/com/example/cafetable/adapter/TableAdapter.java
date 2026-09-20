package com.example.cafetable.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.databinding.ItemCafeTableBinding;
import com.example.cafetable.model.CafeTable;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    public interface OnTableActionListener {
        void onTableClick(CafeTable cafeTable);

        void onTableLongClick(CafeTable cafeTable, View anchor);
    }

    private final List<CafeTable> tableList = new ArrayList<>();
    private final OnTableActionListener listener;

    public TableAdapter(OnTableActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CafeTable> newList) {
        tableList.clear();
        tableList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCafeTableBinding binding = ItemCafeTableBinding.inflate(inflater, parent, false);
        return new TableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        holder.bind(tableList.get(position));
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder {
        private final ItemCafeTableBinding binding;

        TableViewHolder(ItemCafeTableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CafeTable cafeTable) {
            binding.tvTableName.setText(cafeTable.getName());
            binding.tvSeats.setText(cafeTable.getSeats() + " cho");
            binding.tvStatus.setText(cafeTable.getStatus());

            if ("Trong".equalsIgnoreCase(cafeTable.getStatus())) {
                binding.cardTable.setCardBackgroundColor(Color.parseColor("#C8F7C5"));
            } else if ("Dang phuc vu".equalsIgnoreCase(cafeTable.getStatus())) {
                binding.cardTable.setCardBackgroundColor(Color.parseColor("#FFD3B6"));
            } else {
                binding.cardTable.setCardBackgroundColor(Color.parseColor("#FFEAA7"));
            }

            binding.getRoot().setOnClickListener(v -> listener.onTableClick(cafeTable));
            binding.getRoot().setOnLongClickListener(v -> {
                listener.onTableLongClick(cafeTable, binding.getRoot());
                return true;
            });
        }
    }
}
