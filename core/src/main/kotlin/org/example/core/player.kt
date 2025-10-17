package org.example.core

data class Player(
    var position: Vector2D,
    val size: Vector2D,
    var currentDimension: Dimension,
    // Propiedades de física añadidas
    var velocity: Vector2D = Vector2D(0f, 0f),
    var isOnGround: Boolean = false
) {
    /**
     * Un 'companion object' contiene propiedades que pertenecen a la clase en sí,
     * no a una instancia. Son perfectas para constantes como estas.
     */
    companion object {
        const val MOVE_SPEED = 300f
        const val JUMP_FORCE = 600f
        const val GRAVITY = 1800f // Un valor más alto para una gravedad más "pesada"
        const val MAX_FALL_SPEED = 1000f // Límite para que no acelere infinitamente
    }
}
