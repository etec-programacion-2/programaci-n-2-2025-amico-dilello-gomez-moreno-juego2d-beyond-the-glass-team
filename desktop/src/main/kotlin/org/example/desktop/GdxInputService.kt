package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.example.core.GameAction
import org.example.core.InputService

class GdxInputService : InputService {

    override fun start() {}
    override fun stop() {}

    /**
     * Devuelve un Set de todas las acciones que están ocurriendo actualmente.
     */
    override fun getActions(): Set<GameAction> {
        val actions = mutableSetOf<GameAction>()

        // Ya no hay "return" aquí. Comprueba todas las teclas.
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            actions.add(GameAction.JUMP)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            actions.add(GameAction.MOVE_LEFT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            actions.add(GameAction.MOVE_RIGHT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            actions.add(GameAction.SWITCH_DIMENSION)
        }

        return actions
    }
}
