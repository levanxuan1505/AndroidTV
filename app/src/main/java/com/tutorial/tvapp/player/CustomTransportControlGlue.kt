package com.tutorial.tvapp.player

import android.content.Context
import android.view.KeyEvent
import android.view.View
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow

class CustomTransportControlGlue (
    context: Context,
    playerAdapter: BasicMediaPlayerAdapter
): PlaybackTransportControlGlue<BasicMediaPlayerAdapter>(context,playerAdapter) {
    //    Primary Actions
    private val forwardAction = PlaybackControlsRow.FastForwardAction(context)
    private val rewindAction = PlaybackControlsRow.RewindAction(context)
    private val nextAction = PlaybackControlsRow.SkipNextAction(context)
    private val previousAction = PlaybackControlsRow.SkipPreviousAction(context)

    init {
        isSeekEnabled = true
    }

    override fun onCreatePrimaryActions(primaryActionsAdapter: ArrayObjectAdapter) {
        primaryActionsAdapter.add(previousAction)
        primaryActionsAdapter.add(rewindAction)
        super.onCreatePrimaryActions(primaryActionsAdapter)
        primaryActionsAdapter.add(forwardAction)
        primaryActionsAdapter.add(nextAction)
    }

    override fun onActionClicked(action: Action?) {
        when (action) {
            forwardAction -> playerAdapter.fastForward()
            rewindAction -> playerAdapter.rewind()
            else -> super.onActionClicked(action)
        }
        onUpdateProgress()
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (host.isControlsOverlayVisible || event.repeatCount > 0) {
                return super.onKey(v, keyCode, event)
            }
        }
        if (event != null) {
            return when (keyCode) {
                KeyEvent.KEYCODE_DPAD_RIGHT ->
                    if (event.action != KeyEvent.ACTION_DOWN) false else {
                        onActionClicked(forwardAction)
                        true
                    }

                KeyEvent.KEYCODE_DPAD_LEFT ->
                    if (event.action != KeyEvent.ACTION_DOWN) false else {
                        onActionClicked(rewindAction)
                        true
                    }

                else -> super.onKey(v, keyCode, event)
            }
        }
        return TODO("Provide the return value")
    }
}