package com.example.countries;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class CountryRepository {

    private final CountriesDao mCountriesDao;
    private final LiveData<List<CountryEntity>> mAllCountries;

    public CountryRepository(Application application) {
        CountriesRoomDatabase db = CountriesRoomDatabase.getDatabase(application);
        mCountriesDao = db.countriesDao();
        mAllCountries = mCountriesDao.getAllCountries();
    }

    public LiveData<List<CountryEntity>> getAllCountries() {
        new FetchDataAsync(mCountriesDao, false).execute(null, null, null);
        return mAllCountries;
    }


    public LiveData<CountryEntity> getCountryById(int id) {
        return mCountriesDao.getCountryById(id);
    }

    public void refreshCountries() {
        new FetchDataAsync(mCountriesDao, true).execute(null, null, null);
    }

    private static class FetchDataAsync extends AsyncTask<Void, Void, Void> {

        private final boolean mClearBeforeFetch;
        private CountriesDao mCountriesDao;

        FetchDataAsync(CountriesDao countriesDao, boolean clearBeforeFetch) {
            mCountriesDao = countriesDao;
            mClearBeforeFetch = clearBeforeFetch;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mClearBeforeFetch)
                mCountriesDao.clearCountries();
            if (mClearBeforeFetch || (mCountriesDao.getCountryCount() == 0))
                fetchAllCountries();
            return null;
        }

        private void fetchAllCountries() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://restcountries.eu/rest/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RestCountriesService restCountriesService = retrofit.create(RestCountriesService.class);
            List<RestCountry> restCountries = null;
            try {
                restCountries = restCountriesService.listCountries().execute().body();
            }
            catch (Throwable e) {
                // TODO
            }

            if (restCountries != null)
                for (RestCountry restCountry : restCountries) {
                    CountryEntity countryEntity = new CountryEntity();
                    countryEntity.name = restCountry.name;
                    countryEntity.flag = downloadFlag(restCountry.flag);

                    List<String> currencies = new ArrayList<>();
                    for (RestCountry.Currency currency: restCountry.currencies)
                        currencies.add(currency.name);
                    countryEntity.currency = TextUtils.join(", ", currencies);

                    List<String> languages = new ArrayList<>();
                    for (RestCountry.Language language: restCountry.languages)
                        languages.add(language.name);
                    countryEntity.language = TextUtils.join(", ", languages);

                    countryEntity.timezone = TextUtils.join(", ", restCountry.timezones);

                    mCountriesDao.insert(countryEntity);
                }
        }

        private byte[] downloadFlag(String flagURL) {
            try {
                URL url = new URL(flagURL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[4096];
                while ((nRead = stream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
