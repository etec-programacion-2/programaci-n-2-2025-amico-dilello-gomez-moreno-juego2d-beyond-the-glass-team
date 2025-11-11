package org.example.core

/**
 * Representa un objeto coleccionable en el mundo del juego.
 * (ACTUALIZADO: Añadido 'isCollected' para BTG-013)
 */
data class Collectible(
    var position: Vector2D,
    val size: Vector2D,
    val value: Int,
    var isCollected: Boolean = false // (BTG-013)
)