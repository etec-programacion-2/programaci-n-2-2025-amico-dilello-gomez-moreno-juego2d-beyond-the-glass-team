package org.example.core

/**
 * DIP: Define la interfaz de Entrada. El CORE pide el estado de una tecla, sin saber cómo se lee.
 */
interface InputService {
    fun isKeyPressed(keyCode: Int): Boolean
    fun getMousePosition(): Pair<Float, Float>
}