package com.ribmouth.game.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

/**
 * Created by RibMouth on 3/11/2017.
 */
class Content {
    private var textures: HashMap<String, Texture> = HashMap()

    fun loadTexture(path: String, key: String) {
        val texture: Texture = Texture(Gdx.files.internal(path))
        textures.put(key, texture)
    }

    fun getTexture(key: String): Texture? {
        return textures[key]
    }

    fun disposeTexture(key: String) {
        val texture = textures[key]
        if (texture != null) {
            textures.remove(key)
            texture.dispose()
        }
    }
}