package com.ribmouth.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.handlers.BBInput
import com.ribmouth.game.handlers.BBInputProcessor
import com.ribmouth.game.handlers.Content
import com.ribmouth.game.handlers.GameStateManager

/**
 * Created by RibMouth on 2/11/2017.
 */
class Game : ApplicationAdapter() {
    companion object {
        const val TITLE: String = "Block Bunny"
        const val WIDTH = 320f
        const val HEIGHT = 240f
        const val SCALE = 2

        var res: Content = Content()
            private set
    }

    lateinit var sb: SpriteBatch
        private set
    lateinit var gsm: GameStateManager
        private set

    override fun create() {
        Gdx.input.inputProcessor = BBInputProcessor()

        res.loadTexture("images/bunny.png", "bunny")
        res.loadTexture("images/crystal.png", "crystal")
        res.loadTexture("images/hud.png", "hud")

        sb = SpriteBatch()
        gsm = GameStateManager(this)
    }

    override fun render() {
        Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.framesPerSecond)
        gsm.update(Gdx.graphics.deltaTime)
        gsm.render()
        BBInput.update()
    }

    override fun dispose() {
        gsm.dispose()
        sb.dispose()
    }

    override fun resize(width: Int, height: Int) {
        gsm.resize(width, height)
    }

    override fun pause() {}

    override fun resume() {}
}