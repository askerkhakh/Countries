package com.example.countries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

class CountryListViewModel extends AndroidViewModel{

    private final CountryRepository mRepository;
    private LiveData<List<CountryEntity>> mAllCountries;

    public CountryListViewModel(Application application) {
        super(application);
        mRepository = new CountryRepository(application);
        mAllCountries = mRepository.getAllCountries();
    }

    public LiveData<List<CountryEntity>> getAllCountries() {
        return mAllCountries;
    }

    public LiveData<CountryEntity> getCountryById(int id) {
        return mRepository.getCountryById(id);
    }

    public void refreshCountries() {
        mRepository.refreshCountries();
    }
}
