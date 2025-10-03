package org.example.core

/**
 * Entidad: Plataforma del nivel.
 */
data class Platform(
    val position: Vector2D,
    val size: Vector2D,
    val tangibleInDimension: Dimension // Criterio de Aceptación
) {
    /**
     * Helper: Devuelve el rectángulo delimitador para colisiones.
     */
    fun getCollisionRect() = Rect(position.x, position.y, size.x, size.y)
}
