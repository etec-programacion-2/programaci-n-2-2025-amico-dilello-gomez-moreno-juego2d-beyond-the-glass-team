package org.example.core

/**
 * Representa la entidad de un enemigo.
 * @property position La posición actual del enemigo.
 * @property size Las dimensiones del enemigo.
 * @property currentDimension La dimensión en la que existe el enemigo.
 */
data class Enemy(
    var position: Vector2D,
    val size: Vector2D,
    var currentDimension: Dimension
)
