package org.example.core

/**
 * Data class que almacena TODO el estado del jugador (Modelo).
 * No tiene lógica, solo datos.
 *
 * ---
 * @see "Issue BTG-006: Modelo de Jugador y Movimiento Básico."
 * @see "Issue BTG-012: Estado de Combate (facingDirection, isAttacking, etc.)."
 * @see "Issue BTG-013: Habilidades Progresivas (energyFragments, canDoubleJump, etc.)."
 * ---
 *
 * @param position Posición actual (x, y).
 * @param size Tamaño (ancho, alto).
 * @param velocity Velocidad actual (vx, vy).
 * @param isOnGround 'true' si está tocando el suelo.
 * @param facingDirection Hacia dónde mira (-1f Izq, 1f Der).
 * @param isAttacking 'true' si el hitbox de ataque está activo.
 * @param attackTimer Temporizador para gestionar la duración y el cooldown del ataque.
 * @param hitEnemiesThisAttack Almacena enemigos golpeados en el ataque actual para evitar dobles golpes.
 * @param energyFragments Contador de coleccionables.
 * @param canDoubleJump 'true' si la habilidad está desbloqueada.
 * @param hasDoubleJumped 'true' si ya usó el doble salto en el aire actual.
 */
data class Player(
    var position: Vector2D,
    val size: Vector2D,
    var velocity: Vector2D = Vector2D(0f, 0f),
    var isOnGround: Boolean = false,

    // --- ESTADO DE COMBATE (BTG-012) ---
    var facingDirection: Float = 1f, // 1f = Derecha, -1f = Izquierda
    var isAttacking: Boolean = false,
    var attackTimer: Float = 0f, // Temporizador para gestionar la duración y el cooldown
    var hitEnemiesThisAttack: MutableSet<Enemy> = mutableSetOf(),

    // --- HABILIDADES PROGRESIVAS (BTG-013) ---
    var energyFragments: Int = 0,
    var canDoubleJump: Boolean = false, // Habilidad desbloqueable
    var hasDoubleJumped: Boolean = false // Estado para el salto actual

) {
    /**
     * 'companion object' contiene constantes que definen la física
     * y el combate del jugador.
     */
    companion object {
        // --- Constantes de Física (BTG-006, BTG-010) ---
        const val MOVE_SPEED = 250.0f
        const val JUMP_STRENGTH = 600.0f
        const val GRAVITY = 1800.0f
        const val MAX_FALL_SPEED = -800.0f

        // --- Constantes de Combate (BTG-012) ---
        const val ATTACK_DURATION = 0.2f // Duración del hitbox activo
        const val ATTACK_COOLDOWN = 0.5f // Tiempo total entre ataques
        val ATTACK_HITBOX = Vector2D(40f, 64f) // Tamaño del hitbox
    }
}