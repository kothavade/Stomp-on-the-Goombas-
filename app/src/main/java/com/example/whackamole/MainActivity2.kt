package com.example.whackamole

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.Coil
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.whackamole.databinding.ActivityMain2Binding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Integer.min
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

            goomba.image.setOnClickListener {
                if (goomba.state == State.VIS) {
                    score += 100
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

        object : CountDownTimer(61000, 1000) {
            val factor = 100.0/60000
            override fun onTick(millis: Long) {
                val secondsLeft = millis.toDouble()/1000.0
                val secondsPassed = 60 - secondsLeft.toInt()
                x.time.text = "0:${secondsLeft.toInt()}"


                val percentage = secondsLeft * 1000 * factor
                x.progressBar.progress = percentage.toInt()

                if (secondsPassed % 2 == 0) {
                    val numGoombas = min(secondsPassed / 10, goombas.size/2 - 2)
                    respawn(numGoombas)
                }
            }

            override fun onFinish() {
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
        }.start()
        fun onImageClick (view: View) {
            val imageView = view as ImageView
            GlobalScope.launch{
                imageView.load(R.drawable.squashed)
                //delay(5000)
                imageView.visibility = INVISIBLE
            }
        }
    }
}