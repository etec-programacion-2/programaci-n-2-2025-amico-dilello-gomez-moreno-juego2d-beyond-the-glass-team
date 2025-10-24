package org.example.core

interface InputService {
    fun start()
    fun stop()
    
    /**
     * Obtiene TODAS las acciones que el usuario está realizando este fotograma.
     * @return Un Set de GameActions. Estará vacío si no hay ninguna acción.
     */
    fun getActions(): Set<GameAction>
}
