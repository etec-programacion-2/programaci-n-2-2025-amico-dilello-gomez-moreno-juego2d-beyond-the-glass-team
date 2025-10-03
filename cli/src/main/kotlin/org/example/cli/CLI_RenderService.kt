package org.example.cli

import org.example.core.RenderService

/**
 * ADAPTADOR CLI para RenderService.
 * DIP: Implementa la interfaz de RenderService para la consola.
 */
class CLI_RenderService : RenderService {
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        // En un entorno de consola, dibujar un sprite es solo una impresión.
    }
    
    override fun render() {
        // Imprime un separador para simular el cambio de frame.
        println("--- Renderizando nuevo frame de la consola ---")
    }
}
