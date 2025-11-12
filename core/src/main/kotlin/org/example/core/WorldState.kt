package org.example.core

/**
 * Representa un "snapshot" (foto) de solo lectura del estado del mundo en un fotograma.
 * Este es el objeto que 'MiJuego' (lógica) le pasa al 'RenderService' (gráficos)
 * para desacoplarlos (SOLID: DIP).
 *
 * El RenderService recibe esto y no sabe nada sobre 'MiJuego'.
 *
 * ---
 * @see "Issue BTG-002: Arquitectura (DIP)."
 * @see "Issue BTG-008: Bucle de Juego."
 * ---
 *
 * @param player El estado completo del jugador. (BTG-006)
 * @param platforms La lista de todas las plataformas. (BTG-007, BTG-009)
 * @param enemies La lista de enemigos (solo los vivos). (BTG-011)
 * @param collectibles La lista de coleccionables. (BTG-013)
 * @param currentDimension La dimensión activa. (BTG-009)
 * @param playerInvincible 'true' si el jugador está parpadeando (invencible). (BTG-012)
 * @param isPlayerAttacking 'true' si el hitbox de ataque debe dibujarse. (BTG-012)
 * @param playerFacingDirection Hacia dónde mira el jugador (para dibujar el hitbox). (BTG-012)
 */
data class WorldState(
    val player: Player,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>,
    val currentDimension: Dimension,
    val playerInvincible: Boolean = false,
    
    // --- Feedback Visual (BTG-012) ---
    // Pasamos el estado de ataque para el feedback visual
    val isPlayerAttacking: Boolean = false,
    val playerFacingDirection: Float = 1f
)