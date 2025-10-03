// Contenido CORREGIDO para GdxInputService.kt (Módulo desktop)
package org.example.desktop

import com.badlogic.gdx.Gdx
import org.example.core.InputKeys
import org.example.core.InputService

// Implementación de InputService para el entorno de escritorio.
class GdxInputService : InputService {
    override fun isKeyPressed(keyCode: Int): Boolean {
        // Mapeo de códigos abstractos (InputKeys) a códigos concretos de LibGDX.
        return when (keyCode) {
            InputKeys.LEFT -> Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)
            InputKeys.RIGHT -> Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)
            InputKeys.SPACE -> Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)
            else -> false
        }
    }

    override fun getMousePosition(): Pair<Float, Float> {
        return Pair(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
    }
}