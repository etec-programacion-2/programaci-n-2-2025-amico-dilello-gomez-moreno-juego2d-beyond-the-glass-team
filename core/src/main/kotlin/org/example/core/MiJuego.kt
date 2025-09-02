package org.example.core

/**
 * Lógica central del juego - SIN dependencias de UI
 */
class MiJuego {
    private var gameState: String = "STOPPED"
    private var playerName: String = ""
    private var score: Int = 0
    
    fun startGame(playerName: String) {
        this.playerName = playerName
        this.gameState = "RUNNING"
        this.score = 0
        println("Juego iniciado para: $playerName")
    }
    
    fun updateScore(points: Int) {
        score += points
    }
    
    fun getGameInfo(): String {
        return "Jugador: $playerName | Estado: $gameState | Puntuación: $score"
    }
    
    fun stopGame() {
        gameState = "STOPPED"
        println("Juego detenido. Puntuación final: $score")
    }
    
    fun isRunning(): Boolean = gameState == "RUNNING"
    
    fun getScore(): Int = score
}