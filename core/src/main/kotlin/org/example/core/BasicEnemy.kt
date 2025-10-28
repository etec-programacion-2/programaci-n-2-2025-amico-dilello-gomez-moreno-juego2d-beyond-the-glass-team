package org.example.core

/**
 * Implementación de un enemigo básico que patrulla.
 * IA MEJORADA: Ahora detecta los bordes de las plataformas.
 */
class BasicEnemy(
    position: Vector2D,
    size: Vector2D,
    dimension: Dimension
) : Enemy(position, size, dimension) {

    // REQ 2: Aumentamos la velocidad de patrulla
    private val patrolSpeed = 80f

    /**
     * Implementación de la IA:
     * 1. Comprueba si hay suelo delante. Si no, se da la vuelta.
     * 2. Establece su velocidad basada en la dirección.
     */
    override fun updateAI(platforms: List<Platform>) {
        
        // --- INICIO LÓGICA BTG-011 (Req 1: Detección de Borde) ---
        
        // Calcula dónde estarán los "pies" del enemigo en el siguiente paso
        val feelerX: Float
        if (direction > 0) { // Moviéndose a la derecha
            feelerX = position.x + size.x + 1 // 1 píxel delante del borde derecho
        } else { // Moviéndose a la izquierda
            feelerX = position.x - 1 // 1 píxel delante del borde izquierdo
        }
        val feelerY = position.y - 1 // 1 píxel debajo de los pies

        // Comprueba si hay una plataforma en ese punto
        if (!isPlatformAt(feelerX, feelerY, platforms)) {
            // Si no hay plataforma, ¡es un borde! Darse la vuelta.
            direction *= -1f
        }
        // --- FIN LÓGICA BTG-011 ---

        this.velocity.x = patrolSpeed * this.direction
    }

    /**
     * Función de ayuda para la IA. Comprueba si un punto (x, y)
     * está dentro de CUALQUIERA de las plataformas de la lista.
     */
    private fun isPlatformAt(x: Float, y: Float, platforms: List<Platform>): Boolean {
        for (platform in platforms) {
            if (x >= platform.position.x && x <= platform.position.x + platform.size.x &&
                y >= platform.position.y && y <= platform.position.y + platform.size.y) {
                return true // Encontró una plataforma
            }
        }
        return false // No hay nada en ese punto
    }
}