package org.example.core

data class Player(
    var position: Vector2D,
    val size: Vector2D,
    var velocity: Vector2D = Vector2D(0f, 0f),
    var isOnGround: Boolean = false,

    // --- NUEVO ---
    var facingDirection: Float = 1f, // 1f = Derecha, -1f = Izquierda
    var isAttacking: Boolean = false,
    var attackTimer: Float = 0f // Temporizador para gestionar la duración y el cooldown
) {
    companion object {
        const val MOVE_SPEED = 250.0f
        const val JUMP_STRENGTH = 600.0f
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f

        // --- NUEVO ---
        const val ATTACK_DURATION = 0.2f  // Duración del hitbox activo
        const val ATTACK_COOLDOWN = 0.5f  // Tiempo total antes de poder atacar de nuevo
        val ATTACK_HITBOX = Vector2D(50f, 64f) // Tamaño del área de ataque
    }
}