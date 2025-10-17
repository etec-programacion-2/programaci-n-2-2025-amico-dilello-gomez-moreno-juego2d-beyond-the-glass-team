package org.example.cli

import org.example.core.GameAction
import org.example.core.InputService
import kotlin.concurrent.thread

/**
 * Implementa InputService para una aplicación de línea de comandos.
 * Utiliza un hilo en segundo plano para capturar la entrada del teclado
 * sin bloquear el bucle principal del juego.
 */
class CLI_InputService : InputService {

    // @Volatile asegura que los cambios en esta variable sean visibles
    // instantáneamente para todos los hilos (el hilo de entrada y el hilo del juego).
    @Volatile
    private var lastAction: GameAction = GameAction.NONE

    // Esta variable controla si el hilo de entrada debe seguir ejecutándose.
    @Volatile
    private var running = false

    // Variable para almacenar la referencia al hilo que crearemos.
    private var inputThread: Thread? = null

    /**
     * Inicia el hilo en segundo plano que escuchará la entrada del teclado.
     */
    override fun start() {
        running = true
        // 'thread' es una función de ayuda de Kotlin para crear y empezar hilos fácilmente.
        inputThread = thread(start = true, isDaemon = true) {
            println("===> Controles: [A] Izquierda, [D] Derecha, [W/Espacio] Saltar, [S] Cambiar Dimensión <===")
            while (running) {
                try {
                    // readLine() se detiene aquí, pero como está en su propio hilo,
                    // no bloquea el juego principal.
                    val input = readLine()
                    if (input != null) {
                        // Mapeamos la entrada del usuario a una GameAction.
                        lastAction = when (input.lowercase()) {
                            "a" -> GameAction.MOVE_LEFT
                            "d" -> GameAction.MOVE_RIGHT
                            "w", " " -> GameAction.JUMP
                            "s" -> GameAction.SWITCH_DIMENSION // Usamos 's' para simular 'shift'
                            else -> GameAction.NONE
                        }
                    }
                } catch (e: InterruptedException) {
                    // Si el hilo es interrumpido, simplemente salimos del bucle.
                    running = false
                }
            }
        }
    }

    /**
     * Detiene el hilo de entrada de forma segura.
     */
    override fun stop() {
        running = false
        // Interrumpimos el hilo en caso de que esté bloqueado en readLine().
        inputThread?.interrupt()
    }

    /**
     * Devuelve la última acción y la "consume" (la resetea a NONE).
     * Esto evita que la misma acción se procese múltiples veces.
     */
    override fun getAction(): GameAction {
        val action = lastAction
        lastAction = GameAction.NONE // Reseteamos la acción inmediatamente
        return action
    }
}
