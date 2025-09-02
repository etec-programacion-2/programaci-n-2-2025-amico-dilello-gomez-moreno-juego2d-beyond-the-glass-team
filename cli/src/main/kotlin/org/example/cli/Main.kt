package org.example.cli

import org.example.core.MiJuego

/**
 * Interfaz de consola - USA la lógica del core
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")
    
    // Usar la lógica del core
    val juego = MiJuego()
    
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    
    juego.startGame(nombre)
    
    // Simular algunas acciones del juego
    juego.updateScore(100)
    println(juego.getGameInfo())
    
    juego.updateScore(50)
    println(juego.getGameInfo())
    
    println("Presione Enter para terminar...")
    readLine()
    
    juego.stopGame()
    println("¡Gracias por jugar!")
}