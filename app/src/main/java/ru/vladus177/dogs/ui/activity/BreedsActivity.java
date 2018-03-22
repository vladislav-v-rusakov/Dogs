package ru.vladus177.dogs.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.Toolbar;

import ru.vladus177.dogs.R;
import ru.vladus177.dogs.ui.BreedItemNavigator;
import ru.vladus177.dogs.ui.BreedsActivityNavigator;
import ru.vladus177.dogs.ui.fragment.BreedsFragment;
import ru.vladus177.dogs.util.ActivityUtils;
import ru.vladus177.dogs.viewModel.BreedsViewModel;
import ru.vladus177.dogs.viewModel.ViewModelHolder;

public class BreedsActivity extends BaseActivity implements BreedItemNavigator, BreedsActivityNavigator {

    public static final String BREEDS_VIEWMODEL_TAG = "BREEDS_VIEWMODEL_TAG";

    private BreedsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeds);

        setupToolbar();

        BreedsFragment breedsFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        breedsFragment.setViewModel(mViewModel);

        mViewModel.onActivityCreated(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.reset();
    }

    private BreedsViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<BreedsViewModel> retainedViewModel =
                (ViewModelHolder<BreedsViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(BREEDS_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            BreedsViewModel viewModel = new BreedsViewModel(
                    getApplicationContext());
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    BREEDS_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    private BreedsFragment findOrCreateViewFragment() {
        BreedsFragment breedsFragment =
                (BreedsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (breedsFragment == null) {
            // Create the fragment
            breedsFragment = BreedsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), breedsFragment, R.id.contentFrame);
        }
        return breedsFragment;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void openBreedDetails(String breed) {
        Intent intent = new Intent(this, BreedDetailActivity.class);
        intent.putExtra(BreedDetailActivity.EXTRA_BREED, breed);
        startActivity(intent);
    }

    @Override
    public void onError(String error) {
        showLongToast(error);
    }
}
