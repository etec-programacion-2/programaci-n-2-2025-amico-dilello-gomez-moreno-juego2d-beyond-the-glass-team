package org.example.core

/**
 * Representa un objeto coleccionable en el mundo del juego.
 * @property position La posición del objeto.
 * @property size Las dimensiones del objeto.
 * @property value La cantidad de puntos que otorga al ser recolectado.
 */
data class Collectible(
    var position: Vector2D,
    val size: Vector2D,
    val value: Int
)