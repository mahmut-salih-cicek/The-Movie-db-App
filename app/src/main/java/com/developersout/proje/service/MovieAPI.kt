package com.developersout.proje.service

import com.developersout.proje.model.Movie
import com.developersout.proje.model.MovieResponse
import com.developersout.proje.model.PopularMovies
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    //https://api.themoviedb.org/3/movie/upcoming?api_key=91adc38941140177793789420bc77918&language=en-US&page=1

    @GET("3/movie/upcoming")
    fun getMovies(@Query("api_key") key: String): Single<PopularMovies>


}