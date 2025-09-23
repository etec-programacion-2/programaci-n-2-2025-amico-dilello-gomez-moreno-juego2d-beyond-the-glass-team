package org.example.core

// Define la interfaz para los servicios de renderizado.
// Esto desacopla la lógica de dibujo de la tecnología de UI.
interface RenderService {
    fun drawSprite(sprite: Any, x: Float, y: Float)
    fun render()
}
