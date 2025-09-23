package org.example.core

/**
 * Representa una plataforma en el mundo del juego.
 * @property position La posición de la plataforma.
 * @property size Las dimensiones de la plataforma.
 * @property tangibleInDimension La dimensión en la que la plataforma es sólida y el jugador puede interactuar con ella.
 */
data class Platform(
    var position: Vector2D,
    val size: Vector2D,
    val tangibleInDimension: Dimension
)
