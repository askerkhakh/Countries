package com.example.countries;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

/**
 * A fragment representing a single Country detail screen.
 * This fragment is either contained in a {@link CountryListActivity}
 * in two-pane mode (on tablets) or a {@link CountryDetailActivity}
 * on handsets.
 */
public class CountryDetailFragment extends Fragment {

    public static final String COUNTRY_ID = "country_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CountryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(COUNTRY_ID)) {
            Activity activity = this.getActivity();
            CountryListViewModel countryListViewModel = ViewModelProviders.of(this).get(CountryListViewModel.class);
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(mCountry.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.country_detail, container, false);
        CountryListViewModel countryListViewModel = ViewModelProviders.of(this).get(CountryListViewModel.class);
        countryListViewModel.getCountryById(getArguments().getInt(COUNTRY_ID)).observe(this, new Observer<CountryEntity>() {
            @Override
            public void onChanged(@Nullable CountryEntity countryEntity) {
                if (countryEntity == null)
                    return;
                ((TextView) rootView.findViewById(R.id.name)).setText(countryEntity.name);
                SVG svg = ByteArrayToSVGConverter.convert(countryEntity.flag);
                if (svg != null)
                    ((SVGImageView) rootView.findViewById(R.id.flag)).setSVG(svg);
                ((TextView) rootView.findViewById(R.id.currency)).setText(countryEntity.currency);
                ((TextView) rootView.findViewById(R.id.language)).setText(countryEntity.language);
                ((TextView) rootView.findViewById(R.id.timezone)).setText(countryEntity.timezone);
            }
        });
        return rootView;
    }
}
