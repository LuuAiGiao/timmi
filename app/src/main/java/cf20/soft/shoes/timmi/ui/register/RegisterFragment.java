package cf20.soft.shoes.timmi.ui.register;

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
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.databinding.FragmentLoginBinding;
import cf20.soft.shoes.timmi.databinding.FragmentRegisterBinding;
import cf20.soft.shoes.timmi.ui.login.LoginViewModel;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getUserExistsFlow().observe(getViewLifecycleOwner(), userExists -> {
            if (userExists) {
                binding.layoutLoading.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getCreateFlow().observe(getViewLifecycleOwner(), user -> {
            binding.layoutLoading.setVisibility(View.GONE);
            if (user != null) {
                // CommonState.setUser(user);
                Toast.makeText(requireContext(), "Đăng ký thành công! Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).getNavController().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        binding.tvRegister.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Email không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmailAddress(email)) {
                Toast.makeText(requireContext(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = binding.edtName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            String password = binding.edtPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            String passwordConfirm = binding.edtPasswordConfirm.getText().toString();
            if (TextUtils.isEmpty(passwordConfirm)) {
                Toast.makeText(requireContext(), "Xác nhận mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.equals(passwordConfirm)) {
                binding.layoutLoading.setVisibility(View.VISIBLE);
                viewModel.register(email, name, password);
            } else {
                Toast.makeText(requireContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            }
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
