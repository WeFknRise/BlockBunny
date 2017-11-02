package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.ribmouth.game.handlers.GameStateManager

/**
 * Created by RibMouth on 2/11/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    private var font: BitmapFont = BitmapFont()

    override fun handleInput() {

    }

    override fun update(dt: Float) {

    }

    override fun render() {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = cam.combined
        sb.begin()
        font.draw(sb, "Play state", 100f, 100f)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize(width: Int, height: Int) {

    }
}