package com.example.whackamole

import android.widget.ImageView

enum class State {
    VIS, INVIS, SQUASHED
}
enum class Type {
    GOOMBA, BOMB, MUSHROOM
}

data class Goomba(
    val image: ImageView,
    var state: Enum<State> = State.INVIS,
    var type: Enum<Type> = Type.GOOMBA
)
