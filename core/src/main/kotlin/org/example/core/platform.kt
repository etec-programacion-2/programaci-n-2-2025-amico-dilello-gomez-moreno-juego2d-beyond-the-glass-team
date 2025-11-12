package org.example.core

/**
 * Representa una plataforma en el mundo del juego (Modelo).
 * Es una 'data class' simple para almacenar datos.
 *
 * ---
 * @see "Issue BTG-007: Carga de Niveles (entidad Plataforma)."
 * @see "Issue BTG-009: Cambio de Dimensión (propiedad 'tangibleInDimension')."
 * ---
 *
 * @property position La posición (x, y) de la esquina inferior izquierda.
 * @property size Las dimensiones (ancho, alto).
 * @property tangibleInDimension La propiedad CLAVE del juego. Define
 * en qué dimensión (A o B) esta plataforma es sólida.
 */
data class Platform(
    var position: Vector2D,
    val size: Vector2D,
    val tangibleInDimension: Dimension
)