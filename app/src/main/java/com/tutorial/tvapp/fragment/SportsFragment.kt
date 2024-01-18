package com.tutorial.tvapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tutorial.tvapp.DataModel
import com.tutorial.tvapp.DetailActivity
import com.tutorial.tvapp.ListFragment
import com.tutorial.tvapp.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class SportsFragment : Fragment(){
    lateinit var txtTitle: TextView
    lateinit var txtSubTitle: TextView
    lateinit var txtDescription: TextView
    lateinit var imgBanner: ImageView
    lateinit var listFragment: ListFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }
    fun init(view: View) {

        imgBanner = view.findViewById(R.id.img_banner)
        txtTitle = view.findViewById(R.id.title)
        txtSubTitle = view.findViewById(R.id.subtitle)
        txtDescription = view.findViewById(R.id.description)


        listFragment = ListFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.list_fragment, listFragment)
        transaction.commit()


        val gson = Gson()
        val i: InputStream = requireContext().assets.open("HBOMovies.json")
        val br = BufferedReader(InputStreamReader(i))
        val dataList: DataModel = gson.fromJson(br, DataModel::class.java)

        listFragment.bindMoviesData(dataList)

        listFragment.setOnContentSelectedListener {
            updateBanner(it)
        }
        listFragment.setOnItemClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id",it.id)
            startActivity(intent)
        }

    }

    fun updateBanner(dataList: DataModel.Result.Detail) {
        txtTitle.text = dataList.title
        txtDescription.text = dataList.overview


        val url = "https://www.themoviedb.org/t/p/w780" + dataList.backdrop_path
        Glide.with(this).load(url).into(imgBanner)
    }

}