package ru.vladus177.dogs.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import ru.vladus177.dogs.R;
import ru.vladus177.dogs.databinding.FragmentBreedDetailBinding;
import ru.vladus177.dogs.ui.ScrollChildSwipeRefreshLayout;
import ru.vladus177.dogs.viewModel.BreedDetailViewModel;

/**
 * Main UI for the breed detail screen.
 */
public class BreedDetailFragment extends Fragment {

    public static final String ARGUMENT_BREED = "BREED";

    private BreedDetailViewModel mViewModel;

    private FragmentBreedDetailBinding mFragmentBreedDetailBinding;

    private ImageAdapter mAdapter;


    public static BreedDetailFragment newInstance(String breed) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_BREED, breed);
        BreedDetailFragment fragment = new BreedDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public void setViewModel(BreedDetailViewModel breedViewModel) {
        mViewModel = breedViewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAdapter();

        setupRefreshLayout();

        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setTitle(getArguments().getString(ARGUMENT_BREED));

    }

    private void setupAdapter() {

        RecyclerView recyclerView = mFragmentBreedDetailBinding.recycler;
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mAdapter = new ImageAdapter(new ArrayList<String>(0));
        recyclerView.setAdapter(mAdapter);

    }

    private void setupRefreshLayout() {
        RecyclerView recyclerView = mFragmentBreedDetailBinding.recycler;
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mFragmentBreedDetailBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(getArguments().getString(ARGUMENT_BREED));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_breed_detail, container, false);

        mFragmentBreedDetailBinding = FragmentBreedDetailBinding.bind(view);
        mFragmentBreedDetailBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);

        return view;
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

        private List<String> mImages;


        ImageAdapter(List<String> images) {
            mImages = images;
            setList(images);
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.square_image, parent, false);

            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(final ImageHolder holder, int position) {
            String url = mImages.get(position);
            if (url != null && !url.isEmpty()) {
                Picasso.with(holder.image.getContext())
                        .load(url)
                        .resize(100, 100)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.image);
            }

        }

        private void setList(List<String> images) {
            mImages = images;
            notifyDataSetChanged();
        }

        public void replaceData(List<String> images) {
            setList(images);
        }

        @Override
        public int getItemCount() {
            return mImages.size();
        }


        class ImageHolder extends RecyclerView.ViewHolder {
            ImageView image;

            ImageHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image_indicator);
            }
        }
    }
}
