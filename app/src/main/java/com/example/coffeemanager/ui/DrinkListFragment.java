package com.example.coffeemanager.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.btl.R;
import com.example.btl.databinding.FragmentDrinkListBinding;
import com.example.coffeemanager.data.Drink;
import com.example.coffeemanager.viewmodel.DrinkViewModel;

import java.util.List;

public class DrinkListFragment extends Fragment {

    private FragmentDrinkListBinding binding;
    private DrinkViewModel viewModel;
    private DrinkAdapter adapter;
    private LiveData<List<Drink>> currentSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDrinkListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(DrinkViewModel.class);

        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).setSupportActionBar(binding.toolbar);
            String role = ((MainActivity) requireActivity()).getUserRole();
            if (!"manager".equals(role)) {
                binding.fabAdd.setVisibility(View.GONE);
            }
        }

        setupRecyclerView();
        setupSearch();
        observeDrinks();

        binding.fabAdd.setOnClickListener(v -> {
            viewModel.clearSelection();
            openFragment(DrinkFormFragment.newInstance(-1));
        });
    }

    private void setupRecyclerView() {
        adapter = new DrinkAdapter(drink -> {
            viewModel.selectDrink(drink);
            openFragment(new DrinkDetailFragment());
        });
        binding.recyclerDrinks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDrinks.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s == null ? "" : s.toString().trim();
                bindSource(query.isEmpty() ? viewModel.getAllDrinks() : viewModel.searchDrinks(query));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void observeDrinks() {
        bindSource(viewModel.getAllDrinks());
    }

    private void bindSource(LiveData<List<Drink>> source) {
        if (currentSource != null) {
            currentSource.removeObservers(getViewLifecycleOwner());
        }
        currentSource = source;
        currentSource.observe(getViewLifecycleOwner(), drinks -> adapter.setDrinks(drinks));
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            openFragment(new HistoryFragment());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
