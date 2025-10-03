package org.example.core

/**
 * SRP: Estructura de datos para geometría de colisiones.
 */
data class Rect(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
) {
    // ... (Métodos get: left, right, bottom, top y overlaps se mantienen igual) ...
    val left: Float get() = x
    val right: Float get() = x + width
    val bottom: Float get() = y
    val top: Float get() = y + height

    /**
     * Lógica de colisión AABB.
     */
    fun overlaps(other: Rect): Boolean {
        return left < other.right && right > other.left &&
               bottom < other.top && top > other.bottom
    }
}
