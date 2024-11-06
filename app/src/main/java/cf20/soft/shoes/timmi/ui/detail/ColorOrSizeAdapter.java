package cf20.soft.shoes.timmi.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.databinding.ItemColorOrSizeBinding;
import cf20.soft.shoes.timmi.model.ColorOrSize;

public class ColorOrSizeAdapter extends RecyclerView.Adapter<ColorOrSizeViewHolder> {

    private final List<ColorOrSize> items;
    private OnClickItemListener listener;

    public ColorOrSizeAdapter(List<ColorOrSize> items, OnClickItemListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorOrSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_color_or_size, parent, false);
        ItemColorOrSizeBinding binding = ItemColorOrSizeBinding.bind(view);
        return new ColorOrSizeViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorOrSizeViewHolder holder, int position) {
        if (position < items.size()) {
            holder.bind(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }
}

class ColorOrSizeViewHolder extends RecyclerView.ViewHolder {

    private final ItemColorOrSizeBinding binding;
    private final OnClickItemListener listener;

    public ColorOrSizeViewHolder(ItemColorOrSizeBinding binding, OnClickItemListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(ColorOrSize item) {
        binding.tvData.setText(item.getValue());
        if (item.isSelected()) {
            binding.tvData.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
            binding.container.setCardBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.black));
        } else {
            binding.tvData.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.black));
            binding.container.setCardBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
        }

        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelected(item);
            }
        });
    }
}
