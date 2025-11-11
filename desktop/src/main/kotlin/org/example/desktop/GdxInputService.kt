package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.example.core.GameAction
import org.example.core.InputService

/**
 * Implementación CONCRETA de la interfaz 'InputService' (del 'core')
 * usando la biblioteca LibGDX.
 *
 * Su trabajo es TRADUCIR las teclas físicas de LibGDX (ej. Input.Keys.W)
 * en ACCIONES abstractas del juego (ej. GameAction.JUMP).
 */
class GdxInputService : InputService {

    // No necesitan hacer nada en este caso
    override fun start() {}
    override fun stop() {}

    /**
     * Devuelve un Set de todas las acciones que están ocurriendo actualmente
     * (teclas presionadas) con la nueva distribución de teclas.
     */
    override fun getActions(): Set<GameAction> {
        val actions = mutableSetOf<GameAction>()

        // --- NUEVA DISTRIBUCIÓN DE TECLAS (WASD + Flechas) ---

        // Salto: W o Flecha Arriba
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            actions.add(GameAction.JUMP)
        }
        // Mover Izquierda: A o Flecha Izquierda
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            actions.add(GameAction.MOVE_LEFT)
        }
        // Mover Derecha: D o Flecha Derecha
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            actions.add(GameAction.MOVE_RIGHT)
        }
        // Cambiar Dimensión: Shift (Izquierdo o Derecho)
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            actions.add(GameAction.SWITCH_DIMENSION)
        }
        // Atacar: Espacio
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            actions.add(GameAction.ATTACK)
        }

        // Devuelve el conjunto de acciones activas
        return actions
    }
}
