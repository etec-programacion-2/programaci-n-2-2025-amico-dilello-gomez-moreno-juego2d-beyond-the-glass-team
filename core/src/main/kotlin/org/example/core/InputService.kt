package org.example.core

/**
 * Interfaz para los servicios de entrada (SOLID: Inversión de Dependencias).
 * Define el "contrato" de CÓMO el 'core' (MiJuego) recibe la entrada.
 * No sabe si la entrada viene de un teclado (GdxInputService) o una consola (CliInputService).
 *
 * ---
 * @see "Issue BTG-002: Diseño de la arquitectura de servicios (DIP)."
 * ---
 */
interface InputService {
    /** Inicia el servicio (si es necesario). */
    fun start()
    /** Detiene el servicio (si es necesario). */
    fun stop()
    
    /**
     * Obtiene TODAS las acciones que el usuario está realizando este fotograma.
     * @return Un Set de GameActions. Usar un Set permite manejar múltiples
     * acciones simultáneas (ej. saltar y moverse a la derecha).
     */
    fun getActions(): Set<GameAction>
}