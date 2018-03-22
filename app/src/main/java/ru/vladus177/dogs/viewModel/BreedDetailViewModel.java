package ru.vladus177.dogs.viewModel;

import android.content.Context;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.vladus177.dogs.DogApp;
import ru.vladus177.dogs.R;
import ru.vladus177.dogs.model.DogImageResponse;
import ru.vladus177.dogs.network.BreedsService;
import ru.vladus177.dogs.ui.BreedsActivityNavigator;


/**
 * /**
 * Exposes the data to be used in the breed detail screen.
 */

public class BreedDetailViewModel extends Observable {


    // These observable fields will update Views automatically
    public final ObservableList<String> images = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private String mBreed = "";

    private Context mContext;

    private BreedsActivityNavigator mNavigator;

    public BreedDetailViewModel(Context context) {
        mContext = context.getApplicationContext(); // Force use of Application Context.

    }

    public void start(String breed) {
        mBreed = breed;
        dataLoading.set(true);
        fetchBreedImages();

    }

    private void fetchBreedImages() {

        DogApp mApp = DogApp.create(mContext);
        BreedsService breedsService = mApp.getBreedsService();

        Disposable disposable = breedsService.fetchDogImages(mBreed)
                .subscribeOn(mApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DogImageResponse>() {
                    @Override
                    public void accept(DogImageResponse dogImageResponse) throws Exception {
                        updateImageList(dogImageResponse.getBreedImage());
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


    private void updateImageList(List<String> imageList) {
        images.addAll(imageList);
        setChanged();
        notifyObservers();
    }

    public void onRefresh() {
        dataLoading.set(true);
        fetchBreedImages();
    }

    public ObservableList<String> getImages() {
        return images;
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
