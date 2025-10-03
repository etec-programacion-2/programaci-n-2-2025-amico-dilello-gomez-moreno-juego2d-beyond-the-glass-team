package org.example.core

/**
 * SRP: Responsable único de aplicar las leyes de la física y resolver las colisiones.
 * Criterios: Gravedad afecta, se detiene al aterrizar, no atraviesa.
 */
class PhysicsEngine {

    fun updatePlayerPhysics(player: Player, platforms: List<Platform>, deltaTime: Float) {
        
        // 1. APLICAR GRAVEDAD
        if (!player.isOnGround) {
            player.velocity.y += Physics.GRAVITY * deltaTime
        } else {
            player.velocity.y = 0f 
        }

        // --- CALCULAR POSICIÓN TENTATIVA ---
        val newX = player.position.x + player.velocity.x * deltaTime
        val newY = player.position.y + player.velocity.y * deltaTime
        
        val oldRect = player.getCollisionRect()

        // Filtrar solo plataformas tangibles en la dimensión actual
        val tangiblePlatforms = platforms.filter { 
            it.tangibleInDimension == player.currentDimension 
        }
        
        var finalX = newX
        var finalY = newY
        var landed = false

        // 2. RESOLUCIÓN DE COLISIONES (Verificación de Criterios)
        for (platform in tangiblePlatforms) {
            val platformRect = platform.getCollisionRect()

            // Resolución Horizontal (Criterio: no puede atravesar)
            val rectAfterX = Rect(newX, player.position.y, player.size.x, player.size.y)
            if (rectAfterX.overlaps(platformRect)) {
                finalX = player.position.x 
                player.velocity.x = 0f 
            }

            // Resolución Vertical
            val rectAfterY = Rect(finalX, newY, player.size.x, player.size.y)
            if (rectAfterY.overlaps(platformRect)) {
                
                // Aterrizaje (Criterio: se detiene al aterrizar)
                if (player.velocity.y <= 0 && oldRect.bottom >= platformRect.top) {
                    finalY = platformRect.top 
                    landed = true
                    player.velocity.y = 0f 
                } 
                // Golpear techo
                else if (player.velocity.y > 0 && oldRect.top <= platformRect.bottom) {
                    finalY = platformRect.bottom - player.size.y
                    player.velocity.y = 0f 
                }
            }
        }
        
        // 3. APLICAR MOVIMIENTO FINAL
        player.position.x = finalX
        player.position.y = finalY
        player.isOnGround = landed
    }
}