package org.example.core

class PhysicsService {

    fun update(player: Player, platforms: List<Platform>, currentDimension: Dimension, deltaTime: Float) {
        applyGravity(player, deltaTime)
        resolveCollisions(player, platforms, currentDimension, deltaTime)
        updatePosition(player, deltaTime)
    }

    private fun applyGravity(player: Player, deltaTime: Float) {
        if (!player.isOnGround) {
            player.velocity.y -= Player.GRAVITY * deltaTime
        }
    }
    
    private fun resolveCollisions(player: Player, platforms: List<Platform>, currentDimension: Dimension, deltaTime: Float) {
        player.isOnGround = false 
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (player.position.x < platform.position.x + platform.size.x &&
                player.position.x + player.size.x > platform.position.x &&
                player.position.y < platform.position.y + platform.size.y &&
                player.position.y + player.size.y > platform.position.y) {

                val previousPlayerBottom = player.position.y - player.velocity.y * deltaTime

                if (player.velocity.y <= 0 && previousPlayerBottom >= platform.position.y + platform.size.y) {
                    player.position.y = platform.position.y + platform.size.y
                    player.velocity.y = 0f
                    player.isOnGround = true
                }
            }
        }
    }

    private fun updatePosition(player: Player, deltaTime: Float) {
        // Limitar la velocidad de caída
        if (player.velocity.y < Player.MAX_FALL_SPEED) {
            player.velocity.y = Player.MAX_FALL_SPEED
        }

        player.position.x += player.velocity.x * deltaTime
        player.position.y += player.velocity.y * deltaTime
    }
}