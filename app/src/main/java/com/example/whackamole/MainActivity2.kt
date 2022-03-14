package com.example.whackamole

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.whackamole.databinding.ActivityMain2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Integer.min
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.concurrent.timer
import kotlin.random.Random

private lateinit var x: ActivityMain2Binding
class MainActivity2 : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
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

        x.progressBar.progress = 100
        x.progressBar.max = 100
        x.progressBar.isIndeterminate = false
        //init var
        var score = 0
        var difficulty = 0
        val goombas: ArrayList<Goomba> = arrayListOf(
            Goomba(x.imageView), Goomba(x.imageView2), Goomba(x.imageView3), Goomba(x.imageView4),
            Goomba(x.imageView5), Goomba(x.imageView6), Goomba(x.imageView7), Goomba(x.imageView8),
            Goomba(x.imageView9)
        )
        //init animations
        val shrink = ScaleAnimation(
            1.0f,
            0f,
            1.0f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        shrink.duration = 300
        val expand = ScaleAnimation(
            0f,
            1.0f,
            0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        expand.duration = 300
        goombas.forEachIndexed { i, goomba ->
            goomba.image.visibility = INVISIBLE
            //it.key.startAnimation(shrinkInstant)
            goomba.image.load(R.drawable.goombagif)
            fun addImage() {
                val iv = ImageView(this)
                //iv.id = View.generateViewId()
                iv.load(R.drawable.goombagif)
                iv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                if (score <= 2000) {
                    x.linearLayout1.addView(iv)
                }
                else if (score <= 4000) {
                    x.linearLayout2.addView(iv)
                }
            }

            goomba.image.setOnClickListener {
                if (goomba.state == State.VIS) {
                    score += 100
                    addImage()
                    x.score.text =  "Score: $score points"
                    val imageView = it as ImageView
                    goomba.state = State.SQUASHED
                    imageView.load(R.drawable.squashed)
                    GlobalScope.launch{
                        delay(500)
                        goomba.image.startAnimation(shrink)
                        goomba.state = State.INVIS
                        goomba.image.visibility = INVISIBLE
                    }
                }
            }
        }

        fun respawn(num: Int) {
            val exclude = arrayListOf<Int>()
            goombas.forEachIndexed { i, goomba ->
                if (goomba.state != State.INVIS) {
                    exclude.add(i)
                }
                if (goomba.state == State.VIS) {
                    goomba.image.startAnimation(shrink)
                    goomba.state = State.INVIS
                    goomba.image.visibility = INVISIBLE
                }
            }
            for (j in 0 .. num) {
                var rand = Random.nextInt(0,goombas.size)
                while (exclude.contains(rand)) {
                    rand = Random.nextInt(0,goombas.size)
                }
                exclude.add(rand)
                goombas[rand].image.load(R.drawable.goombagif)
                goombas[rand].state = State.VIS
                goombas[rand].image.visibility = VISIBLE
                goombas[rand].image.startAnimation(expand)
            }
        }

        lifecycleScope.launch {
            var secondsPassed = 0
            val factor = 100.0/60000
            while (secondsPassed < 60) {
                val secondsLeft = 60 - secondsPassed
                secondsPassed++

                Log.d("timer", "secondsPassed: $secondsPassed")
                Log.d("timer", "secondsLeft: $secondsLeft")

                x.time.text = "0:$secondsLeft"



                val percentage = secondsLeft * 1000 * factor
                x.progressBar.progress = percentage.toInt()
                if (secondsPassed % 2 == 0) {
                    val numGoombas = min(secondsPassed / 10, goombas.size/2 - 2)
                    respawn(numGoombas)
                }
                delay(1000)
            }

            goombas.forEach {
                    it.image.visibility = INVISIBLE
                }
                x.progressBar.progress = 0
                //play again / return to menu launcher
                val alertDialog: AlertDialog? = x.root.let {
                    val builder = AlertDialog.Builder(this@MainActivity2)
                    builder.apply {
                        setPositiveButton("Yes",
                            DialogInterface.OnClickListener { _, _ ->
                                recreate()
                            })
                        setNegativeButton("No",
                            DialogInterface.OnClickListener { _, _ ->
                                val sendBack = Intent(this@MainActivity2, MainActivity::class.java)
                                sendBack.putExtra("score",score)
                                setResult(RESULT_OK,sendBack)
                                finish()
                            })
                        setMessage("Play Again?")
                        setTitle("Game Over. Your score was $score!")
                    }
                    builder.create()
                }
                alertDialog?.show()
        }
//        fun onImageClick (view: View) {
//            val imageView = view as ImageView
//            GlobalScope.launch{
//                imageView.load(R.drawable.squashed)
//                //delay(5000)
//                imageView.visibility = INVISIBLE
//            }
//        }
    }
}