package org.example.core

/**
 * Entidad: Enemigo.
 */
data class Enemy(
    val position: Vector2D,
    val size: Vector2D,
    val currentDimension: Dimension
)
