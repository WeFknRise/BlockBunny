package com.ribmouth.game.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.ribmouth.game.Game

/**
 * Created by RibMouth on 4/11/2017.
 */
class Crystal(body: Body) : B2DSprite(body) {
    init {
        val tex = Game.res.getTexture("crystal")
        val sprites = TextureRegion.split(tex, 16,16)[0]
        setAnimation(sprites, 1/12f)
    }
}