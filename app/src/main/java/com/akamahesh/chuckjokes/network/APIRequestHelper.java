package com.akamahesh.chuckjokes.network;

import com.akamahesh.chuckjokes.model.Joke;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIRequestHelper {

    String kRandomJokes = "";

    @GET("jokes/random")
    Call<Joke> getRandomJoke();



}
