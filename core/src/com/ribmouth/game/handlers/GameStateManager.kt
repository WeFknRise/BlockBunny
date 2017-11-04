package com.ribmouth.game.handlers

import com.badlogic.gdx.utils.Disposable
import com.ribmouth.game.Game
import com.ribmouth.game.states.GameState
import com.ribmouth.game.states.PlayState
import java.util.*

/**
 * Created by RibMouth on 2/11/2017.
 */
class GameStateManager(game: Game) : Disposable {
    companion object {
        const val PLAY = 5000
    }

    var game: Game = game
        private set
    private var states: Stack<GameState> = Stack()

    init {
        pushState(PLAY)
    }

    fun update(dt: Float) {
        states.peek().update(dt)
    }

    fun render() {
        states.peek().render()
    }

    override fun dispose() {

    }

    fun resize(width: Int, height: Int) {

    }

    fun getState(state: Int): GameState? {
        if(state == PLAY) return PlayState(this)

        return null
    }

    fun setState(state: Int) {
        popState()
        pushState(state)
    }

    private fun popState() {
        val state = states.pop()
        state.dispose()
    }

    private fun pushState(state: Int) {
        states.push(getState(state))
    }
}