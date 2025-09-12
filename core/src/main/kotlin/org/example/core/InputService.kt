package org.example.core

// Define la interfaz para los servicios de entrada de datos.
// Esto permite cambiar entre entrada de teclado/mouse real o simulada.
interface InputService {
    fun isKeyPressed(keyCode: Int): Boolean
    fun getMousePosition(): Pair<Float, Float>
}
