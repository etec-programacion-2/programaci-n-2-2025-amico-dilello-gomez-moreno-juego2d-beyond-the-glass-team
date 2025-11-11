package org.example.core

/**
 * (NUEVO - Parte de BTG-013)
 * Interfaz para el patrón de diseño Observador.
 * Cualquier clase que quiera "escuchar" eventos debe implementar esto.
 */
interface Observer {
    fun onNotify(event: PlayerEvent)
}