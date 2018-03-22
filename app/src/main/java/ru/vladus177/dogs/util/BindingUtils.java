package ru.vladus177.dogs.util;


import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;


import java.util.List;

import ru.vladus177.dogs.ui.ScrollChildSwipeRefreshLayout;
import ru.vladus177.dogs.ui.fragment.BreedDetailFragment;
import ru.vladus177.dogs.ui.fragment.BreedsFragment;
import ru.vladus177.dogs.viewModel.BreedsViewModel;

public class BindingUtils {

    /**
     * Reloads the data when the pull-to-refresh is triggered.
     * <p>
     * Creates the {@code android:onRefresh} for a {@link SwipeRefreshLayout}.
     */
    @BindingAdapter("android:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(ScrollChildSwipeRefreshLayout view,
                                                              final BreedsViewModel viewModel) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.start();
            }
        });
    }


    /**
     * Contains {@link BindingAdapter}s for the breed list.
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:itemsList")
    public static void setItemsList(ListView listView, List<String> items) {
        BreedsFragment.BreedsAdapter adapter = (BreedsFragment.BreedsAdapter) listView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

    /**
     * Contains {@link BindingAdapter}s for the breed list.
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:imageList")
    public static void setImageList(RecyclerView view, List<String> items) {
        BreedDetailFragment.ImageAdapter adapter = (BreedDetailFragment.ImageAdapter) view.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

}
