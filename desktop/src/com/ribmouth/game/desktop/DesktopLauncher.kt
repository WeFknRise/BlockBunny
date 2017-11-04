package com.ribmouth.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.ribmouth.game.Game
import com.ribmouth.game.Game.Companion.HEIGHT
import com.ribmouth.game.Game.Companion.SCALE
import com.ribmouth.game.Game.Companion.TITLE
import com.ribmouth.game.Game.Companion.WIDTH

/**
 * Created by RibMouth on 2/11/2017.
 */
object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = TITLE
        config.width = WIDTH.toInt() * SCALE
        config.height = HEIGHT.toInt() * SCALE
        LwjglApplication(Game(), config)
    }
}