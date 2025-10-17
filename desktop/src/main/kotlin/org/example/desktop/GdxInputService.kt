package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.example.core.InputService
import org.example.core.PlayerAction

// Implementación de InputService para el entorno de escritorio.
class GdxInputService : InputService {

    override fun isActionPressed(action: PlayerAction): Boolean {
        // Mapea cada PlayerAction a una o más teclas físicas.
        val keyCode = when (action) {
            PlayerAction.MOVE_LEFT -> Input.Keys.A
            PlayerAction.MOVE_RIGHT -> Input.Keys.D
            PlayerAction.JUMP -> Input.Keys.SPACE
            
            // --- CASOS AÑADIDOS ---
            // Aquí le enseñamos al 'when' las nuevas acciones.
            PlayerAction.MOVE_LEFT_ALT -> Input.Keys.LEFT
            PlayerAction.MOVE_RIGHT_ALT -> Input.Keys.RIGHT
            PlayerAction.JUMP_ALT -> Input.Keys.SHIFT_RIGHT
        }
        return Gdx.input.isKeyPressed(keyCode)
    }

    override fun getMousePosition(): Pair<Float, Float> {
        return Pair(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
    }
}
