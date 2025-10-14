package org.example.core

/**
 * Define la interfaz para los servicios de renderizado (Polimorfismo).
 * Ahora incluye el método central para el renderizado del estado del mundo.
 */
interface RenderService {
    // MÉTODO CENTRAL BTG-008: Recibe el estado completo del mundo para dibujar
    fun renderWorld(worldState: WorldState) 
    
    // Métodos originales
    fun drawSprite(sprite: Any, x: Float, y: Float) 
    fun render()
}
