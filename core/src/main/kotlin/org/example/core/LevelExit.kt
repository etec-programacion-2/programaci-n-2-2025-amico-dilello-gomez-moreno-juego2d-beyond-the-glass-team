package org.example.core

/**
 * (NUEVO) Modelo de datos para la "Puerta de Salida" del nivel.
 * Almacena la posición de la puerta y la condición para desbloquearla.
 *
 * @param position Posición (x, y) de la puerta.
 * @param size Tamaño (ancho, alto) de la puerta.
 * @param condition El requisito para que esta puerta se abra.
 */
data class LevelExit(
    val position: Vector2D,
    val size: Vector2D,
    val condition: WinCondition
)