package org.example.core

/**
 * Clase base abstracta para todos los enemigos (Criterio 1: Herencia).
 * Define las propiedades comunes y el comportamiento que todos los enemigos deben tener.
 */
abstract class Enemy(
    var position: Vector2D,
    val size: Vector2D,
    val dimension: Dimension // Dimensión en la que este enemigo existe
) {
    // Propiedades de física, similares al Player
    var velocity: Vector2D = Vector2D(0f, 0f)
    var isOnGround: Boolean = false
    var direction: Float = -1f // Dirección de movimiento (-1f = izquierda, 1f = derecha)

    /**
     * Lógica de IA específica de este tipo de enemigo.
     * Se llamará solo si el enemigo está activo (en la dimensión del jugador).
     */
    abstract fun updateAI(platforms: List<Platform>)

    companion object {
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f
    }
}
