package cf20.soft.shoes.timmi.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import cf20.soft.shoes.timmi.MainActivity;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.AdminActivity;
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.admin.ui.product.AdminProductAdapter;
import cf20.soft.shoes.timmi.admin.ui.product.OnClickProductListener;
import cf20.soft.shoes.timmi.databinding.FragmentHomeBinding;
import cf20.soft.shoes.timmi.model.Product;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private ProductAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        adapter = new ProductAdapter(new ArrayList<>(), new OnClickProductListener() {
            @Override
            public void onClickItem(Product product) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item_product", product);
                ((MainActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
            }

            @Override
            public void onEditItem(Product product) {
            }

            @Override
            public void onDeleteItem(Product product) {
            }
        });
        binding.recyclerView.setAdapter(adapter);

        binding.ivCart.setOnClickListener(v -> {
            if (CommonState.getUser() != null) {
                ((MainActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_home_to_navigation_cart);
            } else {
                ((MainActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_home_to_navigation_login);
            }
        });
        viewModel.getProductsFlow().observe(getViewLifecycleOwner(), list -> {
            CommonState.setCacheProducts(list);
            adapter.updateData(list);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}