package org.example.core

/**
 * Servicio dedicado a manejar la física de los enemigos (SOLID: Principio de Responsabilidad Única).
 * Está separado del PhysicsService del jugador.
 */
class EnemyPhysicsService {

    /**
     * Bucle principal de física para un enemigo.
     * Resuelve el movimiento en X y Y por separado para colisiones robustas.
     *
     * @param enemy El enemigo a actualizar.
     * @param platforms La lista de plataformas (para colisionar).
     * @param deltaTime El tiempo pasado desde el último fotograma.
     */
    fun update(enemy: Enemy, platforms: List<Platform>, deltaTime: Float) {
        // 1. Aplicar gravedad
        applyGravity(enemy, deltaTime)
        
        // --- CAMBIO 1: Física consciente de la dimensión ---
        // Pasa la dimensión del PROPIO enemigo a los métodos de colisión.
        
        // 2. Mover y colisionar en X
        enemy.position.x += enemy.velocity.x * deltaTime
        resolveCollisionsX(enemy, platforms, enemy.dimension)

        // 3. Mover y colisionar en Y
        enemy.position.y += enemy.velocity.y * deltaTime
        resolveCollisionsY(enemy, platforms, enemy.dimension)
    }

    /**
     * Aplica la fuerza de gravedad a la velocidad vertical del enemigo.
     */
    private fun applyGravity(enemy: Enemy, deltaTime: Float) {
        if (enemy.velocity.y < Enemy.MAX_FALL_SPEED) {
            enemy.velocity.y = Enemy.MAX_FALL_SPEED
        }
        enemy.velocity.y -= Enemy.GRAVITY * deltaTime
    }

    /**
     * Resuelve colisiones en el eje X (paredes).
     * IMPORTANTE: Esta física también incluye la IA de patrulla. Si choca,
     * invierte su dirección de patrulla.
     */
    private fun resolveCollisionsX(enemy: Enemy, platforms: List<Platform>, dimension: Dimension) {
        
        // --- CAMBIO 1: Física consciente de la dimensión ---
        // Filtra las plataformas para colisionar solo con las de la dimensión del enemigo.
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == dimension }
        
        for (platform in tangiblePlatforms) {
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

    /**
     * Resuelve colisiones en el eje Y (suelo y techo).
     * Actualiza el estado 'isOnGround'.
     */
    private fun resolveCollisionsY(enemy: Enemy, platforms: List<Platform>, dimension: Dimension) {
        enemy.isOnGround = false // Asumir que está en el aire
        
        // --- CAMBIO 1: Física consciente de la dimensión ---
        // Filtra las plataformas para colisionar solo con las de la dimensión del enemigo.
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == dimension }

        for (platform in tangiblePlatforms) {
            if (isColliding(enemy, platform)) {
                if (enemy.velocity.y < 0) { // Cayendo (aterrizando)
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

    /**
     * Comprobación de colisión AABB (Axis-Aligned Bounding Box).
     * (Este método no necesita cambios, ya que opera sobre listas ya filtradas).
     */
    private fun isColliding(enemy: Enemy, platform: Platform): Boolean {
        // Nota: Esta colisión asume que el enemigo SÍ puede chocar con plataformas
        // de otra dimensión (porque no filtra por dimensión).
        return enemy.position.x < platform.position.x + platform.size.x &&
               enemy.position.x + enemy.size.x > platform.position.x &&
               enemy.position.y < platform.position.y + platform.size.y &&
               enemy.position.y + enemy.size.y > platform.position.y
    }
}
