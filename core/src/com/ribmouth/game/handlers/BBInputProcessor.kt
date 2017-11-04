package com.ribmouth.game.handlers

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON1
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON2

/**
 * Created by RibMouth on 3/11/2017.
 */
class BBInputProcessor: InputAdapter() {
    override fun keyDown(k: Int): Boolean {
        if(k == Input.Keys.W) {
            BBInput.setKey(BUTTON1, true)
        }
        if(k == Input.Keys.X) {
            BBInput.setKey(BUTTON2, true)
        }
        return true
    }

    override fun keyUp(k: Int): Boolean {
        if(k == Input.Keys.W) {
            BBInput.setKey(BUTTON1, false)
        }
        if(k == Input.Keys.X) {
            BBInput.setKey(BUTTON2, false)
        }
        return true
    }
}