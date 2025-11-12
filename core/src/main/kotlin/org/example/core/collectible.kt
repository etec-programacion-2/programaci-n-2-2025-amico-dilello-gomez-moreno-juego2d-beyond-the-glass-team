package org.example.core

/**
 * Representa un objeto coleccionable en el mundo del juego.
 * Es una 'data class' simple para almacenar datos (Modelo).
 *
 * ---
 * @see "Issue BTG-013: Coleccionables y Habilidades Progresivas."
 * ---
 *
 * @property position La posición (x, y) del objeto.
 * @property size Las dimensiones (ancho, alto) del objeto.
 * @property value La cantidad de puntos que otorga al ser recolectado.
 * @property isCollected Estado para saber si ya fue recogido.
 */
data class Collectible(
    var position: Vector2D,
    val size: Vector2D,
    val value: Int,
    
    // --- CAMBIO BTG-013 ---
    // Añadimos un estado para saber si ya fue recogido.
    // Esto es más limpio que eliminarlo de la lista, y permite
    // que el RenderService sepa si debe dibujarlo o no.
    var isCollected: Boolean = false
)