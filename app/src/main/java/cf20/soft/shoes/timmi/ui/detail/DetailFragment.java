package cf20.soft.shoes.timmi.ui.detail;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import cf20.soft.shoes.timmi.ImageUtil;
import cf20.soft.shoes.timmi.MainActivity;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.AdminActivity;
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.databinding.FragmentDetailBinding;
import cf20.soft.shoes.timmi.model.ColorOrSize;
import cf20.soft.shoes.timmi.model.Order;
import cf20.soft.shoes.timmi.model.Product;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private DetailViewModel viewModel;

    private Product product = null;

    private ColorOrSizeAdapter colorAdapter;
    private ColorOrSizeAdapter sizeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                product = getArguments().getSerializable("item_product", Product.class);
            } else {
                product = (Product) getArguments().getSerializable("item_product");
            }
        }
        if (product == null) {
            ((AdminActivity) requireActivity()).showNavBottom();
            ((AdminActivity) requireActivity()).getNavController().popBackStack();
            return;
        }
        viewModel.getAdCartFlow().observe(getViewLifecycleOwner(), success -> {
            binding.layoutLoading.setVisibility(View.GONE);
            if (success) {
                Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).getNavController().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Đã có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

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

        binding.recyclerViewColors.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        colors.clear();
        for (String color : product.getColors()) {
            colors.add(new ColorOrSize(color, false));
        }
        colorAdapter = new ColorOrSizeAdapter(colors, color -> {
            for (ColorOrSize c0 : colors) {
                c0.setSelected(c0.getValue().equals(color.getValue()));
            }
            colorAdapter.notifyDataSetChanged();
        });
        binding.recyclerViewColors.setAdapter(colorAdapter);

        binding.recyclerViewSizes.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        sizes.clear();
        for (String size : product.getSizes()) {
            sizes.add(new ColorOrSize(size, false));
        }
        sizeAdapter = new ColorOrSizeAdapter(sizes, size -> {
            for (ColorOrSize z0 : sizes) {
                z0.setSelected(z0.getValue().equals(size.getValue()));
            }
            sizeAdapter.notifyDataSetChanged();
        });
        binding.recyclerViewSizes.setAdapter(sizeAdapter);
        binding.tvQuality.setText("Số lượng: " + product.getQuality() + " chiếc");

        binding.btnAddCart.setOnClickListener(v -> {
            if (CommonState.getUser() == null) {
                ((MainActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_detail_to_navigation_login);
            } else {
                ColorOrSize colorSelected = null;
                for (ColorOrSize color : colors) {
                    if (color.isSelected()) {
                        colorSelected = color;
                    }
                }
                if (colorSelected == null) {
                    Toast.makeText(requireContext(), "Chưa chọn màu sắc", Toast.LENGTH_SHORT).show();
                    return;
                }
                ColorOrSize sizeSelected = null;
                for (ColorOrSize size : sizes) {
                    if (size.isSelected()) {
                        sizeSelected = size;
                    }
                }
                if (sizeSelected == null) {
                    Toast.makeText(requireContext(), "Chưa chọn kích cỡ", Toast.LENGTH_SHORT).show();
                    return;
                }
//                CommonState.addCart();
//                Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                ((MainActivity) requireActivity()).getNavController().popBackStack();

                binding.layoutLoading.setVisibility(View.VISIBLE);
                viewModel.addCart(new Order(product, product.getId(), colorSelected.getValue(), sizeSelected.getValue()));
            }
        });
    }

    private final ArrayList<ColorOrSize> colors = new ArrayList<>();
    private final ArrayList<ColorOrSize> sizes = new ArrayList<>();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
