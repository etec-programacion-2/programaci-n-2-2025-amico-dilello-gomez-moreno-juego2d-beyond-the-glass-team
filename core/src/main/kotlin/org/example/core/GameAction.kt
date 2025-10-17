package org.example.core

/**
 * Enumera todas las acciones que el jugador puede realizar.
 * Esto nos permite manejar la entrada de una manera más limpia y estructurada.
 */
enum class GameAction {
    MOVE_LEFT,
    MOVE_RIGHT,
    JUMP,
    SWITCH_DIMENSION,
    NONE // Representa que no se realizó ninguna acción
}
