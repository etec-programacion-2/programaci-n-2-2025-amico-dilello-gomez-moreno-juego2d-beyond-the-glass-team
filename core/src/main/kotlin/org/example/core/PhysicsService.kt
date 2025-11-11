package org.example.core

class PhysicsService {

    fun update(player: Player, platforms: List<Platform>, currentDimension: Dimension, deltaTime: Float) {
        applyGravity(player, deltaTime)

        player.position.x += player.velocity.x * deltaTime
        resolveCollisionsX(player, platforms, currentDimension)

        player.position.y += player.velocity.y * deltaTime
        resolveCollisionsY(player, platforms, currentDimension)
    }

    private fun applyGravity(player: Player, deltaTime: Float) {
        if (player.velocity.y < Player.MAX_FALL_SPEED) {
            player.velocity.y = Player.MAX_FALL_SPEED
        }
        player.velocity.y -= Player.GRAVITY * deltaTime
    }

    private fun resolveCollisionsX(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                if (player.velocity.x > 0) { // Derecha
                    player.position.x = platform.position.x - player.size.x
                } else if (player.velocity.x < 0) { // Izquierda
                    player.position.x = platform.position.x + platform.size.x
                }
                player.velocity.x = 0f
            }
        }
    }

    private fun resolveCollisionsY(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        player.isOnGround = false
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                if (player.velocity.y < 0) { // Cayendo
                    player.position.y = platform.position.y + platform.size.y
                    player.velocity.y = 0f
                    player.isOnGround = true
                    
                    // --- LÓGICA DE BTG-013 (LA QUE FALTABA) ---
                    player.hasDoubleJumped = false // Resetea el doble salto al tocar el suelo
                
                } else if (player.velocity.y > 0) { // Golpeando techo
                    player.position.y = platform.position.y - player.size.y
                    player.velocity.y = 0f
                }
            }
        }
    }

    private fun isColliding(player: Player, platform: Platform): Boolean {
        return player.position.x < platform.position.x + platform.size.x &&
               player.position.x + player.size.x > platform.position.x &&
               player.position.y < platform.position.y + platform.size.y &&
               player.position.y + player.size.y > platform.position.y
    }
}