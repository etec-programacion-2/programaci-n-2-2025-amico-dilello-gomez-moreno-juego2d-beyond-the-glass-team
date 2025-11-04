package org.example.core

/**
 * Servicio dedicado a manejar la física del JUGADOR (SOLID: Principio de Responsabilidad Única).
 */
class PhysicsService {

    /**
     * El bucle de física del jugador. Se divide en ejes X e Y
     * para colisiones robustas (evita quedarse atascado en las esquinas).
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

    /**
     * Aplica la gravedad a la velocidad vertical del jugador y limita la velocidad de caída.
     */
    private fun applyGravity(player: Player, deltaTime: Float) {
        // Limitar la velocidad de caída
        if (player.velocity.y < Player.MAX_FALL_SPEED) {
            player.velocity.y = Player.MAX_FALL_SPEED
        }
        // Aplicar gravedad
        player.velocity.y -= Player.GRAVITY * deltaTime
    }

    /**
     * Comprueba y resuelve colisiones horizontales (paredes).
     * SOLO colisiona con plataformas en la dimensión actual.
     */
    private fun resolveCollisionsX(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        // Filtra las plataformas: solo importan las tangibles en la dimensión actual
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                // El jugador se movía hacia la derecha y chocó
                if (player.velocity.x > 0) {
                    player.position.x = platform.position.x - player.size.x
                    player.velocity.x = 0f
                } 
                // El jugador se movía hacia la izquierda y chocó
                else if (player.velocity.x < 0) {
                    player.position.x = platform.position.x + platform.size.x
                    player.velocity.x = 0f
                }
            }
        }
    }

    /**
     * Comprueba y resuelve colisiones verticales (suelo y techo).
     * Actualiza el estado 'isOnGround'.
     * SOLO colisiona con plataformas en la dimensión actual.
     */
    private fun resolveCollisionsY(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        player.isOnGround = false // Asumir que está en el aire hasta que se demuestre lo contrario
        
        // Filtra las plataformas: solo importan las tangibles en la dimensión actual
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                // El jugador estaba cayendo -> Aterrizaje
                if (player.velocity.y < 0) {
                    player.position.y = platform.position.y + platform.size.y
                    player.velocity.y = 0f
                    player.isOnGround = true // ¡Está en el suelo!

                    // --- CAMBIO BTG-013 ---
                    // Al tocar el suelo, se resetea el doble salto.
                    player.hasDoubleJumped = false
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
