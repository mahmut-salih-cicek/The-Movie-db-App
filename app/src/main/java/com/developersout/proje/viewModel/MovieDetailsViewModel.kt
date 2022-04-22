package com.developersout.proje.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.developersout.proje.model.Movie
import com.developersout.proje.service.MovieDatabase
import kotlinx.coroutines.launch

class MovieDetailsViewModel(application: Application):BaseViewModel(application) {

    val movieData = MutableLiveData<Movie>()

    fun veriyiRoomdanAl(id : Int){
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()
            val movie =  dao.getMovie(id)
            movieData.value = movie
        }
    }

}