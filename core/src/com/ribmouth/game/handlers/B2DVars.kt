package com.ribmouth.game.handlers

/**
 * Created by RibMouth on 3/11/2017.
 */
class B2DVars {
    companion object {
        const val PPM: Float = 100f //Pixels Per Meter

        //Category bits (don't use default 1)
        //7 would be 0000 0000 0000 0111
        const val BIT_GROUND: Short = 2 // 0000 0000 0000 0010
        const val BIT_BOX: Short = 4 // 0000 0000 0000 0100
        const val BIT_BALL: Short = 8 // 0000 0000 0000 1000
    }
}