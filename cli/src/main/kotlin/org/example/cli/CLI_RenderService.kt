package org.example.cli

import org.example.core.*

class CLI_RenderService : RenderService {

    // --- Configuración de la Ventana de Consola ---
    private val GRID_WIDTH = 80
    private val GRID_HEIGHT = 25
    private val WORLD_TO_GRID_SCALE = 10.0f // Ajustado para que el movimiento sea más visible

    /**
     * Faltaba definir bien el clean()
     * Dibuja el estado actual del mundo en la consola.
     */
    override fun renderWorld(worldState: WorldState) {
        // Criterio de Aceptación: La pantalla se limpia ANTES de redibujar.
        clearScreen()

        val grid = Array(GRID_HEIGHT) { CharArray(GRID_WIDTH) { ' ' } }
        val currentDim = worldState.currentDimension

        // Dibujar Plataformas (#: tangible, ~: intangible)
        worldState.platforms.forEach { platform ->
            // Se dibuja la plataforma completa, no solo su punto de inicio
            val (startCol, startRow) = mapWorldToGrid(platform.position.x, platform.position.y)
            val platformGridWidth = (platform.size.x / WORLD_TO_GRID_SCALE).toInt()
            for (i in 0 until platformGridWidth) {
                val col = startCol + i
                if (isValid(col, startRow)) {
                    grid[startRow][col] = if (platform.tangibleInDimension == currentDim) '#' else '~'
                }
            }
        }

        // Dibujar Enemigos (E)
        worldState.enemies.forEach { enemy ->
            val (col, row) = mapWorldToGrid(enemy.position.x, enemy.position.y)
            if (isValid(col, row)) {
                grid[row][col] = 'E'
            }
        }

        // Dibujar Jugador (P) - Se dibuja al final para que siempre sea visible
        val (playerCol, playerRow) = mapWorldToGrid(worldState.player.position.x, worldState.player.position.y)
        if (isValid(playerCol, playerRow)) {
            grid[playerRow][playerCol] = 'P'
        }

        // Imprimir la matriz completa a la consola
        grid.forEach { row ->
            println(row.joinToString(""))
        }

        // Imprimir información de estado debajo del juego
        println("Dimensión: $currentDim | Jugador @ (${worldState.player.position.x.toInt()}, ${worldState.player.position.y.toInt()})")
    }

    // --- MÉTODOS AUXILIARES ---

    /**
     * ¡LA FUNCIÓN QUE FALTABA!
     * Limpia la consola usando secuencias de escape ANSI.
     * Funciona en la mayoría de terminales modernas (Linux, macOS, Windows Terminal).
     */
    private fun clearScreen() {
    repeat(50) {
        println()
    }

    /**
     * Mapea coordenadas del mundo a la grilla de la consola.
     */
    private fun mapWorldToGrid(worldX: Float, worldY: Float): Pair<Int, Int> {
        val col = (worldX / WORLD_TO_GRID_SCALE).toInt()
        // Invierte el eje Y para que (0,0) esté abajo a la izquierda en el mundo del juego
        val row = GRID_HEIGHT - 1 - (worldY / WORLD_TO_GRID_SCALE).toInt()
        return Pair(col, row)
    }

    /**
     * Verifica si una coordenada de la grilla está dentro de los límites.
     */
    private fun isValid(col: Int, row: Int): Boolean {
        return col in 0 until GRID_WIDTH && row in 0 until GRID_HEIGHT
    }

    // --- Métodos de la interfaz no utilizados en esta implementación ---
    override fun drawSprite(sprite: Any, x: Float, y: Float) { /* No implementado */ }
    override fun render() { /* No implementado */ }
}