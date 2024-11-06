package cf20.soft.shoes.timmi.admin.ui.order;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.databinding.ItemBillAdminBinding;
import cf20.soft.shoes.timmi.model.Bill;
import cf20.soft.shoes.timmi.model.Order;
import cf20.soft.shoes.timmi.model.Product;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {

    private List<Bill> bills;

    public BillAdapter(List<Bill> bills) {
        this.bills = bills;
    }

    public void setData(List<Bill> bills) {
        this.bills = bills;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bill_admin, parent, false);
        ItemBillAdminBinding binding = ItemBillAdminBinding.bind(view);
        return new BillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        if (position < bills.size()) {
            holder.bind(bills.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (bills != null) return bills.size();
        return 0;
    }
}

class BillViewHolder extends RecyclerView.ViewHolder {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ItemBillAdminBinding binding;

    public BillViewHolder(ItemBillAdminBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Bill bill) {
        if (!TextUtils.isEmpty(bill.getUserFullName())) {
            binding.tvName.setText(bill.getUserFullName());
        } else {
            db.collection("users")
                    .document(bill.getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        bill.setUserFullName(task.getResult().get("name").toString());
                        binding.tvName.setText(bill.getUserFullName());
                    })
                    .addOnFailureListener(e -> {
                        binding.tvName.setText(bill.getUserId());
                    });
        }

        binding.tvAddress.setText(bill.getAddress());
        binding.tvPhone.setText(bill.getPhone());
        binding.tvTotal.setText(bill.getTotal() + "đ");

        for (Product product : CommonState.getCacheProducts()) {
            for (Order order : bill.getProduct()) {
                if (Objects.equals(order.getProductId(), product.getId())) {
                    order.setProduct(product);
                }
            }
        }

        OrderAdapter orderAdapter = new OrderAdapter(bill.getProduct());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(orderAdapter);
    }
}
