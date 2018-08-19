package com.example.countries;

class RestCountry {

    public String name;

    public String flag;

    public static class Currency {
        public String name;
    }
    public Currency[] currencies;

    public static class Language {
        public String name;
    }
    public Language[] languages;

    public String[] timezones;

}
