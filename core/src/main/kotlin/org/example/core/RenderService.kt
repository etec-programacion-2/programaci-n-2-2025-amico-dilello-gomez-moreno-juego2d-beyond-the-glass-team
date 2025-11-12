package org.example.core

/**
 * Interfaz para los servicios de renderizado (SOLID: Inversión de Dependencias).
 * Define el "contrato" de CÓMO el 'core' (MiJuego) espera que se dibuje el mundo.
 * No sabe si se dibuja en consola (CliRenderService) o en una ventana (GdxRenderService).
 *
 * ---
 * @see "Issue BTG-002: Diseño de la arquitectura de servicios (DIP)."
 * ---
 */
interface RenderService {
    /**
     * MÉTODO CENTRAL. Recibe el estado completo del mundo (WorldState)
     * y es responsable de dibujarlo en la pantalla.
     * (Relacionado con BTG-008: Bucle de juego)
     */
    fun renderWorld(worldState: WorldState) 
    
    // --- Métodos antiguos (podrían ser para un renderizado más simple o CLI) ---
    // (No utilizados por GdxRenderService)
    fun drawSprite(sprite: Any, x: Float, y: Float) 
    fun render()
}