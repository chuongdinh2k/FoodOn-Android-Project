package com.example.btl_fooddelivery.CustomerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_fooddelivery.R;

import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {
    private Context context;
    private List<CustomerPendingOrders> customerPendingOrderslist;

    public PendingOrdersAdapter(Context context, List<CustomerPendingOrders> customerPendingOrderslist) {
        this.context = context;
        this.customerPendingOrderslist = customerPendingOrderslist;
    }

    @NonNull
    @Override
    public PendingOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order_dishes, parent, false);
        return new PendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrdersAdapter.ViewHolder holder, int position) {
        final CustomerPendingOrders customerPendingOrders = customerPendingOrderslist.get(position);
        holder.Dishname.setText(customerPendingOrders.getDishName());
        holder.Price.setText("Giá: vnd " + customerPendingOrders.getPrice());
        holder.Quantity.setText("× " + customerPendingOrders.getDishQuantity());
        holder.Totalprice.setText("Tổng: vnd " + customerPendingOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return customerPendingOrderslist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Dishname, Price, Quantity, Totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Dishname = itemView.findViewById(R.id.Dishh);
            Price = itemView.findViewById(R.id.pricee);
            Quantity = itemView.findViewById(R.id.qtyy);
            Totalprice = itemView.findViewById(R.id.total);

        }
    }
}
