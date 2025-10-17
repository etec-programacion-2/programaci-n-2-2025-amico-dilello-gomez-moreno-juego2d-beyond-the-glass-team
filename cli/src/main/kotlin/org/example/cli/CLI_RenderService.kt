package org.example.cli

import org.example.core.Dimension
import org.example.core.RenderService
import org.example.core.WorldState

class CLI_RenderService : RenderService {

    // MÉTODO CENTRAL ACTUALIZADO: Dibuja el estado del mundo en la consola.
    override fun renderWorld(worldState: WorldState) {
        val width = 50
        val height = 20
        val grid = Array(height) { CharArray(width) { ' ' } }

        // Dibuja las plataformas
        worldState.platforms.forEach { platform ->
            val char = if (platform.tangibleInDimension == worldState.currentDimension) '#' else '.'
            for (y in platform.position.y.toInt().coerceIn(0, height - 1) until (platform.position.y + platform.size.y).toInt().coerceIn(0, height - 1)) {
                for (x in platform.position.x.toInt().coerceIn(0, width - 1) until (platform.position.x + platform.size.x).toInt().coerceIn(0, width - 1)) {
                    if (x < width && y < height) grid[y][x] = char
                }
            }
        }

        // Dibuja al jugador
        val player = worldState.player
        val pX = player.position.x.toInt().coerceIn(0, width - 1)
        val pY = player.position.y.toInt().coerceIn(0, height - 1)
        if (pX < width && pY < height) grid[pY][pX] = 'P'

        // Imprime el grid en la consola
        println("\u001b[H\u001b[2J") // Limpia la consola
        println("--- BEYOND THE GLASS (CLI) --- Dimensión: ${worldState.currentDimension} ---")
        for (i in grid.indices.reversed()) {
            println(grid[i].joinToString(""))
        }
        println("Controles: A (Izquierda), D (Derecha), W/Espacio (Saltar), S (Cambiar Dimensión), Q (Salir)")
    }

    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}