package org.example.core

/**
 * Servicio dedicado exclusivamente a la lógica de combate (SOLID: Principio de Responsabilidad Única).
 * Maneja las colisiones de ataque y daño.
 */
class CombatService {

    /**
     * Comprueba si el ataque activo del jugador (hitbox) golpea a algún enemigo.
     *
     * @param player El objeto jugador.
     * @param enemies La lista de todos los enemigos.
     * @param currentDimension La dimensión actual del juego.
     * @return Una lista de enemigos que fueron golpeados.
     */
    fun checkPlayerAttack(player: Player, enemies: List<Enemy>, currentDimension: Dimension): List<Enemy> {
        // Si el jugador no está atacando, no puede golpear a nadie.
        if (!player.isAttacking) {
            return emptyList()
        }

        val hitEnemies = mutableListOf<Enemy>()
        // Calcula la posición y tamaño del hitbox de ataque
        val hitbox = getAttackHitbox(player)

        // Filtra enemigos: solo puede golpear a enemigos VIVOS y en la MISMA DIMENSIÓN.
        val attackableEnemies = enemies.filter { it.isAlive && it.dimension == currentDimension }

        // Comprueba la colisión del hitbox con cada enemigo atacable
        for (enemy in attackableEnemies) {
            if (isAABBColliding(hitbox.first, hitbox.second, enemy.position, enemy.size)) {
                hitEnemies.add(enemy)
            }
        }
        return hitEnemies
    }

    /**
     * Comprueba si el cuerpo del jugador está colisionando con algún enemigo activo.
     * (Usado para detectar si el jugador recibe daño).
     *
     * @param player El objeto jugador.
     * @param enemies La lista de todos los enemigos.
     * @param currentDimension La dimensión actual del juego.
     * @return 'true' si el jugador está tocando a un enemigo dañino, 'false' en caso contrario.
     */
    fun checkEnemyDamage(player: Player, enemies: List<Enemy>, currentDimension: Dimension): Boolean {
        // Filtra enemigos: solo puede recibir daño de enemigos VIVOS y en la MISMA DIMENSIÓN.
        val activeEnemies = enemies.filter { it.isAlive && it.dimension == currentDimension }

        for (enemy in activeEnemies) {
            // Comprueba colisión AABB entre el cuerpo del jugador y el cuerpo del enemigo
            if (isAABBColliding(player.position, player.size, enemy.position, enemy.size)) {
                return true // El jugador ha sido golpeado
            }
        }
        return false // El jugador está a salvo
    }

    /**
     * Calcula la posición y el tamaño del hitbox de ataque del jugador.
     * (POO: Encapsulamiento, es un método público que expone lógica interna).
     *
     * @param player El objeto jugador.
     * @return Un 'Pair' donde 'first' es la posición (Vector2D) y 'second' es el tamaño (Vector2D).
     */
    fun getAttackHitbox(player: Player): Pair<Vector2D, Vector2D> {
        val hitboxPos = player.position.copy()
        val hitboxSize = Player.ATTACK_HITBOX

        // El hitbox se coloca delante del jugador, dependiendo de hacia dónde mira
        if (player.facingDirection > 0) { // Mirando a la derecha
            hitboxPos.x += player.size.x
        } else { // Mirando a la izquierda
            hitboxPos.x -= hitboxSize.x
        }
        return Pair(hitboxPos, hitboxSize)
    }

    /**
     * Comprobación de colisión AABB (Axis-Aligned Bounding Box).
     * Devuelve true si dos rectángulos se superponen.
     */
    private fun isAABBColliding(posA: Vector2D, sizeA: Vector2D, posB: Vector2D, sizeB: Vector2D): Boolean {
        return posA.x < posB.x + sizeB.x &&
               posA.x + sizeA.x > posB.x &&
               posA.y < posB.y + sizeB.y &&
               posA.y + sizeA.y > posB.y
    }
}