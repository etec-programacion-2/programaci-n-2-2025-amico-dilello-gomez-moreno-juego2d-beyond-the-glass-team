package org.example.core

data class Player(
    var position: Vector2D,
    val size: Vector2D,
    // Propiedades de física restauradas
    var velocity: Vector2D = Vector2D(0f, 0f),
    var isOnGround: Boolean = false,
    var currentDimension: Dimension = Dimension.A
) {
    // Constantes de física restauradas
    companion object {
        const val MOVE_SPEED = 250.0f
        const val JUMP_STRENGTH = 600.0f
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f
    }
}
