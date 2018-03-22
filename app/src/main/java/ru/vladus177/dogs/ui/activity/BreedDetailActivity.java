package ru.vladus177.dogs.ui.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import ru.vladus177.dogs.R;
import ru.vladus177.dogs.ui.BreedsActivityNavigator;
import ru.vladus177.dogs.ui.fragment.BreedDetailFragment;
import ru.vladus177.dogs.util.ActivityUtils;
import ru.vladus177.dogs.viewModel.BreedDetailViewModel;
import ru.vladus177.dogs.viewModel.ViewModelHolder;

public class BreedDetailActivity extends BaseActivity implements BreedsActivityNavigator {


    public static final String EXTRA_BREED = "EXTRA_BREED";

    public static final String BREEDDETAIL_VIEWMODEL_TAG = "BREEDDETAIL_VIEWMODEL_TAG";

    private BreedDetailViewModel mBreedDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_breeds);

        setupToolbar();

        BreedDetailFragment breedDetailFragment = findOrCreateViewFragment();

        mBreedDetailViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        breedDetailFragment.setViewModel(mBreedDetailViewModel);

        mBreedDetailViewModel.onActivityCreated(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBreedDetailViewModel.reset();
    }

    @NonNull
    private BreedDetailViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<BreedDetailViewModel> retainedViewModel =
                (ViewModelHolder<BreedDetailViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(BREEDDETAIL_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            BreedDetailViewModel viewModel = new BreedDetailViewModel(
                    getApplicationContext());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    BREEDDETAIL_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    private BreedDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        String breed = getIntent().getStringExtra(EXTRA_BREED);

        BreedDetailFragment breedDetailFragment = (BreedDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (breedDetailFragment == null) {
            breedDetailFragment = BreedDetailFragment.newInstance(breed);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    breedDetailFragment, R.id.contentFrame);
        }
        return breedDetailFragment;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onError(String error) {
        showLongToast(error);
    }
}
