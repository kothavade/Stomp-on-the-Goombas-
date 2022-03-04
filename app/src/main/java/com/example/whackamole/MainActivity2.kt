package com.example.whackamole

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.whackamole.databinding.ActivityMain2Binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

//https://stackoverflow.com/questions/41209896/onclicklistener-to-be-executed-only-once

private lateinit var x: ActivityMain2Binding
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        x = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(x.root)
        val imageLoader = ImageLoader.Builder(this)
            .components {
                add(ImageDecoderDecoder.Factory())
            }
        .build()
        Coil.setImageLoader(imageLoader)

        var score = 0
        val images: ArrayList<ImageView> = arrayListOf(x.imageView, x.imageView2, x.imageView3, x.imageView4, x.imageView5, x.imageView6, x.imageView7, x.imageView8, x.imageView9)
        lifecycleScope.launch {
        images.forEach { it ->
            it.visibility = INVISIBLE
            it.load(R.drawable.goombagif)
            it.setOnClickListener {
                val imageView = it as ImageView
                var clickable = true
                if(it.visibility== VISIBLE){
                    //Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch{
                        if(clickable) {
                            score += 100
                            clickable = false
                        }
                        x.score.text=score.toString()
                        imageView.load(R.drawable.squashed)
                        delay(500)
                        it.visibility = INVISIBLE
                        imageView.load(R.drawable.goombagif)
                        clickable = true
                    }
                }
            }
        }
            }
        lifecycleScope.launch {
            var i = 0
            var prevImg = 0

            while (i<60){
                i++
                x.time.text = i.toString()
                delay(1000)
                if (i%2==0){
                    val r = Random()
                    val randomInt = r.nextInt(8)
                    images[prevImg].visibility = INVISIBLE
                    images[randomInt].visibility = VISIBLE
                    prevImg = randomInt
                }
            }
        }

    }
}