package org.example.core

/**
 * DIP: Define la interfaz de Renderizado. El CORE solo pide dibujar, sin saber CÓMO se dibuja.
 */
interface RenderService {
    fun drawSprite(sprite: Any, x: Float, y: Float)
    fun render()
}