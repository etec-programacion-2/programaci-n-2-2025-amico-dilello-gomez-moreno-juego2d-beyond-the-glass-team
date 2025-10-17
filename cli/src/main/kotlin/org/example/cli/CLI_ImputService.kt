package org.example.cli

import org.example.core.GameAction
import org.example.core.InputService
import java.util.concurrent.atomic.AtomicReference

class CLI_InputService : InputService {
    // Usamos AtomicReference para seguridad entre hilos.
    private val lastAction = AtomicReference(GameAction.NONE)
    private var running = true
    private val inputThread: Thread

    init {
        // El hilo se inicia en modo 'daemon' para que no impida que el programa termine.
        inputThread = Thread {
            while (running) {
                if (System.`in`.available() > 0) {
                    val input = System.`in`.read().toChar()
                    val action = when (input.lowercaseChar()) {
                        'a' -> GameAction.MOVE_LEFT
                        'd' -> GameAction.MOVE_RIGHT
                        'w', ' ' -> GameAction.JUMP
                        's' -> GameAction.SWITCH_DIMENSION
                        'q' -> GameAction.NONE // 'q' se manejará en el bucle principal para salir
                        else -> GameAction.NONE
                    }
                    lastAction.set(action)
                }
                Thread.sleep(50) // Pequeña pausa para no consumir 100% de CPU
            }
        }.apply { isDaemon = true }
    }

    override fun start() {
        println("Iniciando servicio de entrada de CLI...")
        running = true
        inputThread.start()
    }

    override fun stop() {
        println("Deteniendo servicio de entrada de CLI...")
        running = false
        inputThread.join(100) // Espera un poco a que el hilo termine
    }

    override fun getAction(): GameAction {
        // Devuelve la última acción y la "consume" (resetea a NONE).
        return lastAction.getAndSet(GameAction.NONE)
    }
}
