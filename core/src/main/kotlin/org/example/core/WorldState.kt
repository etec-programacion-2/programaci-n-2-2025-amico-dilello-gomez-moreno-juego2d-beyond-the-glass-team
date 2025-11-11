package org.example.core

/**
 * Representa un "snapshot" (foto) de solo lectura del estado del mundo en un fotograma.
 * Este es el objeto que 'MiJuego' (lógica) le pasa al 'RenderService' (gráficos)
 * para desacoplarlos (SOLID: DIP).
 *
 * @param player El estado completo del jugador.
 * @param platforms La lista de todas las plataformas.
 * @param enemies La lista de enemigos (solo los vivos).
 * @param collectibles La lista de coleccionables.
 * @param currentDimension La dimensión activa.
 * @param playerInvincible 'true' si el jugador está parpadeando (invencible).
 * @param isPlayerAttacking 'true' si el hitbox de ataque debe dibujarse.
 * @param playerFacingDirection Hacia dónde mira el jugador (para dibujar el hitbox).
 */
data class WorldState(
    val player: Player,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>,
    val currentDimension: Dimension,
    val playerInvincible: Boolean = false,
    
    // --- NUEVO (Feedback Visual) ---
    // Pasamos el estado de ataque para el feedback visual
    val isPlayerAttacking: Boolean = false,
    val playerFacingDirection: Float = 1f
)
