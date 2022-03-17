package com.example.whackamole

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
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
        val seconds = AtomicInteger(0)
        val maxSeconds = AtomicInteger(60)
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
                iv.load(R.drawable.coin)
                iv.setPadding(1,0,1,0)
                iv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                if (score <= 1700) {
                    x.linearLayout1.addView(iv)
                }
                else if (score <= 3400) {
                    x.linearLayout2.addView(iv)
                }
            }

            goomba.image.setOnClickListener{
                if (goomba.state == State.VIS) {
                    when (goomba.type) {
                        Type.GOOMBA -> {
                            score += 100
                            addImage()
                            x.score.text = "$score points"
                            val imageView = it as ImageView
                            goomba.state = State.SQUASHED
                            imageView.load(R.drawable.squashed)
                            GlobalScope.launch {
                                delay(500)
                                goomba.image.startAnimation(shrink)
                                goomba.state = State.INVIS
                                goomba.image.visibility = INVISIBLE
                            }
                        }
                        Type.BOMB -> {
                            seconds.addAndGet(10)
                            goomba.image.startAnimation(shrink)
                            goomba.state = State.INVIS
                            goomba.image.visibility = INVISIBLE
                        }
                        Type.MUSHROOM -> {
                            seconds.addAndGet(-10)
                            if (seconds.get() < 0) {
                                maxSeconds.set(60 - seconds.get())
                            }
                            goomba.image.startAnimation(shrink)
                            goomba.state = State.INVIS
                            goomba.image.visibility = INVISIBLE
                        }
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
                val r = Random.nextInt(0,10)
                when (r) {
                    1 -> {
                        goombas[rand].image.load(R.drawable.bomb)
                        goombas[rand].type = Type.BOMB
                    }
                    9 -> {
                        goombas[rand].image.load(R.drawable.mushroom)
                        goombas[rand].type = Type.MUSHROOM
                    }
                    else -> {
                        goombas[rand].image.load(R.drawable.goombagif)
                        goombas[rand].type = Type.GOOMBA
                    }
                }
                goombas[rand].state = State.VIS
                goombas[rand].image.visibility = VISIBLE
                goombas[rand].image.startAnimation(expand)

            }
        }

        lifecycleScope.launch {
            while (seconds.get() < 60) {
                val factor = 100.0/(maxSeconds.get()*1000)
                val secondsLeft = 60 - seconds.getAndIncrement()

                Log.d("timer", "secondsPassed: ${seconds.get()}")
                Log.d("timer", "secondsLeft: $secondsLeft")

                val time = String.format("%02d", secondsLeft)
                x.time.text = "00:$time"



                val percentage = secondsLeft * 1000 * factor
                x.progressBar.progress = percentage.toInt()
                if (seconds.get() % 2 == 0) {
                    val numGoombas = min(seconds.get() / 10, goombas.size/2 - 2)
                    respawn(numGoombas)
                }
                delay(1000)
            }

            goombas.forEach {
                it.image.visibility = INVISIBLE
            }
            x.progressBar.progress = 0
            x.time.text = "00:00"
            //play again / return to menu launcher
            Log.d("tag", "highscore set to $score, it is $highscore")
            if (score > highscore) {
                highscore = score
            }
            val alertDialog: AlertDialog? = x.root.let {
                val builder = AlertDialog.Builder(this@MainActivity2)
                builder.apply {
                    setPositiveButton("Yes",
                        DialogInterface.OnClickListener { _, _ ->
                            recreate()
                        })
                    setNegativeButton("No",
                        DialogInterface.OnClickListener { _, _ ->
                            finish()
                        })
                    setMessage("Play Again?")
                    setTitle("Game Over. Your score was $score!")
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }
}