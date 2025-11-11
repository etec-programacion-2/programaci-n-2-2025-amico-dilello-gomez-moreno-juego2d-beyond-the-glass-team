package org.example.core

/**
 * (NUEVO - Parte de BTG-013)
 * Interfaz para el patrón de diseño Observador (el "Sujeto").
 * Mantiene una lista de Observadores y les notifica de eventos.
 */
class Subject {
    private val observers = mutableListOf<Observer>()

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    fun notify(event: PlayerEvent) {
        observers.forEach { it.onNotify(event) }
    }
}