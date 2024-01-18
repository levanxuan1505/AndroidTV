package com.tutorial.tvapp.player

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackSeekDataProvider
import com.tutorial.tvapp.R

class MyVideoFragment :VideoSupportFragment() {
    private lateinit var transportGlue : CustomTransportControlGlue
    private lateinit var fastForwardIndicatorView :View
    private lateinit var rewindIndicatorView :View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val playerGlue = PlaybackTransportControlGlue(activity,MediaPlayerAdapter(activity))
//         playerGlue.host = VideoSupportFragmentGlueHost(this)
//        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback(){
//            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
//                if (glue?.isPrepared == true){
//                    playerGlue.seekProvider = PlaybackSeekDataProvider()
//                    playerGlue.play()
//                }
//            }
//
//        })
//        playerGlue.subtitle = "My Demo Subtitle"
//        playerGlue.title = "My Android Tv Development Tutorial"
//        val uriPath = "https://hdbo.opstream5.com/20230114/29209_df6d21ee/index.m3u8"
//        playerGlue.playerAdapter.setDataSource(Uri.parse(uriPath))

          transportGlue = CustomTransportControlGlue(
              context = requireContext(),
              playerAdapter = BasicMediaPlayerAdapter(requireContext())
          )
        transportGlue.host = VideoSupportFragmentGlueHost(this)
        transportGlue.title = "My Android TV Video From Netflix"
        val uriPath = "https://hdbo.opstream5.com/20230114/29209_df6d21ee/index.m3u8"
        transportGlue.playerAdapter.setDataSource(Uri.parse(uriPath))

        setOnKeyInterceptListener{ view, keyCode, event ->
            if(isControlsOverlayVisible || event.repeatCount > 0) {
                isShowOrHideControlsOverlayOnUserInteraction = true
            } else when (keyCode) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    isShowOrHideControlsOverlayOnUserInteraction = event.action != KeyEvent.ACTION_DOWN
                    if(event.action == KeyEvent.ACTION_DOWN){
                          animateIndicator(fastForwardIndicatorView)
                    }
                }
              KeyEvent.KEYCODE_DPAD_LEFT -> {
                  isShowOrHideControlsOverlayOnUserInteraction = event.action != KeyEvent.ACTION_DOWN
                  if (event.action == KeyEvent.ACTION_DOWN) {
                      animateIndicator(rewindIndicatorView)

                  }
              }
            }
            transportGlue.onKey(view,keyCode,event)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) as ViewGroup
        fastForwardIndicatorView = inflater.inflate(R.layout.view_forward,view,false)
        view.addView(fastForwardIndicatorView)

        rewindIndicatorView = inflater.inflate(R.layout.view_rewind,view,false)
        view.addView(rewindIndicatorView)
        return view
    }
    fun animateIndicator(indicator :View) {
        indicator.animate()
            .withEndAction {
                indicator.isVisible = false
                indicator.alpha = 1F
                indicator.scaleX = 1F
                indicator.scaleY = 1F
            }
            .withStartAction{
                indicator.isVisible = true
            }
            .alpha(0.2F)
            .scaleX(2f)
            .scaleY(2f)
            .setDuration(400)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }


}