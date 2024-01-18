package com.tutorial.tvapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tutorial.tvapp.databinding.ActivityDetailBinding
import com.tutorial.tvapp.model.DetailResponse
import com.tutorial.tvapp.player.PlaybackActivity
import com.tutorial.tvapp.utils.Common
import com.tutorial.tvapp.utils.Common.Companion.isEllipsized
import com.tutorial.tvapp.viewmodel.DetailViewmodel
import com.tutorial.tvapp.viewmodel.DetailViewmodelFactory

class DetailActivity :FragmentActivity() {
    lateinit var binding : ActivityDetailBinding
    lateinit var viewmodel : DetailViewmodel
    val castFragment = ListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail)

        addFragment(castFragment)
        val movieId= intent.getIntExtra("id",0)
        var repository = (application as MyApplication).tmdbRepo
         viewmodel = ViewModelProvider(this,
             DetailViewmodelFactory(repository, movieId)).get(DetailViewmodel::class.java)


        viewmodel.movieDetails.observe(this){
            when(it){
                is com.tutorial.tvapp.api.Response.Loading->{

                }
                is com.tutorial.tvapp.api.Response.Success->{
                    setData(it.data)
                }
                is com.tutorial.tvapp.api.Response.Error->{
                }

            }
        }
        viewmodel.castDetails.observe(this){
            when(it){
                is com.tutorial.tvapp.api.Response.Loading->{

                }
                is com.tutorial.tvapp.api.Response.Success->{
                    if(!it.data?.cast.isNullOrEmpty()){
                        castFragment.bindCastData(it.data?.cast!!)
                    }
                }
                is com.tutorial.tvapp.api.Response.Error->{

                }

            }
        }

        binding.addToList.setOnKeyListener{view,keyCode,keyEvent->
            when (keyCode){
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if(keyEvent.action == KeyEvent.ACTION_DOWN) {
                        castFragment.requestFocus()
                    }
                }
            }
            false
        }
        binding.play.setOnClickListener {
            val intent = Intent(this, PlaybackActivity::class.java)
                  startActivity(intent)
        }

    }
    private fun addFragment(castFragment: ListFragment) {
      val transaction = supportFragmentManager.beginTransaction()
          transaction.add(R.id.cast_fragment,castFragment)
          transaction.commit()
    }
    private fun setData(response: DetailResponse?) {
        binding.title.text = response?.title ?: ""
        binding.subtitle.text = getSubtitle(response)
        binding.description.text = response?.overview ?: ""
        val path = "https://www.themoviedb.org/t/p/w780" + (response?.backdrop_path ?: "") ?: "https://img.myloview.com/stickers/white-laptop-screen-with-hd-video-technology-icon-isolated-on-grey-background-abstract-circle-random-dots-vector-illustration-400-176057922.jpg"
        Glide.with(this)
            .load(path)
            .into(binding.imgBanner)
        binding.description.isEllipsized {isEllipsized->
            binding.showMore.visibility = if (isEllipsized) View.VISIBLE else View.GONE
            binding.showMore.setOnClickListener{
                Common.descriptionDialog(this,
                    response?.title,
                    getSubtitle(response),
                    response?.overview.toString()
                    )
            }
        }
    }
    fun getSubtitle(response: DetailResponse?): String {
       val rating = if (response!!.adult){
           "18+ "
       } else {
           "13+ "
       }
        val genres = response.genres.joinToString(
            prefix = " ",
            postfix = " . ",
            separator = " . "
        ){it.name}
        val hours :Int = response.runtime / 60
        val min: Int = response.runtime % 60
        return rating+genres+hours+ "h" + min +"m"
      }

}