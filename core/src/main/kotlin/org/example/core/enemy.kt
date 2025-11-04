package org.example.core

/**
 * Clase base abstracta para todos los enemigos (POO: Herencia).
 * Define las propiedades y comportamientos comunes que CUALQUIER enemigo tendrá.
 *
 * @param position Posición (x, y).
 * @param size Tamaño (ancho, alto).
 * @param dimension Dimensión a la que pertenece.
 */
abstract class Enemy(
    var position: Vector2D,
    val size: Vector2D,
    val dimension: Dimension
) {
    // Propiedades de estado y física
    var velocity: Vector2D = Vector2D(0f, 0f)
    var isOnGround: Boolean = false
    var direction: Float = -1f // Dirección de patrulla (-1f Izq, 1f Der)
    
    // Estado de vida
    var isAlive: Boolean = true

    /**
     * Método abstracto para la Inteligencia Artificial (POO: Polimorfismo).
     * Cada subclase (como BasicEnemy) DEBE implementar su propia lógica de IA.
     */
    abstract fun updateAI(platforms: List<Platform>)

    /**
     * 'companion object' contiene constantes compartidas por TODAS las instancias de Enemy.
     */
    companion object {
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f
    }
}