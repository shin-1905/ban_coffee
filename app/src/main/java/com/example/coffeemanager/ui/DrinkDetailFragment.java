package com.example.coffeemanager.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.btl.databinding.FragmentDrinkDetailBinding;
import com.example.coffeemanager.data.Drink;
import com.example.coffeemanager.viewmodel.DrinkViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.NumberFormat;
import java.util.Locale;

public class DrinkDetailFragment extends Fragment {

    private FragmentDrinkDetailBinding binding;
    private DrinkViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDrinkDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(DrinkViewModel.class);

        binding.layoutOrder.setVisibility(View.GONE);
        binding.btnAddToCart.setVisibility(View.GONE);

        viewModel.getSelectedDrink().observe(getViewLifecycleOwner(), drink -> {
            if (drink != null) {
                displayDrink(drink);
            }
        });

        binding.btnEdit.setOnClickListener(v -> {
            Drink drink = viewModel.getSelectedDrink().getValue();
            if (drink != null) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_list_container, DrinkFormFragment.newInstance(drink.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void displayDrink(Drink drink) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        binding.tvDetailName.setText(drink.getName());
        binding.tvDetailPrice.setText(formatter.format(drink.getPrice()) + " đ");
        binding.tvDetailDescription.setText(drink.getDescription());

        if (drink.getImageUri() != null && !drink.getImageUri().isEmpty()) {
            Glide.with(this).load(Uri.parse(drink.getImageUri())).into(binding.ivDetailImage);
        } else {
            binding.ivDetailImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private void showDeleteDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa món này không?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Drink drink = viewModel.getSelectedDrink().getValue();
                    if (drink != null) {
                        viewModel.delete(drink);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
