package ru.vladus177.dogs.network;

import retrofit2.http.GET;
import io.reactivex.Observable;
import retrofit2.http.Path;
import retrofit2.http.Url;
import ru.vladus177.dogs.model.BreedsResponse;
import ru.vladus177.dogs.model.DogImageResponse;

public interface BreedsService {

    @GET
    Observable<BreedsResponse> fetchBreeds(@Url String url);

    @GET("breed/{breed}/images")
    Observable<DogImageResponse> fetchDogImages(@Path("breed") String breed);


}
