package cf20.soft.shoes.timmi.admin.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cf20.soft.shoes.timmi.ImageUtil;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.databinding.ItemProductAdminBinding;
import cf20.soft.shoes.timmi.model.Product;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductViewHolder> {

    private List<Product> products;
    private OnClickProductListener listener;

    public AdminProductAdapter(List<Product> products, OnClickProductListener listener) {
        this.products = products;
        this.listener = listener;
    }

    public void updateData(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product_admin, parent, false);
        ItemProductAdminBinding binding = ItemProductAdminBinding.bind(view);
        return new AdminProductViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        if (position < products.size()) {
            holder.bind(products.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (products == null) return 0;
        return products.size();
    }

}

class AdminProductViewHolder extends RecyclerView.ViewHolder {

    private final ItemProductAdminBinding binding;
    private OnClickProductListener listener;

    public AdminProductViewHolder(ItemProductAdminBinding binding, OnClickProductListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(Product product) {
        // Bind dữ liệu sản phẩm vào các thành phần giao diện
        if (product.getImage() == null) {
            binding.ivImage.setImageResource(R.drawable.placehoder);
        } else {
            StringBuilder ima = new StringBuilder();
            for (String s : product.getImage()) {
                ima.append(s);
            }
            if (ima.toString().isEmpty()) {
                binding.ivImage.setImageResource(R.drawable.placehoder);
            } else {
                binding.ivImage.setImageBitmap(ImageUtil.decode(ima.toString()));
            }
        }
        binding.tvName.setText(product.getName());
        binding.tvColors.setText("Màu sắc: " + product.getColorsV2());
        binding.tvSizes.setText("Kích thước: " + product.getSizesV2());
        binding.tvSales.setText("Sale: " + product.getSale() + " %");
        binding.tvPrice.setText("Giá: " + product.getPrice() + " VND");
        binding.tvQuality.setText("Số lượng: " + product.getQuality() + " chiếc");

        binding.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditItem(product);
            }
        });
        binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteItem(product);
            }
        });
    }
}
