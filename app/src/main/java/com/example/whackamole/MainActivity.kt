package com.example.whackamole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.ImageLoader
import coil.decode.GifDecoder
import com.example.whackamole.databinding.ActivityMainBinding
import kotlinx.coroutines.coroutineScope

//get score: https://developer.android.com/training/basics/intents/result
private lateinit var z: ActivityMainBinding
var highscore = 0
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        z = ActivityMainBinding.inflate(layoutInflater)
        setContentView(z.root)


        z.highscore.text = "High Score: $highscore"
        z.play.setOnClickListener{
            val intent = Intent(this,MainActivity2::class.java)
            startActivityForResult(intent,1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.getIntExtra("score", 0)!! > highscore && requestCode == 1) {
            highscore = data.getIntExtra("score", 0)
            z.highscore.text = "High Score: $highscore"
        }
    }

}