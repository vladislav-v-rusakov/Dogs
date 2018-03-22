package ru.vladus177.dogs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.vladus177.dogs.model.BreedsResponse;
import ru.vladus177.dogs.model.DogImageResponse;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private MockApiForTests testApi;

    @Before
    public void setup() {
        testApi = new MockApiForTests();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ru.vladus177.dogs", appContext.getPackageName());
    }

    @Test
    public void loadImagesForBreed() {
        testApi.getAnimalImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DogImageResponse>() {
                    @Override
                    public void accept(DogImageResponse imageResponse) throws Exception {
                        assertEquals(imageResponse, Arrays.asList("image1", "image2", "image3", "image4"));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Test
    public void loadAllOfTheBreeds() {
        testApi.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BreedsResponse>() {
                    @Override
                    public void accept(BreedsResponse breedResponse) throws Exception {
                        assertEquals(breedResponse.getStatus(), "success");
                        assertEquals(breedResponse.getBreedList().size(), 4);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    class MockApiForTests {

        private Map<String, List<String>> createMap() {
            Map<String, List<String>> map = new HashMap<>();
            map.put("akita", new ArrayList<String>(0));
            map.put("beagle", new ArrayList<String>(0));
            map.put("borzoi", new ArrayList<String>(0));
            map.put("boxer", new ArrayList<String>(0));
            return map;
        }

        Observable<DogImageResponse> getAnimalImages() {
            List<String> images = Arrays.asList("image1", "image2", "image3", "image4");
            return Observable.just(new DogImageResponse(images));

        }

        Observable<BreedsResponse> getCategories() {
            Map<String, List<String>> message = createMap();
            return Observable.just(new BreedsResponse("success", message));

        }
    }
}
