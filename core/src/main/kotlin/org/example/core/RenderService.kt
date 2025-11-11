package org.example.core

/**
 * Define la interfaz para los servicios de renderizado (Polimorfismo).
 * Ahora incluye el método central para el renderizado del estado del mundo.
 *
 * (ACTUALIZADO: Se añadió deltaTime a renderWorld)
 */
interface RenderService {
    
    /**
     * Dibuja el estado completo del mundo.
     * @param worldState El snapshot de todos los objetos del juego.
     * @param deltaTime El tiempo desde el último fotograma (para animaciones).
     */
    fun renderWorld(worldState: WorldState, deltaTime: Float) 
    
    // Métodos originales
    fun drawSprite(sprite: Any, x: Float, y: Float) 
    fun render()
}