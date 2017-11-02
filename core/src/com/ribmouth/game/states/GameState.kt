package com.ribmouth.game.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.GameStateManager

/**
 * Created by RibMouth on 2/11/2017.
 */
abstract class GameState(gsm: GameStateManager) {
    protected val gsm: GameStateManager = gsm

    protected val game: Game = gsm.game
    protected val sb: SpriteBatch = game.sb

    lateinit protected var cam: OrthographicCamera
    lateinit protected var hudCam: OrthographicCamera
    lateinit protected var camViewport: Viewport
    lateinit protected var hudViewport: Viewport

    init {
        setupCamera(Game.WIDTH, Game.HEIGHT)
        setupViewport(cam, hudCam, Game.WIDTH, Game.HEIGHT)
    }

    abstract fun handleInput()
    abstract fun update(dt: Float)
    abstract fun render()
    abstract fun dispose()
    abstract fun resize(width: Int, height: Int)

    /**
     * Set up cam, and hudCam.
     * If needed to create a different type of camera and viewport, then override and implement it in
     * GameState class.
     */
    open protected fun setupCamera(viewportWidth: Float, viewportHeight: Float) {
        // set up cam
        cam = OrthographicCamera()
        cam.setToOrtho(false, viewportWidth, viewportHeight)

        // set up hud-cam
        hudCam = OrthographicCamera()
        hudCam.setToOrtho(false, viewportWidth, viewportHeight)
    }

    /**
     * Set up camViewport, and hudViewport.
     * If needed to create a different type of viewport, then override and implement it in GameState class.
     */
    open protected fun setupViewport(cam: OrthographicCamera, hudCam: OrthographicCamera, viewportWidth: Float, viewportHeight: Float) {
        camViewport = FitViewport(viewportWidth, viewportHeight, cam)
        hudViewport = FitViewport(viewportWidth, viewportHeight, hudCam)
    }
}