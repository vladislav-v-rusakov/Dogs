package ru.vladus177.dogs;


import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.vladus177.dogs.network.ApiFactory;
import ru.vladus177.dogs.network.BreedsService;

public class DogApp extends Application {

    private BreedsService mBreedsService;
    private Scheduler mScheduler;

    private static DogApp get(Context context) {
        return (DogApp) context.getApplicationContext();
    }

    public static DogApp create(Context context) {
        return DogApp.get(context);
    }

    public BreedsService getBreedsService() {
        if (mBreedsService == null) {
            mBreedsService = ApiFactory.create();
        }

        return mBreedsService;
    }

    public Scheduler subscribeScheduler() {
        if (mScheduler == null) {
            mScheduler = Schedulers.io();
        }

        return mScheduler;
    }

    public void setBreedsService(BreedsService breedsService) {
        mBreedsService = breedsService;
    }

    public void setScheduler(Scheduler scheduler) {
        this.mScheduler = scheduler;
    }
}
