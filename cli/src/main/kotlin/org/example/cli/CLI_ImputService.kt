package org.example.cli

import org.example.core.InputService

/**
 * Implementación de InputService para la CLI.
 * Simula las entradas del usuario en un entorno de consola.
 */
class CLI_InputService : InputService {
    override fun isKeyPressed(keyCode: Int): Boolean {
        // En un entorno de consola, no hay una forma sencilla de detectar
        // pulsaciones de teclas en tiempo real, por lo que se devuelve false.
        return false 
    }

    override fun getMousePosition(): Pair<Float, Float> {
        return Pair(0.0f, 0.0f)
    }
}