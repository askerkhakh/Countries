package com.example.countries;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

import java.util.List;

/**
 * An activity representing a list of Countries. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CountryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CountryListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.country_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecyclerView recyclerView = findViewById(R.id.country_list);
        assert recyclerView != null;
        final CountryListRecyclerViewAdapter adapter = new CountryListRecyclerViewAdapter(this, mTwoPane);
        recyclerView.setAdapter(adapter);

        final CountryListViewModel countryListViewModel = ViewModelProviders.of(this).get(CountryListViewModel.class);
        countryListViewModel.getAllCountries().observe(this, new Observer<List<CountryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CountryEntity> countryEntities) {
                adapter.setCountries(countryEntities);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryListViewModel.refreshCountries();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }

    public static class CountryListRecyclerViewAdapter
            extends RecyclerView.Adapter<CountryListRecyclerViewAdapter.ViewHolder> {

        private final CountryListActivity mParentActivity;
        @Nullable
        private List<CountryEntity> mCountries;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryEntity item = (CountryEntity) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(CountryDetailFragment.COUNTRY_ID, item.id);
                    CountryDetailFragment fragment = new CountryDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.country_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CountryDetailActivity.class);
                    intent.putExtra(CountryDetailFragment.COUNTRY_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        CountryListRecyclerViewAdapter(CountryListActivity parent,
                                       boolean twoPane) {
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (mCountries == null)
                return;
            CountryEntity countryEntity = mCountries.get(position);
            holder.mCountryNameView.setText(countryEntity.name);
            byte[] flag = countryEntity.flag;
            SVG svg = ByteArrayToSVGConverter.convert(flag);
            if (svg != null)
                holder.mCountryFlagView.setSVG(svg);

            holder.itemView.setTag(countryEntity);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (mCountries == null)
                return 0;
            return mCountries.size();
        }

        public void setCountries(List<CountryEntity> countries) {
            this.mCountries = countries;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mCountryNameView;
            final SVGImageView mCountryFlagView;

            ViewHolder(View view) {
                super(view);
                mCountryNameView = view.findViewById(R.id.name);
                mCountryFlagView = view.findViewById(R.id.flag);
            }
        }
    }
}
