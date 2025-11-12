package org.example.core

/**
 * Servicio dedicado a manejar la física del JUGADOR
 * (SOLID: Principio de Responsabilidad Única).
 *
 * ---
 * @see "Issue BTG-010: Física y Colisiones."
 * @see "Issue BTG-006: Movimiento del Jugador."
 * ---
 */
class PhysicsService {

    /**
     * El bucle de física del jugador. Se divide en ejes X e Y
     * para colisiones robustas (evita quedarse atascado en las esquinas).
     *
     * @param player El jugador a actualizar.
     * @param platforms La lista de todas las plataformas.
     * @param currentDimension La dimensión actual (para filtrar plataformas).
     * @param deltaTime El tiempo desde el último fotograma.
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
     * (POO: Encapsulamiento) Privado.
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
     * (POO: Encapsulamiento) Privado.
     *
     * ---
     * @see "Issue BTG-009: Física consciente de la dimensión."
     * ---
     */
    private fun resolveCollisionsX(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        // Filtra solo las plataformas tangibles en la dimensión actual
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                // Chocando por la derecha
                if (player.velocity.x > 0) {
                    player.position.x = platform.position.x - player.size.x
                } 
                // Chocando por la izquierda
                else if (player.velocity.x < 0) {
                    player.position.x = platform.position.x + platform.size.x
                }
                player.velocity.x = 0f
            }
        }
    }

    /**
     * Comprueba y resuelve colisiones verticales (suelo, techo).
     * SOLO colisiona con plataformas en la dimensión actual.
     * (POO: Encapsulamiento) Privado.
     *
     * ---
     * @see "Issue BTG-009: Física consciente de la dimensión."
     * ---
     */
    private fun resolveCollisionsY(player: Player, platforms: List<Platform>, currentDimension: Dimension) {
        // Asume que no está en el suelo hasta que se demuestre lo contrario
        player.isOnGround = false

        // Filtra solo las plataformas tangibles en la dimensión actual
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == currentDimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(player, platform)) {
                // El jugador estaba cayendo -> Aterrizaje
                if (player.velocity.y < 0) {
                    player.position.y = platform.position.y + platform.size.y
                    player.velocity.y = 0f
                    player.isOnGround = true // ¡Está en el suelo!

                    // --- Relacionado con BTG-013: Doble Salto ---
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
     * (POO: Encapsulamiento) Privado.
     */
    private fun isColliding(player: Player, platform: Platform): Boolean {
        return player.position.x < platform.position.x + platform.size.x &&
               player.position.x + player.size.x > platform.position.x &&
               player.position.y < platform.position.y + platform.size.y &&
               player.position.y + player.size.y > platform.position.y
    }
}