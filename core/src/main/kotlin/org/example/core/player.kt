package org.example.core

data class Player(
    var position: Vector2D,
    val size: Vector2D,
    var velocity: Vector2D = Vector2D(0f, 0f),
    var isOnGround: Boolean = false
    // Se elimina 'currentDimension: Dimension'.
    // El estado de la dimensión lo maneja el mundo (MiJuego), no el jugador.
) {
    companion object {
        const val MOVE_SPEED = 250.0f
        const val JUMP_STRENGTH = 600.0f
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f
    }
}

