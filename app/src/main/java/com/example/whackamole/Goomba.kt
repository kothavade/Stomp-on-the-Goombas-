package com.example.whackamole

import android.widget.ImageView

enum class State {
    VIS, INVIS, SQUASHED
}

data class Goomba(
    val image: ImageView,
    var state: Enum<State> = State.INVIS
)
