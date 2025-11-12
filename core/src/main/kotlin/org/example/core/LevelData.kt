package org.example.core

/**
 * Contenedor de datos que almacena todas las entidades cargadas de un nivel.
 * Esto representa el estado inicial del mundo de juego.
 * Es un Modelo de datos simple.
 *
 * ---
 * @see "Issue BTG-007: Carga de Niveles."
 * ---
 */
data class LevelData(
    val playerStart: Vector2D, // (Relacionado con BTG-006)
    val platforms: List<Platform>, // (Relacionado con BTG-009)
    val enemies: List<Enemy>, // (Relacionado con BTG-011)
    val collectibles: List<Collectible> // (Relacionado con BTG-013)
)