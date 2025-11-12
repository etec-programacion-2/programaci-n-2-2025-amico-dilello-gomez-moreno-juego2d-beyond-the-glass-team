package org.example.core

/**
 * (Patrón Observador: El Sujeto / Observable)
 *
 * Esta clase gestiona una lista de Observadores.
 * El motor del juego (MiJuego) tendrá una instancia de esta clase.
 *
 * (POO: Encapsulamiento) Oculta la lista de observadores y el
 * proceso de notificación.
 *
 * ---
 * @see "Issue BTG-013: Patrón de Diseño Observador."
 * ---
 */
class Subject {
    
    // Lista privada de todos los observadores registrados
    private val observers = mutableListOf<Observer>()

    /**
     * Permite a un objeto registrarse para recibir notificaciones.
     * @param observer El objeto que quiere "escuchar" (ej. AbilityUnlocker).
     */
    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    /**
     * Permite a un objeto dejar de recibir notificaciones.
     * @param observer El objeto que quiere "darse de baja".
     */
    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    /**
     * El método clave. Notifica a TODOS los observadores registrados
     * sobre un nuevo evento.
     * (Llamado por 'MiJuego' cuando se recoge un coleccionable).
     *
     * @param event El evento que acaba de ocurrir.
     */
    fun notify(event: PlayerEvent) {
        // Llama a onNotify() en cada observador de la lista
        observers.forEach { it.onNotify(event) }
    }
}