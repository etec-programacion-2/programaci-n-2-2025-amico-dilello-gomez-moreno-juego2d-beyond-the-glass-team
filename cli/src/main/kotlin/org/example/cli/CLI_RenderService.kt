package org.example.cli

import org.example.core.*

class CLI_RenderService : RenderService {

    // --- Configuración de la Ventana de Consola ---
    private val GRID_WIDTH = 80
    private val GRID_HEIGHT = 25
    private val WORLD_TO_GRID_SCALE = 2.0f 

    override fun renderWorld(worldState: WorldState) {
        // Criterio de Aceptación: La pantalla se limpia.
        clearScreen()

        val grid = Array(GRID_HEIGHT) { CharArray(GRID_WIDTH) { ' ' } }
        val currentDim = worldState.currentDimension

        // Dibujar Plataformas (#: tangible, ~: intangible)
        worldState.platforms.forEach { platform ->
            val (col, row) = mapWorldToGrid(platform.position.x, platform.position.y)
            if (isValid(col, row)) {
                // Usa '#' o '~' según la dimensión para cumplir el criterio
                grid[row][col] = if (platform.tangibleInDimension == currentDim) '#' else '~'
            }
        }

        // Dibujar Enemigos (E)
        worldState.enemies.forEach { enemy ->
            val (col, row) = mapWorldToGrid(enemy.position.x, enemy.position.y)
            if (isValid(col, row)) {
                grid[row][col] = 'E' 
            }
        }
        
        // Dibujar Jugador (P) - Prioridad alta
        val (playerCol, playerRow) = mapWorldToGrid(worldState.player.position.x, worldState.player.position.y)
        if (isValid(playerCol, playerRow)) {
            grid[playerRow][playerCol] = 'P' 
        }

        // Imprimir la matriz
        grid.forEach { row ->
            println(row.joinToString(""))
        }
        
        // Imprimir información de estado
        println("Dimensión: $currentDim | Jugador @ (${worldState.player.position.x.toInt()}, ${worldState.player.position.y.toInt()})")
    }
    
    // Métodos de la interfaz no usados en la CLI
    override fun drawSprite(sprite: Any, x: Float, y: Float) { /* No usado */ }
    override fun render() { /* No usado */ }

    // Métodos Auxiliares
    private fun mapWorldToGrid(worldX: Float, worldY: Float): Pair<Int, Int> {
        val col = (worldX / WORLD_TO_GRID_SCALE).toInt()
        val row = GRID_HEIGHT - 1 - (worldY / WORLD_TO_GRID_SCALE).toInt() 
        return Pair(col.coerceIn(0, GRID_WIDTH - 1), row.coerceIn(0, GRID_HEIGHT - 1))
    }
    
    private fun isValid(col: Int, row: Int): Boolean {
        return col in 0 until GRID_WIDTH && row in 0 until GRID_HEIGHT
    }
    
    // Función para limpiar la pantalla (necesaria para el redibujado en cada fotograma)
    private fun clearScreen() {
        val ESC = '\u001b'
        print("${ESC}[H${ESC}[2J") 
    }
}
