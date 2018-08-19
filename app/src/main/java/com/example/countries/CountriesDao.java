package com.example.countries;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CountriesDao {
    @Insert
    void insert(CountryEntity countryEntity);

    @Query("SELECT * FROM countries ORDER BY name")
    LiveData<List<CountryEntity>> getAllCountries();

    @Query("SELECT * FROM countries WHERE id = :id")
    LiveData<CountryEntity> getCountryById(int id);

    @Query("SELECT COUNT(*) FROM countries")
    int getCountryCount();

    @Query("DELETE FROM countries")
    void clearCountries();
}
