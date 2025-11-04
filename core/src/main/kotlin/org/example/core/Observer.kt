package org.example.core

/**
 * (Patrón Observador: Interfaz del Observador)
 *
 * Define el contrato para cualquier clase que quiera "escuchar"
 * eventos del juego.
 */
interface Observer {
    /**
     * El "Subject" llama a este método en todos sus observadores
     * cuando ocurre un evento.
     * @param event El evento que acaba de ocurrir.
     */
    fun onNotify(event: PlayerEvent)
}
