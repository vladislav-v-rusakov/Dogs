package ru.vladus177.dogs.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import ru.vladus177.dogs.ui.BreedItemNavigator;

public class ItemBreedViewModel extends BaseObservable {

    private String breed;
    private Context context;
    @Nullable
    private WeakReference<BreedItemNavigator> mNavigator;

    public ItemBreedViewModel(String breed, Context context){
        this.breed = breed;
        this.context = context;
    }

    public void setNavigator(BreedItemNavigator navigator) {
        mNavigator = new WeakReference<>(navigator);
    }

    public String getBreedName() {

        return breed;

    }

    public void onItemClick(){
        if (mNavigator != null) {
            mNavigator.get().openBreedDetails(breed);
        }
    }

    public void setBreed(String breed) {
        this.breed = breed;
        notifyChange();
    }
}
