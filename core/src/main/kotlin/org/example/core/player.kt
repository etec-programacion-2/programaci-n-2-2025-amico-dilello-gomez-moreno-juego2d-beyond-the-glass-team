package org.example.core

/**
 * SRP: Representa el ESTADO del jugador Leoric. NO contiene la lógica de física/movimiento.
 */
data class Player(
    val size: Vector2D,
    var position: Vector2D,
    var velocity: Vector2D = Vector2D(0f, 0f), // Velocidad (Vx, Vy)
    var isOnGround: Boolean = false,         // ¿Está tocando una plataforma?
    var currentDimension: Dimension = Dimension.A
) {
    /**
     * Helper: Devuelve el rectángulo delimitador para colisiones.
     */
    fun getCollisionRect() = Rect(position.x, position.y, size.x, size.y)
}