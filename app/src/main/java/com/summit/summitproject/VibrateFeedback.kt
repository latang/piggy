package com.summit.summitproject

import android.content.Context
import android.media.MediaPlayer
import android.os.Vibrator

class VibrateFeedback(context: Context) {

    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate() {
        //Vibrate
        val pattern = longArrayOf(0, 50, 50, 50)
        vibrator.vibrate(pattern, -1)
    }
}