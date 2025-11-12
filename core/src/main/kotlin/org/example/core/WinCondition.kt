package org.example.core

/**
 * (NUEVO) Enum que define las posibles condiciones para ganar un nivel.
 * Esto permite al ProgressionService saber qué comprobar.
 */
enum class WinCondition {
    /** El jugador debe eliminar a todos los enemigos del nivel. */
    ALL_ENEMIES_KILLED,
    
    /** El jugador solo necesita llegar a la salida (para niveles de puzle/finales). */
    REACH_EXIT
}