package org.example.core

/**
 * (NUEVO - Parte de BTG-013)
 * Este es el Observador (Observer).
 * Escucha eventos del jugador y desbloquea habilidades cuando se cumplen
 * las condiciones.
 */
class AbilityUnlocker(private val player: Player) : Observer {

    private var doubleJumpUnlocked = false

    override fun onNotify(event: PlayerEvent) {
        when (event) {
            PlayerEvent.FragmentCollected -> checkEnergyFragments()
            // Se podrían añadir más eventos aquí, ej: PlayerEvent.BossDefeated
        }
    }

    /**
     * Comprueba si el jugador tiene suficientes fragmentos para desbloquear
     * el doble salto.
     */
    private fun checkEnergyFragments() {
        // --- LÓGICA DE BTG-013 (AHORA DEBERÍA COMPILAR) ---
        if (!doubleJumpUnlocked && player.energyFragments >= Player.FRAGMENTS_TO_UNLOCK) {
            player.canDoubleJump = true
            doubleJumpUnlocked = true
            // Podríamos notificar a otro sistema (ej. de UI) que muestre un mensaje
        }
    }
}