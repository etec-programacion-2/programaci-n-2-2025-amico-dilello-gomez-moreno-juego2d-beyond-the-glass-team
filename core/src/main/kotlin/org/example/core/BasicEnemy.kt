package org.example.core

/**
 * Implementación de un enemigo básico que patrulla (POO: Herencia).
 * Hereda de la clase abstracta Enemy.
 * (SOLID: L) Es un subtipo de 'Enemy' y puede sustituirlo.
 *
 * IA MEJORADA: Ahora detecta los bordes de las plataformas usando "sensores".
 *
 * ---
 * @see "Issue BTG-011: Implementación de Enemigos (IA)."
 * ---
 *
 * @param position Posición inicial.
 * @param size Tamaño del enemigo.
 * @param dimension Dimensión a la que pertenece.
 */
class BasicEnemy(
    position: Vector2D,
    size: Vector2D,
    dimension: Dimension
) : Enemy(position, size, dimension) {

    // Velocidad a la que patrullará este tipo de enemigo.
    private val patrolSpeed = 80f

    /**
     * Implementación de la IA (POO: Polimorfismo).
     * 1. Comprueba si hay suelo delante usando un "sensor" (feeler).
     * 2. Si no hay suelo (detecta un borde), se da la vuelta.
     * 3. Establece su velocidad basada en la dirección de patrulla.
     *
     * @param platforms Lista de todas las plataformas del nivel (para IA).
     */
    override fun updateAI(platforms: List<Platform>) {
        
        // --- INICIO LÓGICA BTG-011 (Req 1: Detección de Borde) ---
        
        // Calcula dónde estarán los "pies" del enemigo en el siguiente paso
        val feelerX: Float
        if (direction > 0) { // Moviéndose a la derecha
            // 1 píxel delante del borde derecho
            feelerX = position.x + size.x + 1 
        } else { // Moviéndose a la izquierda
            // 1 píxel delante del borde izquierdo
            feelerX = position.x - 1
        }
        // El "sensor" apunta un píxel por debajo de los pies
        val feelerY = position.y - 1 

        // Comprueba si hay una plataforma en ese punto, pero solo en la
        // dimensión a la que este enemigo pertenece.
        // --- Relacionado con BTG-009: IA consciente de la dimensión ---
        if (!isPlatformAt(feelerX, feelerY, platforms, this.dimension)) {
            // Si no hay plataforma, ¡es un borde! Darse la vuelta.
            direction *= -1f // Invierte la dirección
        }
        // --- FIN LÓGICA BTG-011 ---

        // Asigna la velocidad final basada en la dirección
        this.velocity.x = patrolSpeed * this.direction
    }

    /**
     * Función de ayuda para la IA. Comprueba si un punto (x, y)
     * está dentro de CUALQUIERA de las plataformas tangibles en la dimensión dada.
     * (POO: Encapsulamiento, es un método privado).
     */
    private fun isPlatformAt(x: Float, y: Float, platforms: List<Platform>, dimension: Dimension): Boolean {
        
        // --- Relacionado con BTG-009: IA consciente de la dimensión ---
        // Filtra la lista de plataformas para considerar solo las que
        // son tangibles en la dimensión de este enemigo.
        val tangiblePlatforms = platforms.filter { it.tangibleInDimension == dimension }

        // Recorre todas las plataformas tangibles
        for (platform in tangiblePlatforms) {
            if (x >= platform.position.x && x <= platform.position.x + platform.size.x &&
                y >= platform.position.y && y <= platform.position.y + platform.size.y) {
                return true // Encontró una plataforma en el punto
            }
        }
        return false // No hay ninguna plataforma en ese punto
    }
}