package com.ribmouth.game.handlers

import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * Created by RibMouth on 3/11/2017.
 */
class Animation {
    private var frames: Array<TextureRegion>? = null
    private var time: Float = 1.0f
    private var delay: Float = 1 / 12f
    private var currentFrame: Int = 1
    var timesPlayed: Int = 0
        private set

    constructor(){}

    constructor(frames: Array<TextureRegion>) {
        setFrames(frames, 1 / 12f)
    }

    constructor(frames: Array<TextureRegion>, delay: Float) {
        setFrames(frames, delay)
    }

    fun setFrames(frames: Array<TextureRegion>, delay: Float) {
        this.frames = frames
        this.delay = delay
        time = 0f
        currentFrame = 0
        timesPlayed = 0
    }

    fun update(dt: Float) {
        //it's not making any sense to update if frames is null, or has zero insize
        if (frames == null || frames?.size == 0)
            return

        if (delay <= 0f) return
        time += dt
        while (time >= delay) {
            step()
        }
    }

    private fun step() {
        time -= delay
        currentFrame++
        if(currentFrame == frames!!.size) {
            currentFrame = 0
            timesPlayed++
        }
    }

    fun getCurrentFrame(): TextureRegion? {
        if (frames == null || frames?.size == 0) {
            return null
        } else {
            return frames!![currentFrame]
        }
    }
}