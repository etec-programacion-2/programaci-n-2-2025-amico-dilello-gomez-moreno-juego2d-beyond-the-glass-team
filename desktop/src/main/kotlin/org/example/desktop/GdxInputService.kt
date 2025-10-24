package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.example.core.GameAction
import org.example.core.InputService
import org.example.core.PlayerAction

// Implementación de InputService para el entorno de escritorio.
class GdxInputService : InputService {

    // Estos métodos son requeridos por la nueva interfaz. Por ahora, no necesitan hacer nada.
    override fun start() {}
    override fun stop() {}

    // MÉTODO CENTRAL ACTUALIZADO PARA CUMPLIR CON LA NUEVA INTERFAZ
    override fun getAction(): GameAction {
        // Comprueba las teclas en orden de prioridad y devuelve la primera que encuentre.
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            return GameAction.JUMP
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            return GameAction.MOVE_LEFT
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            return GameAction.MOVE_RIGHT
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            return GameAction.SWITCH_DIMENSION
        }
        
        // Si no se presiona ninguna tecla de acción, devuelve NONE.
        return GameAction.NONE
    }
}
