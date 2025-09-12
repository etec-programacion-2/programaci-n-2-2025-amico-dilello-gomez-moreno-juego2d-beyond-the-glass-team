package org.example.cli

import org.example.core.GameEngine
import org.example.core.MiJuego

// Función principal para iniciar la aplicación de línea de comandos.
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    // Se crean las implementaciones concretas para la CLI.
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()

    // Se inyectan las dependencias en el GameEngine para su ejecución.
    val consoleGame = GameEngine(cliRenderService, cliInputService, juego)

    // Lógica de inicio y simulación para la CLI.
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)

    juego.updateScore(100)
    println(juego.getGameInfo())

    // Inicia el bucle del motor del juego.
    consoleGame.run()

    println("Presione Enter para terminar...")
    readLine()

    juego.stopGame()
    println("¡Gracias por jugar!")
}
