package org.example.core

/**
 * Representa un vector o un punto en un plano 2D (Modelo).
 * Se utiliza para la posición, velocidad y tamaño de las entidades.
 * Es una 'data class' simple para almacenar datos.
 *
 * ---
 * @see "Issue (Core): Utilidad básica requerida por BTG-006, BTG-007, etc."
 * ---
 *
 * @property x La coordenada en el eje X.
 * @property y La coordenada en el eje Y.
 */
data class Vector2D(var x: Float, var y: Float)