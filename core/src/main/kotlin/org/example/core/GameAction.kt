package org.example.core

/**
 * Enum que representa las ACCIONES abstractas del juego (SOLID: Inversión de Dependencias).
 * El 'core' (MiJuego) entiende estas acciones. No sabe qué teclas
 * (W, A, S, D, Flechas...) las producen.
 *
 * ---
 * @see "Issue BTG-002: Diseño de la arquitectura de servicios."
 * @see "Issue BTG-006: Acciones de Movimiento y Salto."
 * @see "Issue BTG-009: Acción de Cambio de Dimensión."
 * @see "Issue BTG-012: Acción de Ataque."
 * ---
 */
enum class GameAction {
    MOVE_LEFT,
    MOVE_RIGHT,
    JUMP,
    SWITCH_DIMENSION,
    ATTACK // Nueva acción de ataque
}