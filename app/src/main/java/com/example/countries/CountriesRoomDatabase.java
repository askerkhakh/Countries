package com.example.countries;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CountryEntity.class}, version = 1)
public abstract class CountriesRoomDatabase extends RoomDatabase {

    public abstract CountriesDao countriesDao();

    private static CountriesRoomDatabase INSTANCE;

    public static CountriesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CountriesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CountriesRoomDatabase.class,
                            "countries_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
