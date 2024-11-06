package cf20.soft.shoes.timmi.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cf20.soft.shoes.timmi.MainActivity;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.AdminActivity;
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvRegister.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).getNavController().navigate(R.id.action_navigation_login_to_navigation_register);
        });
        binding.tvAdminTools.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AdminActivity.class));
        });

        viewModel.getLoginFlow().observe(getViewLifecycleOwner(), user -> {
            binding.layoutLoading.setVisibility(View.GONE);
            if (user != null) {
                Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                CommonState.setUser(user);
                ((MainActivity) requireActivity()).getNavController().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        binding.tvLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Email không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmailAddress(email)) {
                Toast.makeText(requireContext(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                return;
            }
            String password = binding.edtPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.layoutLoading.setVisibility(View.VISIBLE);
            viewModel.login(email, password);
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
