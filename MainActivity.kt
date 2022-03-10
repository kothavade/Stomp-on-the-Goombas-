package com.example.addingviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.addingviews.databinding.ActivityMainBinding

private lateinit var  x: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        x = ActivityMainBinding.inflate(layoutInflater)
        setContentView(x.root)

        val textView = TextView(this)
        textView.id = View.generateViewId()
        textView.text = "Hello World"

        val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        textView.layoutParams = params

        x.layout.addView(textView)

        val constraintSet = ConstraintSet()
        constraintSet.clone(x.layout)

        constraintSet.connect(textView.id,ConstraintSet.TOP,x.layout.id,ConstraintSet.TOP)
        constraintSet.connect(textView.id,ConstraintSet.BOTTOM,x.layout.id,ConstraintSet.BOTTOM)
        constraintSet.connect(textView.id,ConstraintSet.RIGHT,x.layout.id,ConstraintSet.RIGHT)
        constraintSet.connect(textView.id,ConstraintSet.LEFT,x.layout.id,ConstraintSet.LEFT)

        constraintSet.setVerticalBias(textView.id,.3f)
        constraintSet.setHorizontalBias(textView.id,.75f)

        constraintSet.applyTo(x.layout)




    }
}