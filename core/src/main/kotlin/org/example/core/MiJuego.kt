package org.example.core

// La implementación concreta de la lógica del juego.
class MiJuego : GameLogicService {
    private var gameState: String = "STOPPED"
    private var playerName: String = ""
    private var score: Int = 0

    override fun startGame(playerName: String) {
        this.playerName = playerName
        this.gameState = "RUNNING"
        this.score = 0
        println("Juego iniciado para: $playerName")
    }

    override fun updateScore(points: Int) {
        score += points
    }

    override fun getGameInfo(): String {
        return "Jugador: $playerName | Estado: $gameState | Puntuación: $score"
    }

    override fun stopGame() {
        gameState = "STOPPED"
        println("Juego detenido. Puntuación final: $score")
    }

    override fun isRunning(): Boolean = gameState == "RUNNING"

    override fun getScore(): Int = score
}
