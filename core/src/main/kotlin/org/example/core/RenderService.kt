package org.example.core

/**
 * Interfaz para los servicios de renderizado (SOLID: Inversión de Dependencias).
 * Define el "contrato" de CÓMO el 'core' (MiJuego) espera que se dibuje el mundo.
 * No sabe si se dibuja en consola (CliRenderService) o en una ventana (GdxRenderService).
 */
interface RenderService {
    /**
     * MÉTODO CENTRAL. Recibe el estado completo del mundo (WorldState)
     * y es responsable de dibujarlo en la pantalla.
     */
    fun renderWorld(worldState: WorldState) 
    
    // --- Métodos antiguos (podrían ser para un renderizado más simple) ---
    fun drawSprite(sprite: Any, x: Float, y: Float) 
    fun render()
}
