package cf20.soft.shoes.timmi.admin.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cf20.soft.shoes.timmi.ImageUtil;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.databinding.ItemOrderInCartV2Binding;
import cf20.soft.shoes.timmi.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private List<Order> products;

    public OrderAdapter(List<Order> products) {
        this.products = products;
    }

    public void setData(List<Order> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order_in_cart_v2, parent, false);
        ItemOrderInCartV2Binding binding = ItemOrderInCartV2Binding.bind(view);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (position < products.size()) {
            holder.bind(products.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (products != null) return products.size();
        return 0;
    }
}

class OrderViewHolder extends RecyclerView.ViewHolder {
    private ItemOrderInCartV2Binding binding;

    public OrderViewHolder(ItemOrderInCartV2Binding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Order order) {
        if (order.getProduct().getImage() == null) {
            binding.ivImage.setImageResource(R.drawable.placehoder);
        } else {
            StringBuilder ima = new StringBuilder();
            for (String s : order.getProduct().getImage()) {
                ima.append(s);
            }
            if (ima.toString().isEmpty()) {
                binding.ivImage.setImageResource(R.drawable.placehoder);
            } else {
                binding.ivImage.setImageBitmap(ImageUtil.decode(ima.toString()));
            }
        }
        binding.tvName.setText(order.getProduct().getName());
        binding.tvPhanLoai.setText("Phân loại: " + order.getColor() + ", " + order.getSize());
    }
}