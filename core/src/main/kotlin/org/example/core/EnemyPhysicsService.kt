package org.example.core

/**
 * Servicio dedicado a manejar la física de los enemigos (S de SOLID).
 */
class EnemyPhysicsService {

    fun update(enemy: Enemy, platforms: List<Platform>, deltaTime: Float) {
        applyGravity(enemy, deltaTime)
        
        // Mover y colisionar en X
        enemy.position.x += enemy.velocity.x * deltaTime
        resolveCollisionsX(enemy, platforms)

        // Mover y colisionar en Y
        enemy.position.y += enemy.velocity.y * deltaTime
        resolveCollisionsY(enemy, platforms)
    }

    private fun applyGravity(enemy: Enemy, deltaTime: Float) {
        if (enemy.velocity.y < Enemy.MAX_FALL_SPEED) {
            enemy.velocity.y = Enemy.MAX_FALL_SPEED
        }
        enemy.velocity.y -= Enemy.GRAVITY * deltaTime
    }

    /**
     * Resuelve colisiones en X. Si choca, invierte la dirección (IA de patrulla).
     */
    private fun resolveCollisionsX(enemy: Enemy, platforms: List<Platform>) {
        for (platform in platforms) {
            if (isColliding(enemy, platform)) {
                if (enemy.velocity.x > 0) { // Moviéndose a la derecha
                    enemy.position.x = platform.position.x - enemy.size.x
                } else if (enemy.velocity.x < 0) { // Moviéndose a la izquierda
                    enemy.position.x = platform.position.x + platform.size.x
                }
                // --- IA DE PATRULLA ---
                // Al chocar con una pared, invierte su dirección.
                enemy.direction *= -1f 
                enemy.velocity.x = 0f
            }
        }
    }

    private fun resolveCollisionsY(enemy: Enemy, platforms: List<Platform>) {
        enemy.isOnGround = false
        for (platform in platforms) {
            if (isColliding(enemy, platform)) {
                if (enemy.velocity.y < 0) { // Cayendo
                    enemy.position.y = platform.position.y + platform.size.y
                    enemy.velocity.y = 0f
                    enemy.isOnGround = true
                } else if (enemy.velocity.y > 0) { // Golpeando techo
                    enemy.position.y = platform.position.y - enemy.size.y
                    enemy.velocity.y = 0f
                }
            }
        }
    }

    private fun isColliding(enemy: Enemy, platform: Platform): Boolean {
        return enemy.position.x < platform.position.x + platform.size.x &&
               enemy.position.x + enemy.size.x > platform.position.x &&
               enemy.position.y < platform.position.y + platform.size.y &&
               enemy.position.y + enemy.size.y > platform.position.y
    }
}