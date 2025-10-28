package org.example.core

/**
 * Define el resultado de una comprobación de combate.
 */
enum class CombatResult {
    NONE,
    ENEMY_KILLED,
    PLAYER_DAMAGED
}

/**
 * Servicio que maneja la lógica de combate.
 * Comprueba colisiones entre el jugador y los enemigos.
 */
class CombatService {

    /**
     * Comprueba las colisiones entre el jugador y los enemigos.
     * @return El resultado del combate.
     */
    fun checkCombat(player: Player, enemies: List<Enemy>, currentDimension: Dimension): CombatResult {
        
        // Solo comprueba enemigos que están vivos y en la dimensión del jugador
        val activeEnemies = enemies.filter { it.isAlive && it.dimension == currentDimension }

        for (enemy in activeEnemies) {
            if (isColliding(player, enemy)) {
                
                // --- Criterio 3: Mecánica de Derrota (Stomp) ---
                // ¿El jugador está cayendo?
                // ¿Y está el jugador por encima de la mitad del enemigo?
                val isStomp = player.velocity.y < 0 && 
                              player.position.y > enemy.position.y + (enemy.size.y * 0.5f)
                
                if (isStomp) {
                    enemy.isAlive = false // Marca al enemigo como muerto
                    return CombatResult.ENEMY_KILLED
                } else {
                    // --- Criterio 2: El jugador recibe daño ---
                    return CombatResult.PLAYER_DAMAGED
                }
            }
        }

        // No hubo colisiones
        return CombatResult.NONE
    }

    /**
     * Comprobación de colisión AABB (Axis-Aligned Bounding Box).
     */
    private fun isColliding(player: Player, enemy: Enemy): Boolean {
        // simple AABB
        return player.position.x < enemy.position.x + enemy.size.x &&
               player.position.x + player.size.x > enemy.position.x &&
               player.position.y < enemy.position.y + enemy.size.y &&
               player.position.y + player.size.y > enemy.position.y
    }
}