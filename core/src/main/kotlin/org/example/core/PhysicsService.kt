package org.example.core

class PhysicsService {

    private fun applyGravity(player: Player, deltaTime: Float) {
        if (!player.isOnGround) {
            // Se resta para que la gravedad tire hacia abajo (eje Y negativo)
            player.velocity.y -= Player.GRAVITY * deltaTime
        }
    }

    private fun resolveCollisions(player: Player, platforms: List<Platform>, deltaTime: Float) {
        player.isOnGround = false

        platforms.forEach { platform ->
            if (platform.tangibleInDimension == player.currentDimension) {
                val playerRect = player.getBounds()
                val platformRect = platform.getBounds()

                if (playerRect.overlaps(platformRect)) {
                    // Calculamos dónde estaba el jugador en el frame anterior para detectar desde dónde vino la colisión
                    val previousPlayerBottom = player.position.y - (player.velocity.y * deltaTime)
                    val platformTop = platform.position.y + platform.size.y

                    // Si venía desde arriba y ahora está dentro, lo colocamos justo encima de la plataforma
                    if (player.velocity.y <= 0 && previousPlayerBottom >= platformTop) {
                        player.position.y = platformTop
                        player.velocity.y = 0f
                        player.isOnGround = true
                    }
                }
            }
        }
    }
    
    fun update(player: Player, platforms: List<Platform>, deltaTime: Float) {
        applyGravity(player, deltaTime)

        // Actualizamos la posición basándonos en la velocidad
        player.position.x += player.velocity.x * deltaTime
        player.position.y += player.velocity.y * deltaTime

        resolveCollisions(player, platforms, deltaTime)

        // Aplicamos el límite de velocidad de caída
        if (player.velocity.y < -Player.MAX_FALL_SPEED) {
            player.velocity.y = -Player.MAX_FALL_SPEED
        }
    }
}

// Funciones de extensión para obtener los rectángulos de colisión
fun Player.getBounds(): Rect = Rect(position.x, position.y, size.x, size.y)
fun Platform.getBounds(): Rect = Rect(position.x, position.y, size.x, size.y)

// Clase simple para representar un rectángulo
data class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
    fun overlaps(other: Rect): Boolean {
        return x < other.x + other.width && x + width > other.x &&
               y < other.y + other.height && y + height > other.y
    }
}
