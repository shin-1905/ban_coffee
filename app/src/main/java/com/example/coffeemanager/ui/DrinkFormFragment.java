package com.example.coffeemanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.btl.databinding.FragmentDrinkFormBinding;
import com.example.coffeemanager.data.Drink;
import com.example.coffeemanager.viewmodel.DrinkViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;

public class DrinkFormFragment extends Fragment {

    private static final String ARG_DRINK_ID = "drink_id";

    private FragmentDrinkFormBinding binding;
    private DrinkViewModel viewModel;
    private int editDrinkId = -1;
    private String selectedImageUri = "";

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        getContext().getContentResolver().takePersistableUriPermission(uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        selectedImageUri = uri.toString();
                        Glide.with(this).load(uri).into(binding.ivFormImage);
                    }
                }
            }
    );

    public static DrinkFormFragment newInstance(int drinkId) {
        DrinkFormFragment fragment = new DrinkFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DRINK_ID, drinkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            editDrinkId = getArguments().getInt(ARG_DRINK_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDrinkFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(DrinkViewModel.class);

        setupCategoryDropdown();

        if (editDrinkId != -1) {
            binding.tvFormTitle.setText("Chỉnh sửa đồ uống");
            Executors.newSingleThreadExecutor().execute(() -> {
                Drink drink = viewModel.getRepository().getDrinkById(editDrinkId);
                if (drink != null && getActivity() != null) {
                    requireActivity().runOnUiThread(() -> populateForm(drink));
                }
            });
        } else {
            binding.tvFormTitle.setText("Thêm đồ uống mới");
        }

        binding.fabSelectImage.setOnClickListener(v -> openImagePicker());
        binding.btnSave.setOnClickListener(v -> saveDrink());
        binding.btnCancel.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void setupCategoryDropdown() {
        String[] categories = {"Cà phê", "Trà", "Sinh tố", "Nước ép", "Đồ uống khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, categories);
        binding.spinnerCategory.setAdapter(adapter);
    }

    private void populateForm(Drink drink) {
        binding.etName.setText(drink.getName());
        binding.spinnerCategory.setText(drink.getCategory(), false);
        binding.etPrice.setText(String.valueOf(drink.getPrice()));
        binding.etDescription.setText(drink.getDescription());
        binding.switchAvailable.setChecked(drink.isAvailable());
        selectedImageUri = drink.getImageUri();

        if (selectedImageUri != null && !selectedImageUri.isEmpty()) {
            Glide.with(this).load(Uri.parse(selectedImageUri)).into(binding.ivFormImage);
        }
    }

    private void saveDrink() {
        String name = binding.etName.getText() != null
                ? binding.etName.getText().toString().trim() : "";
        String category = binding.spinnerCategory.getText() != null
                ? binding.spinnerCategory.getText().toString().trim() : "";
        String priceStr = binding.etPrice.getText() != null
                ? binding.etPrice.getText().toString().trim() : "";

        if (name.isEmpty()) {
            binding.tilName.setError("Vui lòng nhập tên đồ uống");
            return;
        }
        binding.tilName.setError(null);

        if (category.isEmpty()) {
            binding.tilCategory.setError("Vui lòng chọn danh mục");
            return;
        }
        binding.tilCategory.setError(null);

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            binding.tilPrice.setError("Giá không hợp lệ");
            return;
        }
        binding.tilPrice.setError(null);

        String description = binding.etDescription.getText() != null
                ? binding.etDescription.getText().toString().trim() : "";
        boolean available = binding.switchAvailable.isChecked();

        Drink drink = new Drink(name, category, price, description, available, selectedImageUri);

        if (editDrinkId != -1) {
            drink.setId(editDrinkId);
            viewModel.update(drink);
        } else {
            viewModel.insert(drink);
        }

        Snackbar.make(binding.getRoot(), "Đã lưu thành công!", Snackbar.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
