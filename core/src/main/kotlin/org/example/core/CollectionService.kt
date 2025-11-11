package org.example.core

/**
 * (NUEVO - Parte de BTG-013)
 * Servicio de Responsabilidad Única (SOLID) para manejar la lógica
 * de recolección de coleccionables.
 */
class CollectionService {

    /**
     * Comprueba si el jugador está colisionando con algún coleccionable
     * que NO haya sido recogido ya.
     * @return Lista de coleccionables que el jugador ha tocado este frame.
     */
    fun checkCollection(player: Player, collectibles: List<Collectible>): List<Collectible> {
        val collectedNow = mutableListOf<Collectible>()
        
        // Filtrar solo los que se pueden recoger
        val availableCollectibles = collectibles.filter { !it.isCollected }

        for (collectible in availableCollectibles) {
            if (isAABBColliding(player.position, player.size, collectible.position, collectible.size)) {
                collectedNow.add(collectible)
            }
        }
        return collectedNow
    }

    private fun isAABBColliding(posA: Vector2D, sizeA: Vector2D, posB: Vector2D, sizeB: Vector2D): Boolean {
        return posA.x < posB.x + sizeB.x &&
               posA.x + sizeA.x > posB.x &&
               posA.y < posB.y + sizeB.y &&
               posA.y + sizeA.y > posB.y
    }
}