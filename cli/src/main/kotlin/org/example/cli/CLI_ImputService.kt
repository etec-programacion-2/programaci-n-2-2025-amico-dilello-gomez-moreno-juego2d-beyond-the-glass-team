package org.example.cli

import org.example.core.InputKeys
import org.example.core.InputService

/**
 * ADAPTADOR CLI para InputService.
 * DIP: Implementa la interfaz de InputService para la consola.
 */
class CLI_InputService : InputService {
    // En la consola, solo se puede simular la presión de teclas, no es interactivo.
    override fun isKeyPressed(keyCode: Int): Boolean {
        // Para la demo CLI, simulamos que no se presiona nada en tiempo de ejecución.
        // Las interacciones son manejadas en el Main al inicio.
        return false
    }
    
    override fun getMousePosition(): Pair<Float, Float> = Pair(0.0f, 0.0f)
}
