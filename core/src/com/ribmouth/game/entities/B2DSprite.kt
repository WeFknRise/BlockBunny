package com.ribmouth.game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.ribmouth.game.handlers.Animation
import com.ribmouth.game.handlers.B2DVars.Companion.PPM

/**
 * Created by RibMouth on 3/11/2017.
 */
open class B2DSprite(body: Body) {
    var body: Body = body
        private set
    protected var animation: Animation = Animation()

    var width: Float = 0f
        private set
    var height: Float = 0f
        private set

    var position: Vector2 = body.position
        get() = body.position
        private set

    fun setAnimation(regs: Array<TextureRegion>, delay: Float) {
        animation.setFrames(regs, delay)

        if (regs.isNotEmpty()) {
            // same size sprite
            width = regs[0].regionWidth.toFloat()
            height = regs[0].regionHeight.toFloat()
        }
    }

    fun update(dt: Float) {
        animation.update(dt)
    }

    fun render(sb: SpriteBatch) {
        sb.draw(animation.getCurrentFrame(), body.position.x * PPM - width / 2, body.position.y * PPM - height / 2)
    }
}