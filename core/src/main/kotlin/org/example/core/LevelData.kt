package org.example.core

/**
 * SRP: Contenedor de datos del estado inicial de un nivel cargado.
 */
data class LevelData(
    val playerStart: Vector2D,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>
)