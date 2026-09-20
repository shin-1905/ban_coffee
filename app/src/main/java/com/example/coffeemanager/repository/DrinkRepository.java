package com.example.coffeemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coffeemanager.data.Drink;
import com.example.coffeemanager.data.DrinkQuery;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrinkRepository {

    private final DrinkQuery drinkQuery;
    private final MutableLiveData<List<Drink>> allDrinks = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DrinkRepository(Application application) {
        drinkQuery = new DrinkQuery(application);
        taiTatCaDoUong();
    }

    public LiveData<List<Drink>> getAllDrinks() {
        return allDrinks;
    }

    public LiveData<List<Drink>> searchDrinks(String query) {
        MutableLiveData<List<Drink>> ketQuaTimKiem = new MutableLiveData<>();
        executor.execute(() -> ketQuaTimKiem.postValue(drinkQuery.searchDrinks(query)));
        return ketQuaTimKiem;
    }

    public void insert(Drink drink) {
        executor.execute(() -> {
            drinkQuery.insertDrink(drink);
            taiTatCaDoUongNoiBo();
        });
    }

    public void update(Drink drink) {
        executor.execute(() -> {
            drinkQuery.updateDrink(drink);
            taiTatCaDoUongNoiBo();
        });
    }

    public void delete(Drink drink) {
        executor.execute(() -> {
            drinkQuery.deleteDrink(drink.getId());
            taiTatCaDoUongNoiBo();
        });
    }

    public Drink getDrinkById(int id) {
        return drinkQuery.getDrinkById(id);
    }

    private void taiTatCaDoUong() {
        executor.execute(this::taiTatCaDoUongNoiBo);
    }

    private void taiTatCaDoUongNoiBo() {
        allDrinks.postValue(drinkQuery.getAllDrinks());
    }
}
