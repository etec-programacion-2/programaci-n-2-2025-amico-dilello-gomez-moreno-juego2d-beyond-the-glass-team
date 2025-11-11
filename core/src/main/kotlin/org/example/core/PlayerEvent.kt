package org.example.core

/**
 * (Patrón Observador: Evento)
 *
 * Define los tipos de eventos que el "Subject" (MiJuego) puede notificar
 * a sus "Observers" (AbilityUnlocker).
 */
enum class PlayerEvent {
    ENERGY_COLLECTED // Evento para cuando se recoge un fragmento
}
