package org.example.core

data class WorldState(
    val player: Player,
    val platforms: List<Platform>,
    val enemies: List<Enemy>,
    val collectibles: List<Collectible>,
    val currentDimension: Dimension,
    // --- NUEVO ---
    val playerInvincible: Boolean = false 
)
