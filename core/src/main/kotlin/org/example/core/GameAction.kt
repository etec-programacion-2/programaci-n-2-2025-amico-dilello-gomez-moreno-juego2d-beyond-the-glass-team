package org.example.core

/**
 * Enumera todas las acciones que el jugador puede realizar.
 * NONE y QUIT han sido eliminados para un sistema de Set<GameAction> más limpio.
 */
enum class GameAction {
    MOVE_LEFT,
    MOVE_RIGHT,
    JUMP,
    SWITCH_DIMENSION
}

