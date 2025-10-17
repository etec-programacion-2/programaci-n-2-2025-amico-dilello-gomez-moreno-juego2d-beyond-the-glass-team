package org.example.core

/**
 * Define la interfaz para los servicios de entrada de usuario.
 * Ahora se basa en "acciones" y debe poder iniciarse y detenerse.
 */
interface InputService {
    /**
     * Inicia el servicio (por ejemplo, el hilo que escucha el teclado).
     */
    fun start()

    /**
     * Detiene el servicio de forma segura.
     */
    fun stop()

    /**
     * Obtiene la última acción solicitada por el usuario.
     * Es responsabilidad de esta función "consumir" la acción,
     * para que no se devuelva la misma acción dos veces.
     */
    fun getAction(): GameAction
}
