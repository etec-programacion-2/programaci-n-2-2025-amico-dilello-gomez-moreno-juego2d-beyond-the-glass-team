package org.example.core

/**
 * (Patrón Observador: Implementación del Observador Concreto)
 *
 * Esta clase implementa 'Observer' (POO: Herencia/Polimorfismo).
 * Su ÚNICA responsabilidad (SOLID: S) es escuchar eventos y
 * desbloquear habilidades para el jugador.
 *
 * ---
 * @see "Issue BTG-013: Coleccionables y Habilidades Progresivas (Patrón Observador)."
 * ---
 *
 * @param player La instancia del jugador a la que modificará.
 * @param fragmentThreshold El número de fragmentos necesarios para desbloquear.
 */
class AbilityUnlocker(
    private val player: Player, // Referencia al estado del jugador
    private val fragmentThreshold: Int = 3 // Umbral
) : Observer {

    /**
     * Implementación del método de la interfaz Observer.
     * Aquí es donde ocurre la lógica de desbloqueo.
     */
    override fun onNotify(event: PlayerEvent) {
        // Solo nos interesa el evento de recolección
        when (event) {
            PlayerEvent.ENERGY_COLLECTED -> {
                // Comprueba si se ha alcanzado el umbral Y si la habilidad no está ya desbloqueada
                if (player.energyFragments >= fragmentThreshold && !player.canDoubleJump) {
                    
                    // ¡HABILIDAD DESBLOQUEADA!
                    // Modifica directamente el estado del jugador (que es una 'data class').
                    player.canDoubleJump = true
                    
                    // (Aquí se podría notificar a un servicio de UI, reproducir un sonido, etc.)
                    println("¡DOBLE SALTO DESBLOQUEADO!")
                }
            }
        }
    }
}