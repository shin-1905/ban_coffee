package com.example.coffeemanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.coffeemanager.data.Invoice;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Invoice invoice);
    }

    private final NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private final List<Invoice> invoices = new ArrayList<>();
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setInvoices(List<Invoice> invoiceList) {
        invoices.clear();
        invoices.addAll(invoiceList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        holder.tvId.setText("Hoa don #" + invoice.getId());
        holder.tvDate.setText("Ngay: " + invoice.getDate());
        holder.tvTotal.setText("Tong tien: " + formatter.format(invoice.getTotal()) + " đ");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(invoice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView tvDate;
        private final TextView tvTotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_item_invoice_id);
            tvDate = itemView.findViewById(R.id.tv_item_invoice_date);
            tvTotal = itemView.findViewById(R.id.tv_item_invoice_total);
        }
    }
}
