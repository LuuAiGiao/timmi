package cf20.soft.shoes.timmi.ui.home;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cf20.soft.shoes.timmi.ImageUtil;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.ui.product.OnClickProductListener;
import cf20.soft.shoes.timmi.databinding.ItemProductBinding;
import cf20.soft.shoes.timmi.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<Product> products;
    private OnClickProductListener listener;

    public ProductAdapter(List<Product> products, OnClickProductListener listener) {
        this.products = products;
        this.listener = listener;
    }

    public void updateData(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        ItemProductBinding binding = ItemProductBinding.bind(view);
        return new ProductViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
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

class ProductViewHolder extends RecyclerView.ViewHolder {

    private final ItemProductBinding binding;
    private OnClickProductListener listener;

    public ProductViewHolder(ItemProductBinding binding, OnClickProductListener listener) {
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
        binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (product.getSale() > 0) {
            binding.tvSales.setVisibility(View.VISIBLE);
            binding.tvSales.setText("Giảm " + product.getSale() + " %");

            binding.tvPrice.setVisibility(View.VISIBLE);
            binding.tvPrice.setText(product.getPrice() + "đ");
            long newPrice = product.getPrice() - (product.getPrice() * product.getSale() / 100);
            binding.tvPriceAfterSale.setText(newPrice + "đ");
        } else {
            binding.tvPrice.setVisibility(View.GONE);
            binding.tvPriceAfterSale.setText(product.getPrice() + "đ");

            binding.tvSales.setVisibility(View.GONE);
        }

        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickItem(product);
            }
        });

//        binding.tvQuality.setText("Số lượng: " + product.getQuality() + " chiếc");
//
//        binding.btnEdit.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onEditItem(product);
//            }
//        });
//        binding.btnDelete.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onDeleteItem(product);
//            }
//        });
    }
}

