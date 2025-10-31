package org.example.core

data class WorldState(
    val player: Player,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>,
    val currentDimension: Dimension,
    val playerInvincible: Boolean = false,
    
    // --- NUEVO ---
    // Pasamos el estado de ataque para el feedback visual
    val isPlayerAttacking: Boolean = false,
    val playerFacingDirection: Float = 1f
)

