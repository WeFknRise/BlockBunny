package com.ribmouth.game.handlers

/**
 * Created by RibMouth on 3/11/2017.
 */
class BBInput {
    companion object {
        const val NUM_KEYS: Int = 2
        const val BUTTON1: Int = 0
        const val BUTTON2: Int = 1

        var keys: Array<Boolean> = Array(NUM_KEYS, { i -> false })
        var pKeys: Array<Boolean> = Array(NUM_KEYS, { i -> false })

        fun update() {
            for (i in 0 until NUM_KEYS) {
                pKeys[i] = keys[i]
            }
        }

        fun setKey(i: Int, b: Boolean) {
            keys[i] = b
        }

        fun isDown(i: Int): Boolean {
            return keys[i]
        }

        fun isPressed(i: Int): Boolean{
            return keys[i] && !pKeys[i]
        }
    }
}