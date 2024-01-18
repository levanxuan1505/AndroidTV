package com.tutorial.tvapp

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.BrowseFrameLayout
import com.tutorial.tvapp.fragment.LanguagesFagment
import com.tutorial.tvapp.fragment.MoviesFragment
import com.tutorial.tvapp.search.SearchFragment
import com.tutorial.tvapp.fragment.SettingsFragment
import com.tutorial.tvapp.fragment.SportsFragment
import com.tutorial.tvapp.fragment.TvFragment
import com.tutorial.tvapp.search.SearchActivity
import com.tutorial.tvapp.utils.Common
import com.tutorial.tvapp.utils.Constants

class MainActivity : FragmentActivity(), View.OnKeyListener {
    lateinit var navBar: BrowseFrameLayout
    lateinit var fragmentContainer: FrameLayout
    lateinit var btnSearch: TextView
    lateinit var btnHome: TextView
    lateinit var btnTVShows: TextView
    lateinit var btnMovies: TextView
    lateinit var btnSports: TextView
    lateinit var btnSettings: TextView
    lateinit var btnLanguages: TextView
    var SIDE_MENU = false
    var selectedMenu = Constants.MENU_HOME
    lateinit var lastSelectedMenu : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentContainer= findViewById(R.id.container)
        navBar= findViewById(R.id.blfNavBar)

        btnSearch= findViewById(R.id.btn_search)
        btnHome= findViewById(R.id.btn_home)
        btnMovies= findViewById(R.id.btn_movies)
        btnTVShows= findViewById(R.id.btn_tv)
        btnSettings= findViewById(R.id.btn_settings)
        btnSports= findViewById(R.id.btn_sports)
        btnLanguages= findViewById(R.id.btn_languages)

        btnSearch.setOnKeyListener(this)
        btnHome.setOnKeyListener(this)
        btnMovies.setOnKeyListener(this)
        btnTVShows.setOnKeyListener(this)
        btnSettings.setOnKeyListener(this)
        btnSports.setOnKeyListener(this)
        btnLanguages.setOnKeyListener(this)


      lastSelectedMenu= btnHome
        lastSelectedMenu.isActivated = true

        changeFragment(HomeFragment())
    }
    fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
       closeMennu()
    }

    override fun onKey(view: View?, i: Int, key_event: KeyEvent?): Boolean {
     when (i) {
         KeyEvent.KEYCODE_DPAD_LEFT -> {
             if(!SIDE_MENU) {
                 SIDE_MENU = true
                 openMennu()
                 switchToLastSelectedMenu()
             }
         }
         KeyEvent.KEYCODE_DPAD_CENTER -> {
             lastSelectedMenu.isActivated = false
             view?.isActivated=true
             lastSelectedMenu= view!!
             when (view.id) {
                 R.id.btn_search-> {
                     if (selectedMenu != Constants.MENU_SEARCH) {
                         selectedMenu = Constants.MENU_SEARCH
                         val intent = Intent(this, SearchActivity::class.java)
                         startActivity(intent)
                     }
                 }
                 R.id.btn_home->  {
                     selectedMenu = Constants.MENU_HOME
                     changeFragment(HomeFragment())
                 }

                 R.id.btn_movies->  {
                     selectedMenu = Constants.MENU_MOVIES
                     changeFragment(MoviesFragment())
                 }

                 R.id.btn_tv->  {
                     selectedMenu = Constants.MENU_TV
                     changeFragment(TvFragment())
                 }

                 R.id.btn_sports->  {
                     selectedMenu = Constants.MENU_SPORTS
                     changeFragment(SportsFragment())
                 }

                 R.id.btn_settings->  {
                     selectedMenu = Constants.MENU_SETTINGS
                     changeFragment(SettingsFragment())
                 }
                 R.id.btn_languages->  {
                     selectedMenu = Constants.MENU_LANGUAGES
                     changeFragment(LanguagesFagment())
                 }
             }
         }
     }
        return false
    }


    override fun onKeyDown(keyCode: Int, event : KeyEvent?): Boolean {

       if( keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && SIDE_MENU)  {
                    closeMennu()
                    SIDE_MENU = false
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun onBackPressed() {
        if(!SIDE_MENU){
            switchToLastSelectedMenu()
            openMennu()
            SIDE_MENU= true
        }
        else {
            super.onBackPressed()
        }

    }
    fun switchToLastSelectedMenu(){
        when (selectedMenu) {
            Constants.MENU_SEARCH -> {
                btnSearch.requestFocus()
            }
            Constants.MENU_HOME -> {
                btnHome.requestFocus()
            }
            Constants.MENU_MOVIES -> {
                btnMovies.requestFocus()
            }
            Constants.MENU_TV -> {
                btnTVShows.requestFocus()
            }
            Constants.MENU_SPORTS -> {
                btnSports.requestFocus()
            }
            Constants.MENU_SETTINGS -> {
                btnSettings.requestFocus()
            }
            Constants.MENU_LANGUAGES -> {
                btnLanguages.requestFocus()
            }
        }
    }
    fun openMennu() {
        val animSlide : Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        navBar.startAnimation(animSlide)
        navBar.requestLayout()
        navBar.layoutParams.width= Common.getWidthInPercent(this,16)
    }
    fun closeMennu() {
   //     val animSlide : Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
   //     navBar.startAnimation(animSlide)
        navBar.requestLayout()
        navBar.layoutParams.width= Common.getWidthInPercent(this,5)
        fragmentContainer.requestFocus()
        SIDE_MENU= false
    }
}
