package ru.vladus177.dogs;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import ru.vladus177.dogs.model.DogImageResponse;
import ru.vladus177.dogs.network.BreedsService;
import ru.vladus177.dogs.viewModel.BreedDetailViewModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BreedDetailViewModelTest {

    private static List<String> BREEDS;

    @Mock
    private BreedsService mBreedsService;

    @Mock
    private Context mContext;

    private BreedDetailViewModel mViewModel;


    @Before
    public void setupViewModel() {

        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewModel = new BreedDetailViewModel(mContext);

        BREEDS = Arrays.asList("image1", "image2", "image3", "image4");
    }


    @Test
    public void testApiResponse() {

        when(mBreedsService.fetchDogImages("Akita"))
                .thenReturn(Observable.just(new DogImageResponse(BREEDS)));

        mViewModel.updateImageList(BREEDS);

        // progress indicator is hidden
        assertFalse(mViewModel.dataLoading.get());

        // And data loaded
        assertFalse(mViewModel.images.isEmpty());
        assertTrue(mViewModel.images.size() == 4);
    }

    @Test
    public void testApiResponseError() {

        when(mBreedsService.fetchDogImages("Akita"))
                .thenReturn(Observable.<DogImageResponse>error(new Throwable("An error has occurred!")));

        // progress indicator is hidden
        assertFalse(mViewModel.dataLoading.get());

        // And data not loaded
        assertTrue(mViewModel.images.isEmpty());
    }
}
