package org.example.core

/**
 * Contenedor de datos que almacena el estado actual del mundo de juego, 
 * desacoplando la lógica (CLI_GameStateManager) de la presentación (CLI_RenderService).
 */
data class WorldState(
    val player: Player,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>,
    val currentDimension: Dimension
)
