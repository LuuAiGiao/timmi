package cf20.soft.shoes.timmi.admin.ui.product;

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

import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.AdminActivity;
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.databinding.FragmentAdminProductBinding;
import cf20.soft.shoes.timmi.model.Product;

public class AdminProductFragment extends Fragment {
    private FragmentAdminProductBinding binding;
    private AdminProductViewModel viewModel;
    private AdminProductAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AdminProductViewModel.class);
        binding = FragmentAdminProductBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AdminProductAdapter(new ArrayList<>(), new OnClickProductListener() {
            @Override
            public void onEditItem(Product product) {
                ((AdminActivity) requireActivity()).hideNavBottom();

                Bundle bundle = new Bundle();
                bundle.putSerializable("item_product", product);
                ((AdminActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_admin_product_to_navigation_edit_product, bundle);
            }

            @Override
            public void onDeleteItem(Product product) {
                viewModel.deleteItem(product);
            }
        });

        binding.recyclerView.setAdapter(adapter);

        viewModel.getProductsFlow().observe(getViewLifecycleOwner(), list -> {
            CommonState.setCacheProducts(list);
            adapter.updateData(list);
        });
        viewModel.getDeleteFlow().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess == null) return;
            if (isSuccess) {
                Toast.makeText(requireContext(), "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                viewModel.getData();
            } else {
                Toast.makeText(requireContext(), "Xoá sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
            viewModel.clearDeleteFlow();
        });

        CommonState.getRefreshFlow().observe(getViewLifecycleOwner(), isRefresh -> {
            if (isRefresh) {
                viewModel.getData();
            }
        });

        binding.tvCreateProduct.setOnClickListener(v -> {
            ((AdminActivity) requireActivity()).hideNavBottom();
            ((AdminActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_admin_product_to_navigation_create_product);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
