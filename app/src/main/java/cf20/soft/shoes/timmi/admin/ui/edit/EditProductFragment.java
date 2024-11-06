package cf20.soft.shoes.timmi.admin.ui.edit;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;

import cf20.soft.shoes.timmi.ImageUtil;
import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.admin.AdminActivity;
import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.admin.ui.create.CreateProductViewModel;
import cf20.soft.shoes.timmi.databinding.FragmentCreateProductBinding;
import cf20.soft.shoes.timmi.databinding.FragmentEditProductBinding;
import cf20.soft.shoes.timmi.model.Product;

public class EditProductFragment extends Fragment {
    private FragmentEditProductBinding binding;
    private CreateProductViewModel viewModel;

    int PICK_IMAGE_REQUEST = 1;

    private Bitmap imageBitmap;
    private Product product = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CreateProductViewModel.class);

        binding = FragmentEditProductBinding.inflate(inflater, container, false);

        viewModel.getUpdateFlow().observe(getViewLifecycleOwner(), isSuccess -> {
            binding.layoutLoading.setVisibility(View.GONE);
            if (isSuccess) {
                Toast.makeText(requireContext(), "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                ((AdminActivity) requireActivity()).showNavBottom();
                ((AdminActivity) requireActivity()).getNavController().popBackStack();
                CommonState.postValueRefresh(true);
            } else {
                Toast.makeText(requireContext(), "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
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

        if (product.getImage() == null) {
            binding.ivImage.setImageResource(R.drawable.placehoder);
        } else {
            StringBuilder ima = new StringBuilder();
            for (String s : product.getImage()) {
                ima.append(s);
            }
            if (!ima.toString().isEmpty()) {
                imageBitmap = ImageUtil.decode(ima.toString());
                binding.ivImage.setImageBitmap(imageBitmap);
            }
        }
        binding.edtNameProduct.setText(product.getName());
        binding.edtColorProduct.setText(product.getColorsV2());
        binding.edtSizeProduct.setText(product.getSizesV2());
        binding.edtSaleProduct.setText(String.valueOf(product.getSale()));
        binding.edtPriceProduct.setText(String.valueOf(product.getPrice()));
        binding.edtQualityProduct.setText(String.valueOf(product.getQuality()));

        binding.tvCancel.setOnClickListener(v -> {
            ((AdminActivity) requireActivity()).showNavBottom();
            ((AdminActivity) requireActivity()).getNavController().popBackStack();
        });
        binding.ivImage.setOnClickListener(v -> {
            openFileChooser();
        });

        binding.tvAddProduct.setOnClickListener(v -> {
            String name = binding.edtNameProduct.getText().toString().trim();
            ArrayList<String> image = new ArrayList<>();
            String colors = binding.edtColorProduct.getText().toString().trim();
            String sizes = binding.edtSizeProduct.getText().toString().trim();
            String price = binding.edtPriceProduct.getText().toString().trim();
            String sale = binding.edtSaleProduct.getText().toString().trim();
            int saleNumber = 0;
            if (!TextUtils.isEmpty(sale)) {
                saleNumber = Integer.parseInt(sale);
            }
            String quality = binding.edtQualityProduct.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(colors) || TextUtils.isEmpty(sizes) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quality)) {
                Toast.makeText(requireContext(), "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] c = colors.split(",");
            ArrayList<String> cl = new ArrayList<>();
            Collections.addAll(cl, c);

            boolean isSizeValid = true;
            String[] s = sizes.split(",");
            ArrayList<String> sz = new ArrayList<>();
            for (String itemSize : s) {
                try {
                    Integer.parseInt(itemSize.trim());
                    sz.add(itemSize);
                } catch (Exception exception) {
                    isSizeValid = false;
                }
            }

            if (imageBitmap != null) {
                String base64 = ImageUtil.convert(imageBitmap);
                int length = base64.length();

                image.add(base64.substring(0, length / 2));
                image.add(base64.substring(length / 2));
            }

            if (!isSizeValid) {
                Toast.makeText(requireContext(), "Kích thước phải là số và cách nhau bởi dấu ,", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.layoutLoading.setVisibility(View.VISIBLE);
            viewModel.editProduct(new Product(product.getId(), name, image, cl, sz, Long.parseLong(price), saleNumber, Integer.parseInt(quality)));
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), data.getData());
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 500, 500, false);
                binding.ivImage.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
