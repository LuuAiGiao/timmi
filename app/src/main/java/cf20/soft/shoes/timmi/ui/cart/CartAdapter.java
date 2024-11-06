package cf20.soft.shoes.timmi.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cf20.soft.shoes.timmi.ImageUtil;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.databinding.ItemOrderInCartBinding;
import cf20.soft.shoes.timmi.model.Order;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> orders;
    private final OnDeleteItemOrderCartListener listener;

    public CartAdapter(List<Order> orders, OnDeleteItemOrderCartListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    public void setPreOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order_in_cart, parent, false);
        ItemOrderInCartBinding binding = ItemOrderInCartBinding.bind(view);
        return new CartViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (position < orders.size()) {
            holder.bind(orders.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (orders != null) return orders.size();
        return 0;
    }
}

class CartViewHolder extends RecyclerView.ViewHolder {

    private final ItemOrderInCartBinding binding;
    private final OnDeleteItemOrderCartListener listener;

    public CartViewHolder(ItemOrderInCartBinding binding, OnDeleteItemOrderCartListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(Order order) {
        // Bind dữ liệu sản phẩm vào các thành phần giao diện
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
        if (order.getProduct().getSale() == 0) {
            binding.tvPrice.setText("Giá: " + order.getProduct().getPrice() + "đ");
        } else {
            long price = order.getProduct().getPrice() - order.getProduct().getPrice() * order.getProduct().getSale() / 100;
            binding.tvPrice.setText("Giá: " + price + "đ");
        }

        binding.tvQuality.setText("Số lượng: 1");

        binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteItemOrderCart(order);
            }
        });
    }
}
