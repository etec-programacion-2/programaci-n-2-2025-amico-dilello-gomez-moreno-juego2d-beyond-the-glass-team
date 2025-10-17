package org.example.cli

import org.example.core.*

class CLI_RenderService : RenderService {

    private val GRID_WIDTH = 80
    private val GRID_HEIGHT = 25
    private val WORLD_TO_GRID_SCALE = 10.0f

    override fun renderWorld(worldState: WorldState) {
        // Criterio de Aceptación: La pantalla se limpia ANTES de redibujar.
        clearScreen()

        val grid = Array(GRID_HEIGHT) { CharArray(GRID_WIDTH) { ' ' } }
        val currentDim = worldState.currentDimension

        // Dibujar Plataformas
        worldState.platforms.forEach { platform ->
            val (startCol, startRow) = mapWorldToGrid(platform.position.x, platform.position.y)
            val platformGridWidth = (platform.size.x / WORLD_TO_GRID_SCALE).toInt().coerceAtLeast(1)
            for (i in 0 until platformGridWidth) {
                val col = startCol + i
                if (isValid(col, startRow)) {
                    grid[startRow][col] = if (platform.tangibleInDimension == currentDim) '#' else '~'
                }
            }
        }

        // Dibujar Enemigos
        worldState.enemies.forEach { enemy ->
            val (col, row) = mapWorldToGrid(enemy.position.x, enemy.position.y)
            if (isValid(col, row)) {
                grid[row][col] = 'E'
            }
        }

        // Dibujar Jugador
        val (playerCol, playerRow) = mapWorldToGrid(worldState.player.position.x, worldState.player.position.y)
        if (isValid(playerCol, playerRow)) {
            grid[playerRow][playerCol] = 'P'
        }

        // Imprimir la matriz y la información
        grid.forEach { row ->
            println(row.joinToString(""))
        }
        println("Dimensión: $currentDim | Jugador @ (${worldState.player.position.x.toInt()}, ${worldState.player.position.y.toInt()})")
    }

    /**
     * ¡VERSIÓN RESTAURADA QUE SÍ FUNCIONA!
     * Limpia la consola imprimiendo suficientes líneas en blanco.
     */
    private fun clearScreen() {
        repeat(50) {
            println()
        }
    }

    private fun mapWorldToGrid(worldX: Float, worldY: Float): Pair<Int, Int> {
        val col = (worldX / WORLD_TO_GRID_SCALE).toInt()
        val row = GRID_HEIGHT - 1 - (worldY / WORLD_TO_GRID_SCALE).toInt()
        return Pair(col, row)
    }

    private fun isValid(col: Int, row: Int): Boolean {
        return col in 0 until GRID_WIDTH && row in 0 until GRID_HEIGHT
    }

    // Métodos de la interfaz no utilizados
    override fun drawSprite(sprite: Any, x: Float, y: Float) { /* No implementado */ }
    override fun render() { /* No implementado */ }
}