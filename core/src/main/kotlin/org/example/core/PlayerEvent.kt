package org.example.core

/**
 * (NUEVO - Parte de BTG-013)
 * Define los tipos de eventos que el 'Subject' (MiJuego) puede notificar
 * a los 'Observer' (AbilityUnlocker).
 */
sealed class PlayerEvent {
    object FragmentCollected : PlayerEvent()
    // object PlayerDied : PlayerEvent()
    // object BossDefeated : PlayerEvent()
}