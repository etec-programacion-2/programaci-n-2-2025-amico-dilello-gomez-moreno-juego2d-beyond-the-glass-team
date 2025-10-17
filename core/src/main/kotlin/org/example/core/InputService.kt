package org.example.core

// Define la interfaz para los servicios de entrada de datos.
interface InputService {
    // Solo dejamos el método que usa la abstracción 'PlayerAction'.
    fun isActionPressed(action: PlayerAction): Boolean
    fun getMousePosition(): Pair<Float, Float>
}

