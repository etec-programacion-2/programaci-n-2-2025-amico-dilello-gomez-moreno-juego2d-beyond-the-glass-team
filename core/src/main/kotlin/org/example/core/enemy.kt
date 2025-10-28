package org.example.core

abstract class Enemy(
    var position: Vector2D,
    val size: Vector2D,
    val dimension: Dimension
) {
    var velocity: Vector2D = Vector2D(0f, 0f)
    var isOnGround: Boolean = false
    var direction: Float = -1f
    
    // --- NUEVO ---
    var isAlive: Boolean = true

    abstract fun updateAI(platforms: List<Platform>)

    companion object {
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f
    }
}
