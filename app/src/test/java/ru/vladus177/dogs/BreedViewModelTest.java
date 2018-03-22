package ru.vladus177.dogs;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import ru.vladus177.dogs.model.BreedsResponse;
import ru.vladus177.dogs.network.BreedsService;
import ru.vladus177.dogs.viewModel.BreedsViewModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


public class BreedViewModelTest {

    private static Map<String, List<String>> BREEDS_RESPONSE;

    private static List<String> BREEDS;

    @Mock
    private BreedsService mBreedsService;

    @Mock
    private Context mContext;

    private BreedsViewModel mViewModel;

    private static Map<String, List<String>> createMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("akita", new ArrayList<String>(0));
        map.put("beagle", new ArrayList<String>(0));
        map.put("borzoi", new ArrayList<String>(0));
        map.put("boxer", new ArrayList<String>(0));
        return map;
    }

    @Before
    public void setupViewModel() {

        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewModel = new BreedsViewModel(mContext);

        BREEDS_RESPONSE = createMap();

        BREEDS = new ArrayList<>(BREEDS_RESPONSE.keySet());
    }


    @Test
    public void testApiResponse() {

        String SUCCESS = "SUCCESS";
        when(mBreedsService.fetchBreeds("https://dog.ceo/api/"))
                .thenReturn(Observable.just(new BreedsResponse(SUCCESS, BREEDS_RESPONSE)));

        mViewModel.updateBreedsList(BREEDS);

        // progress indicator is hidden
        assertFalse(mViewModel.dataLoading.get());

        // And data loaded
        assertFalse(mViewModel.items.isEmpty());
        assertTrue(mViewModel.items.size() == 4);
    }

    @Test
    public void testApiResponseError() {

        when(mBreedsService.fetchBreeds("https://dog.ceo/api/"))
                .thenReturn(Observable.<BreedsResponse>error(new Throwable("An error has occurred!")));
        // progress indicator is hidden
        assertFalse(mViewModel.dataLoading.get());

        // And data not loaded
        assertTrue(mViewModel.items.isEmpty());


    }
}
