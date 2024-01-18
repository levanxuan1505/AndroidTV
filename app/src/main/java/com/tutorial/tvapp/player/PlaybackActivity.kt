package com.tutorial.tvapp.player

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tutorial.tvapp.R
import com.tutorial.tvapp.model.DetailResponse

class PlaybackActivity :FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, MyVideoFragment())
                .commit()
        }
    }
}