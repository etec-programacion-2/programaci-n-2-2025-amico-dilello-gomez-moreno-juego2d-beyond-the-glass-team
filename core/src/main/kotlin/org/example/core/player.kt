package org.example.core

/**
 * Representa la entidad del jugador.
 * @property position La posición actual del jugador en el mundo.
 * @property size Las dimensiones del jugador (ancho y alto).
 * @property currentDimension La dimensión en la que se encuentra el jugador.
 */
data class Player(
    var position: Vector2D,
    val size: Vector2D,
    var currentDimension: Dimension
)
