package org.example.core

/**
 * Contenedor de datos que almacena todas las entidades cargadas de un nivel.
 * Esto representa el estado inicial del mundo de juego.
 */
data class LevelData(
    val playerStart: Vector2D,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>
)
