package com.developersout.proje.viewModel

import android.app.Application
import android.app.PendingIntent.getActivity
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.afdhal_fa.imageslider.ImageSlider
import com.developersout.proje.R
import com.developersout.proje.adapter.SampleAdapter
import com.developersout.proje.model.BannerUIModel
import com.developersout.proje.model.Movie
import com.developersout.proje.model.PopularMovies
import com.developersout.proje.service.MovieDatabase
import com.developersout.proje.service.ServiceBuilder
import com.developersout.proje.util.OzelSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


class MovieHomeViewModel(application: Application) : BaseViewModel(application) {

    private val mAdapter by lazy { SampleAdapter() }

    var APIKEY="91adc38941140177793789420bc77918"
    var nowPlayingList = ArrayList<Movie>()

    val movies = MutableLiveData<List<Movie>>()
    val movieYukleniyor = MutableLiveData<Boolean>()
    private val disposable = CompositeDisposable()

    private val guncellemeZamani = 5 * 60 * 1000 * 1000 * 1000L
    private val ozelSharedPreferences = OzelSharedPreferences(getApplication())


    /*
      .subscribe({
                          response -> println(response)},
                      {t -> println(t)
                      })
       */


    fun refreshData(){
        val kaydedilmeZamani =ozelSharedPreferences.zamaniAl()

        if(kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani){
            getDataFromSQLite()
        }else {
            getDataFromInternet()
        }

    }
    fun refreshFromInternet(){
        getDataFromInternet()
    }
    private fun getDataFromInternet(){
            movieYukleniyor.value = true

            disposable.add(
                ServiceBuilder.buildService().getMovies(APIKEY)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribeWith(object :DisposableSingleObserver<PopularMovies>(){
                        override fun onSuccess(t: PopularMovies) {
                            println(t.results[0].poster_path)
                            var data2 = t.results
                            nowPlayingList = data2 as ArrayList<Movie>
                            val imageList = ArrayList<BannerUIModel>()
                            for (k in nowPlayingList){
                                //https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg
                                imageList.add(BannerUIModel("https://image.tmdb.org/t/p/w500"+k.poster_path,k.title,k.overview))
                            }

                            mAdapter.setItem(imageList)
                            mAdapter.onItemClick = {
                                //   Toast.makeText(application, it.title, Toast.LENGTH_SHORT).show()
                            }

                           saveSQLite(t)
                            Toast.makeText(getApplication(),"Internetten alindi",Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })
            )
    }



    private fun getDataFromSQLite(){
        movieYukleniyor.value = true

        launch {
            val movieList = MovieDatabase(getApplication()).movieDao().getAllMovie()
            displayMovies(movieList)
            Toast.makeText(getApplication(),"Roomdan alindi",Toast.LENGTH_SHORT).show()
        }
    }
    fun saveSQLite(popularMovies: PopularMovies){
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()

            dao.deleteAllMovie()
            dao.insertAll(*popularMovies.results.toTypedArray())

            displayMovies(popularMovies.results)
        }
        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }
    private fun displayMovies(movieList : List<Movie>){
        movieYukleniyor.value = false
        movies.value=movieList
    }
}