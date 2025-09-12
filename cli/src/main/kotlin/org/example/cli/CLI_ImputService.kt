package org.example.cli

import org.example.core.InputService

// Implementación de InputService para la CLI.
class CLI_InputService : InputService {
    override fun isKeyPressed(keyCode: Int): Boolean {
        return false // La CLI no tiene eventos de tecla en tiempo real.
    }

    override fun getMousePosition(): Pair<Float, Float> {
        return Pair(0.0f, 0.0f)
    }
}
