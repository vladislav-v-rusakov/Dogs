package ru.vladus177.dogs.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import ru.vladus177.dogs.R;
import ru.vladus177.dogs.databinding.FragmentBreedsBinding;
import ru.vladus177.dogs.databinding.ItemBreedBinding;
import ru.vladus177.dogs.ui.BreedItemNavigator;
import ru.vladus177.dogs.ui.ScrollChildSwipeRefreshLayout;
import ru.vladus177.dogs.ui.activity.BreedsActivity;
import ru.vladus177.dogs.viewModel.BreedsViewModel;
import ru.vladus177.dogs.viewModel.ItemBreedViewModel;

/**
 * Display a list of breeds.
 */

public class BreedsFragment extends Fragment {

    private BreedsViewModel mBreedsViewModel;

    private FragmentBreedsBinding mFragmentBreedsBinding;

    private BreedsAdapter mListAdapter;

    public BreedsFragment() {
        // Requires empty public constructor
    }

    public static BreedsFragment newInstance() {
        return new BreedsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBreedsViewModel.items.isEmpty()) {
            mBreedsViewModel.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentBreedsBinding = FragmentBreedsBinding.inflate(inflater, container, false);

        mFragmentBreedsBinding.setView(this);

        mFragmentBreedsBinding.setViewmodel(mBreedsViewModel);

        setHasOptionsMenu(true);

        return mFragmentBreedsBinding.getRoot();
    }


    public void setViewModel(BreedsViewModel viewModel) {
        mBreedsViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupListAdapter();

        setupRefreshLayout();
    }

    @Override
    public void onDestroy() {
        mListAdapter.onDestroy();
        super.onDestroy();
    }


    private void setupListAdapter() {
        ListView listView =  mFragmentBreedsBinding.breedsList;

        mListAdapter = new BreedsAdapter(
                new ArrayList<String>(0),
                (BreedsActivity) getActivity()
        );
        listView.setAdapter(mListAdapter);
    }

    private void setupRefreshLayout() {
        ListView listView =  mFragmentBreedsBinding.breedsList;
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mFragmentBreedsBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);
    }

    public static class BreedsAdapter extends BaseAdapter {

        @Nullable private BreedItemNavigator mBreedItemNavigator;

        private List<String> mBreeds;


        BreedsAdapter(List<String> breeds, @Nullable BreedsActivity breedItemNavigator) {
            mBreedItemNavigator = breedItemNavigator;
            setList(breeds);

        }

        void onDestroy() {
            mBreedItemNavigator = null;
        }

        public void replaceData(List<String> breeds) {
            setList(breeds);
        }

        @Override
        public int getCount() {
            return mBreeds != null ? mBreeds.size() : 0;
        }

        @Override
        public String getItem(int i) {
            return mBreeds.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            String breed = getItem(i);
            ItemBreedBinding binding;
            if (view == null) {
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                // Create the binding
                binding = ItemBreedBinding.inflate(inflater, viewGroup, false);
            } else {
                // Recycling view
                binding = DataBindingUtil.getBinding(view);
            }

            final ItemBreedViewModel viewmodel = new ItemBreedViewModel(
                    breed,
                    viewGroup.getContext().getApplicationContext()
            );

            viewmodel.setNavigator(mBreedItemNavigator);

            binding.setViewmodel(viewmodel);

            viewmodel.setBreed(breed);

            return binding.getRoot();
        }


        private void setList(List<String> breeds) {
            mBreeds = breeds;
            notifyDataSetChanged();
        }
    }
}
