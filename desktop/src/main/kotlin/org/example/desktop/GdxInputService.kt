package org.example.desktop

import com.badlogic.gdx.Gdx
import org.example.core.InputService

// Implementación de InputService para el entorno de escritorio.
class GdxInputService : InputService {
    override fun isKeyPressed(keyCode: Int): Boolean {
        return Gdx.input.isKeyPressed(keyCode)
    }

    override fun getMousePosition(): Pair<Float, Float> {
        return Pair(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
    }
}