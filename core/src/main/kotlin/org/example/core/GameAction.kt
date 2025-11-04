package org.example.core

/**
 * Enum que representa las ACCIONES abstractas del juego (SOLID: Inversión de Dependencias).
 * El 'core' (MiJuego) entiende estas acciones. No sabe qué teclas
 * (W, A, S, D, Flechas...) las producen.
 */
enum class GameAction {
    MOVE_LEFT,
    MOVE_RIGHT,
    JUMP,
    SWITCH_DIMENSION,
    ATTACK // Nueva acción de ataque
}
