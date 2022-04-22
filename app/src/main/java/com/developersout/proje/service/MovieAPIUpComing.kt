package com.developersout.proje.service

import com.developersout.proje.model.PopularMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPIUpComing {
    //https://api.themoviedb.org/3/movie/now_playing?api_key=91adc38941140177793789420bc77918&language=en-US&page=1
    @GET("3/movie/now_playing")
    fun getMoviesNowPlaying (@Query("api_key") key: String): Single<PopularMovies>

}