package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.example.core.GameAction
import org.example.core.InputService

/**
 * Implementación CONCRETA de la interfaz 'InputService' (del 'core')
 * usando la biblioteca LibGDX.
 *
 * (SOLID: D) Es la implementación que cumple el contrato de 'InputService'.
 * Su trabajo es TRADUCIR las teclas físicas de LibGDX (ej. Input.Keys.W)
 * en ACCIONES abstractas del juego (ej. GameAction.JUMP).
 *
 * ---
 * @see "Issue BTG-002: Diseño de la arquitectura de servicios (InputService)."
 * ---
 */
class GdxInputService : InputService {

    // No necesitan hacer nada en este caso
    override fun start() {}
    override fun stop() {}

    /**
     * Devuelve un Set de todas las acciones que están ocurriendo actualmente
     * (teclas presionadas) con la nueva distribución de teclas.
     * El 'core' (MiJuego) recibe esto y no sabe qué teclas se presionaron.
     */
    override fun getActions(): Set<GameAction> {
        val actions = mutableSetOf<GameAction>()

        // --- NUEVA DISTRIBUCIÓN DE TECLAS (WASD + Flechas) ---

        // --- Relacionado con BTG-006: Movimiento y Salto ---
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

        // --- Relacionado con BTG-009: Cambio de Dimensión ---
        // Cambiar Dimensión: Shift (Izquierdo o Derecho)
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            actions.add(GameAction.SWITCH_DIMENSION)
        }
        
        // --- Relacionado con BTG-012: Sistema de Combate (Ataque) ---
        // Atacar: Espacio
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            actions.add(GameAction.ATTACK)
        }

        // --- (NUEVO) Acción de Salir ---
        // Salir: ESC
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            actions.add(GameAction.QUIT)
        }

        return actions
    }
}