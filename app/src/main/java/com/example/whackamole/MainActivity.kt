package com.example.whackamole

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.whackamole.databinding.ActivityMainBinding

//get score: https://developer.android.com/training/basics/intents/result
private lateinit var z: ActivityMainBinding
var highscore = 0
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        z = ActivityMainBinding.inflate(layoutInflater)
        setContentView(z.root)

        val letsPlay = MediaPlayer.create(this,R.raw.letsplay)
        var background = MediaPlayer.create(this,R.raw.wii)
        background.isLooping = true
        background.start()

        val r = Runnable {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
        val handler = Handler()


        z.highscore.text = "High Score: $highscore"
        z.play.setOnClickListener{
            background?.release()
            background = null
            letsPlay.start()
            handler.postDelayed(r,100)
        }


    }

    override fun onResume() {
        super.onResume()
        Log.d("tag", "activity result: highscore is $highscore")
        z.highscore.text = "High Score: $highscore"
    }
}