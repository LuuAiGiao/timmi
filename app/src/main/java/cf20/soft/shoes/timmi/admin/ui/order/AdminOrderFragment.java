package cf20.soft.shoes.timmi.admin.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import cf20.soft.shoes.timmi.databinding.FragmentAdminOrderBinding;

public class AdminOrderFragment extends Fragment {

    private FragmentAdminOrderBinding binding;
    private AdminOrderViewModel viewModel;

    private BillAdapter billAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AdminOrderViewModel.class);

        binding = FragmentAdminOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        billAdapter = new BillAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(billAdapter);
        viewModel.getBillsFlow().observe(getViewLifecycleOwner(), s -> {
            billAdapter.setData(s);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
