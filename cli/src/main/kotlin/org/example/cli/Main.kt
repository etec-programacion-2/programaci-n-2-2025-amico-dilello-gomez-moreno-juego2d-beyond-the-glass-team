package org.example.cli

import org.example.core.GameEngine
import org.example.core.MiJuego
import org.example.core.RenderService
import org.example.core.InputService

/**
 * Función principal para iniciar la aplicación de línea de comandos.
 * Este archivo actúa como el "lanzador" de la aplicación CLI.
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    // Se crean las instancias concretas de los servicios específicos para la CLI.
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()

    // Se crea una instancia de la lógica del juego, que vive en el módulo core.
    val juego = MiJuego()

    // Se inyectan las dependencias (los servicios y la lógica del juego) en el GameEngine.
    val consoleGame = GameEngine(cliRenderService, cliInputService, juego)

    // Lógica de inicio y simulación para la CLI.
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)

    juego.updateScore(100)

    // La llamada a render() se ejecuta una sola vez para mostrar el estado actual.
    // No se utiliza el bucle del GameEngine, ya que causaría un ciclo infinito en la CLI.
    consoleGame.renderService.render()
    println(juego.getGameInfo())

    println("Presione Enter para terminar...")
    readLine()

    juego.stopGame()
    println("¡Gracias por jugar!")
}