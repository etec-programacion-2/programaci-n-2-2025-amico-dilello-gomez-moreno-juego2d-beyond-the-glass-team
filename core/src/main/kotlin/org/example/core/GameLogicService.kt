package org.example.core

interface GameLogicService {
    fun loadLevel(levelName: String)
    fun update(action: GameAction, deltaTime: Float)
    fun getPlayer(): Player
    fun getLevelData(): LevelData?
    // Método que añadieron tus compañeros
    fun getGameInfo(): String
}

