package com.example.coffeemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coffeemanager.data.Drink;
import com.example.coffeemanager.repository.DrinkRepository;

import java.util.List;

public class DrinkViewModel extends AndroidViewModel {

    private final DrinkRepository repository;
    private final LiveData<List<Drink>> allDrinks;
    private final MutableLiveData<Drink> selectedDrink = new MutableLiveData<>();

    public DrinkViewModel(@NonNull Application application) {
        super(application);
        repository = new DrinkRepository(application);
        allDrinks = repository.getAllDrinks();
    }

    public LiveData<List<Drink>> getAllDrinks() {
        return allDrinks;
    }

    public LiveData<List<Drink>> searchDrinks(String query) {
        return repository.searchDrinks(query);
    }

    public void insert(Drink drink) {
        repository.insert(drink);
    }

    public void update(Drink drink) {
        repository.update(drink);
    }

    public void delete(Drink drink) {
        repository.delete(drink);
    }

    public DrinkRepository getRepository() {
        return repository;
    }

    public LiveData<Drink> getSelectedDrink() {
        return selectedDrink;
    }

    public void selectDrink(Drink drink) {
        selectedDrink.setValue(drink);
    }

    public void clearSelection() {
        selectedDrink.setValue(null);
    }
}
