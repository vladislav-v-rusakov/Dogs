package ru.vladus177.dogs.viewModel;


import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.vladus177.dogs.Constant;
import ru.vladus177.dogs.DogApp;
import ru.vladus177.dogs.network.BreedsService;
import ru.vladus177.dogs.R;
import ru.vladus177.dogs.model.BreedsResponse;
import ru.vladus177.dogs.ui.BreedsActivityNavigator;

/**
 * Exposes the data to be used in the dog breeds list screen.
 */

public class BreedsViewModel extends Observable {

    // These observable fields will update Views automatically
    public final ObservableList<String> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private Context mContext;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BreedsActivityNavigator mNavigator;


    public BreedsViewModel(@NonNull Context context) {
        mContext = context;
    }


    public void start() {
        dataLoading.set(true);
        fetchBreedsList();
    }

    private void fetchBreedsList() {

        DogApp mApp = DogApp.create(mContext);
        BreedsService breedsService = mApp.getBreedsService();

        Disposable disposable = breedsService.fetchBreeds(Constant.BREEDS_ALL)
                .subscribeOn(mApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BreedsResponse>() {
                    @Override
                    public void accept(BreedsResponse breedResponse) throws Exception {
                        updateBreedsList(breedResponse.getBreedList());
                        dataLoading.set(false);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onError(mContext.getResources().getString(R.string.loading_error));
                        dataLoading.set(false);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updateBreedsList(List<String> breeds) {
        items.addAll(breeds);
        setChanged();
        notifyObservers();
    }

    public ObservableList<String> getItems() {
        return items;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        mContext = null;
        mNavigator = null;
    }

    public void onActivityCreated(BreedsActivityNavigator navigator) {
        mNavigator = navigator;
    }

    private void onError(String error) {
        if (mNavigator != null) {
            mNavigator.onError(error);
        }
    }
}
