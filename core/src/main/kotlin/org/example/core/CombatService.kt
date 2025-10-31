package org.example.core

class CombatService {

    /**
     * Comprueba si el ataque activo del jugador golpea a algún enemigo.
     * CORRECCIÓN: Ahora filtra por la dimensión actual.
     */
    fun checkPlayerAttack(player: Player, enemies: List<Enemy>, currentDimension: Dimension): List<Enemy> {
        if (!player.isAttacking) {
            return emptyList()
        }

        val hitEnemies = mutableListOf<Enemy>()
        val hitbox = getAttackHitbox(player)

        // CORRECCIÓN: Filtra solo enemigos vivos Y en la dimensión del jugador
        val attackableEnemies = enemies.filter { it.isAlive && it.dimension == currentDimension }

        for (enemy in attackableEnemies) {
            if (isAABBColliding(hitbox.first, hitbox.second, enemy.position, enemy.size)) {
                hitEnemies.add(enemy)
            }
        }
        return hitEnemies
    }

    /**
     * Comprueba si el jugador está colisionando con algún enemigo activo.
     */
    fun checkEnemyDamage(player: Player, enemies: List<Enemy>, currentDimension: Dimension): Boolean {
        // Este filtro ya era correcto: comprueba enemigos vivos Y en la dimensión del jugador
        val activeEnemies = enemies.filter { it.isAlive && it.dimension == currentDimension }

        for (enemy in activeEnemies) {
            if (isAABBColliding(player.position, player.size, enemy.position, enemy.size)) {
                return true
            }
        }
        return false
    }

    fun getAttackHitbox(player: Player): Pair<Vector2D, Vector2D> {
        val hitboxPos = player.position.copy()
        val hitboxSize = Player.ATTACK_HITBOX

        if (player.facingDirection > 0) {
            hitboxPos.x += player.size.x
        } else {
            hitboxPos.x -= hitboxSize.x
        }
        return Pair(hitboxPos, hitboxSize)
    }

    private fun isAABBColliding(posA: Vector2D, sizeA: Vector2D, posB: Vector2D, sizeB: Vector2D): Boolean {
        return posA.x < posB.x + sizeB.x &&
               posA.x + sizeA.x > posB.x &&
               posA.y < posB.y + sizeB.y &&
               posA.y + sizeA.y > posB.y
    }
}

