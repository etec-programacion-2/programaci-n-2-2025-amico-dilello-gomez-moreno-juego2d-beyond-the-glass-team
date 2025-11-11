package org.example.core

data class Player(
    var position: Vector2D,
    val size: Vector2D, // Se ajustará a 32x32 desde MiJuego.kt
    var velocity: Vector2D = Vector2D(0f, 0f),
    var isOnGround: Boolean = false,

    var facingDirection: Float = 1f, // 1f = Derecha, -1f = Izquierda
    var isAttacking: Boolean = false,
    var attackTimer: Float = 0f,

    // --- CAMPOS DE BTG-013 (LOS QUE FALTABAN) ---
    var energyFragments: Int = 0,
    var canDoubleJump: Boolean = false,
    var hasDoubleJumped: Boolean = false,
    var dimensionSwitchCooldown: Float = 0f,
    val hitEnemiesThisAttack: MutableSet<Enemy> = mutableSetOf()
) {
    companion object {
        const val MOVE_SPEED = 250.0f
        const val JUMP_STRENGTH = 600.0f
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f

        const val ATTACK_DURATION = 0.2f
        const val ATTACK_COOLDOWN = 0.5f
        val ATTACK_HITBOX = Vector2D(25f, 32f) // Ajustado a 32x32

        // --- CONSTANTES DE BTG-013 (LAS QUE FALTABAN) ---
        const val DIMENSION_SWITCH_COOLDOWN = 1.0f
        const val FRAGMENTS_TO_UNLOCK = 3
    }
}