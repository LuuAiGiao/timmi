package cf20.soft.shoes.timmi.ui.cart;

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

import cf20.soft.shoes.timmi.MainActivity;
import cf20.soft.shoes.timmi.databinding.FragmentCartBinding;
import cf20.soft.shoes.timmi.model.Order;
import cf20.soft.shoes.timmi.model.PreOrder;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;

    private CartViewModel viewModel;
    private CartAdapter adapter;

    private PreOrder preOrder;
    private long total;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getPreOrderFlow().observe(getViewLifecycleOwner(), preOrder -> {
            this.preOrder = preOrder;
            total = totalMoney();
            adapter.setPreOrders(preOrder.getOrders());
        });
        viewModel.getDeleteFlow().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess == null) return;
            binding.layoutLoading.setVisibility(View.GONE);
            if (isSuccess) {
                Toast.makeText(requireContext(), "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                viewModel.getCart();
            } else {
                Toast.makeText(requireContext(), "Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
            viewModel.clearDeleteFlow();
        });
        viewModel.getCreateFlow().observe(getViewLifecycleOwner(), isSuccess -> {
            binding.layoutLoading.setVisibility(View.GONE);
            if (isSuccess) {
                Toast.makeText(requireContext(), "Tạo đơn hàng thành công", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).getNavController().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Tạo đơn hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CartAdapter(new ArrayList<>(), order -> {
            binding.layoutLoading.setVisibility(View.VISIBLE);
            viewModel.deleteOrder(order);
        });

        total = totalMoney();
        binding.recyclerView.setAdapter(adapter);

        binding.btnAddCart.setOnClickListener(v -> {
            if (preOrder.getOrders().isEmpty()) return;

            if (binding.edtAddress.getText().toString().isEmpty() || binding.edtPhone.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Yêu cầu thông tin nhận hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            String address = binding.edtAddress.getText().toString().trim();
            String phone = binding.edtPhone.getText().toString().trim();

            binding.layoutLoading.setVisibility(View.VISIBLE);
            viewModel.addBill(preOrder, address, phone, total);
        });
    }

    private long totalMoney() {
        long total = 0;
        if (preOrder == null) return 0;
        if (!preOrder.getOrders().isEmpty()) {
            for (Order order : preOrder.getOrders()) {
                if (order.getProduct() == null) continue;
                if (order.getProduct().getSale() == 0) {
                    total += order.getProduct().getPrice();
                } else {
                    long price = order.getProduct().getPrice() - order.getProduct().getPrice() * order.getProduct().getSale() / 100;
                    total += price;
                }
            }
        }
        binding.tvTotal.setText(total + "đ");
        return total;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
