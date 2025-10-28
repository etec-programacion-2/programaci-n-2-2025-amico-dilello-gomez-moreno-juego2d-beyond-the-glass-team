package org.example.core

class PhysicsService {

    /**
     * El bucle de física ahora se divide en ejes X e Y para colisiones robustas.
     */
    fun update(player: Player, platforms: List<Platform>, currentDimension: Dimension, deltaTime: Float) {
        // 1. Aplicar fuerzas (solo gravedad por ahora)
        applyGravity(player, deltaTime)

        // 2. Mover y colisionar en el eje X
        player.position.x += player.velocity.x * deltaTime
        resolveCollisionsX(player, platforms, currentDimension)

        // 3. Mover y colisionar en el eje Y
        player.position.y += player.velocity.y * deltaTime
        resolveCollisionsY(player, platforms, currentDimension)
    }

    private fun applyGravity(player: Player, deltaTime: Float) {
        // Limitar la velocidad de caída
        if (player.velocity.y < Player.MAX_FALL_SPEED) {
            player.velocity.y = Player.MAX_FALL_SPEED
        }
        player.velocity.y -= Player.GRAVITY * deltaTime
    }

    /**
     * Comprueba y resuelve colisiones horizontales (paredes).
     */
    private fun resolveCollisionsX(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                // El jugador se movía hacia la derecha
                if (player.velocity.x > 0) {
                    player.position.x = platform.position.x - player.size.x
                } 
                // El jugador se movía hacia la izquierda
                else if (player.velocity.x < 0) {
                    player.position.x = platform.position.x + platform.size.x
                }
                player.velocity.x = 0f
            }
        }
    }

    /**
     * Comprueba y resuelve colisiones verticales (suelo y techo).
     */
    private fun resolveCollisionsY(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        player.isOnGround = false // Asumir que está en el aire
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                // El jugador estaba cayendo -> Aterrizaje
                if (player.velocity.y < 0) {
                    player.position.y = platform.position.y + platform.size.y
                    player.velocity.y = 0f
                    player.isOnGround = true
                } 
                // El jugador estaba subiendo -> "Bonk" en el techo
                else if (player.velocity.y > 0) {
                    player.position.y = platform.position.y - player.size.y
                    player.velocity.y = 0f
                }
            }
        }
    }

    /**
     * Comprobación de colisión AABB (Axis-Aligned Bounding Box).
     * Devuelve true si hay una superposición.
     */
    private fun isColliding(player: Player, platform: Platform): Boolean {
        return player.position.x < platform.position.x + platform.size.x &&
               player.position.x + player.size.x > platform.position.x &&
               player.position.y < platform.position.y + platform.size.y &&
               player.position.y + player.size.y > platform.position.y
    }
}