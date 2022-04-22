package com.developersout.proje.view

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.afdhal_fa.imageslider.ImageSlider
import com.developersout.proje.R
import com.developersout.proje.adapter.SampleAdapter
import com.developersout.proje.databinding.ActivityMainBinding
import com.developersout.proje.model.BannerUIModel
import com.developersout.proje.model.Movie
import com.developersout.proje.model.PopularMovies
import com.developersout.proje.service.MovieApiServisUpComing
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    var APIKEY="d9d25ba60c3c37758dfa035b7f861397"
    val movies = MutableLiveData<List<Movie>>()
    private val disposable = CompositeDisposable()
    var nowPlayingList = ArrayList<Movie>()

    private lateinit var binding : ActivityMainBinding
    private val mAdapter by lazy { SampleAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /// transparent status bar
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){

            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            window.decorView.windowInsetsController!!.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        getDataFromNet()

    }


    private fun  getDataFromNet(){

        disposable.add(
            MovieApiServisUpComing.buildService().getMoviesNowPlaying(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : DisposableSingleObserver<PopularMovies>(){
                    override fun onSuccess(t: PopularMovies) {
                       // println(t.results[0].poster_path)
                        var data2 = t.results
                        nowPlayingList = data2 as ArrayList<Movie>
                        val imageList = ArrayList<BannerUIModel>()
                        for (k in nowPlayingList){
                            //https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg
                            imageList.add(BannerUIModel("https://image.tmdb.org/t/p/w500"+k.poster_path,k.title,k.overview))
                        }
                        println(imageList)
                        mAdapter.setItem(imageList)
                        var id = findViewById<ImageSlider>(R.id.imageSlide)
                        id.setImageListWithAdapter(mAdapter, imageList.size)
                        mAdapter.onItemClick = {
                         //   Toast.makeText(application, it.title, Toast.LENGTH_SHORT).show()
                        }

                     //   Toast.makeText(getApplication(),"Internetten alindi", Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }




}