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
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        z = ActivityMainBinding.inflate(layoutInflater)
        setContentView(z.root)


        z.play.setOnClickListener{
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
    }

}