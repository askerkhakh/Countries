package com.example.countries;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "countries")
public class CountryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] flag;

    public String currency;

    public String language;

    public String timezone;
}
