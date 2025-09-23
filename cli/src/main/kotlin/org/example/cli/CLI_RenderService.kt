package org.example.cli

import org.example.core.RenderService

/**
 * Implementación de RenderService para la interfaz de línea de comandos.
 * Esta clase sabe cómo mostrar información en la consola.
 */
class CLI_RenderService : RenderService {
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        println("Dibujando sprite $sprite en la consola.")
    }

    override fun render() {
        println("--- Renderizando nuevo frame de la consola ---")
    }
}
