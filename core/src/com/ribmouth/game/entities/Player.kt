package com.ribmouth.game.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.ribmouth.game.Game

/**
 * Created by RibMouth on 3/11/2017.
 */
class Player(body: Body) : B2DSprite(body) {
    private var numCrystals: Int = 0
    var totalCrystals: Int = 0
        private set(value) { totalCrystals = value }
    var died: Boolean = false
        private set

    init {
        val texture = Game.res.getTexture("bunny")
        val sprites: Array<TextureRegion> = TextureRegion.split(texture, 32, 32)[0]
        setAnimation(sprites, 1 / 12f)
    }

    fun collectCrystal() {
        numCrystals++
    }

    fun getNumCrystals(): Int {
        return numCrystals
    }

    fun makeDirectDie() {
        died = true
    }
}