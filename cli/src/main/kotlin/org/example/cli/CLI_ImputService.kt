package org.example.cli

import org.example.core.InputService
import org.example.core.PlayerAction

/**
 * Implementación de InputService para la CLI.
 * Simula las entradas del usuario en un entorno de consola.
 */
class CLI_InputService : InputService {
    // Se implementa el nuevo método 'isActionPressed'.
    // En la consola, siempre devolverá false.
    override fun isActionPressed(action: PlayerAction): Boolean {
        return false 
    }

    override fun getMousePosition(): Pair<Float, Float> {
        return Pair(0.0f, 0.0f)
    }
}
