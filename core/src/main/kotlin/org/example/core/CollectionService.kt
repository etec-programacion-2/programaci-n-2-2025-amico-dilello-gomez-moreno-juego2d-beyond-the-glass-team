package org.example.core

/**
 * Servicio dedicado a la lógica de recolección de objetos
 * (SOLID: Principio de Responsabilidad Única).
 *
 * ---
 * @see "Issue BTG-013: Coleccionables y Habilidades Progresivas."
 * ---
 */
class CollectionService {

    /**
     * Comprueba si el jugador está colisionando con algún coleccionable activo.
     *
     * @param player El objeto jugador.
     * @param collectibles La lista de TODOS los coleccionables.
     * @return Una lista de los coleccionables que el jugador acaba de tocar.
     */
    fun checkCollectibleCollisions(player: Player, collectibles: List<Collectible>): List<Collectible> {
        val collectedItems = mutableListOf<Collectible>()

        // Filtra solo los que NO han sido recogidos (isCollected = false)
        val activeCollectibles = collectibles.filter { !it.isCollected }

        for (collectible in activeCollectibles) {
            // Comprueba colisión AABB
            // --- Relacionado con BTG-010: Física y Colisiones ---
            if (isAABBColliding(player.position, player.size, collectible.position, collectible.size)) {
                collectedItems.add(collectible)
            }
        }
        return collectedItems
    }

    /**
     * Comprobación de colisión AABB (Axis-Aligned Bounding Box).
     * (POO: Encapsulamiento) Privado para este servicio.
     */
    private fun isAABBColliding(posA: Vector2D, sizeA: Vector2D, posB: Vector2D, sizeB: Vector2D): Boolean {
        return posA.x < posB.x + sizeB.x &&
               posA.x + sizeA.x > posB.x &&
               posA.y < posB.y + sizeB.y &&
               posA.y + sizeA.y > posB.y
    }
}