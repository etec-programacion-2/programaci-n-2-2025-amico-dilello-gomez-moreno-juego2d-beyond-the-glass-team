package org.example.core

interface GameLogicService {
    fun loadLevel(levelName: String)
    fun update(actions: Set<GameAction>, deltaTime: Float)
    fun getPlayer(): Player
    fun getLevelData(): LevelData?
    fun getGameInfo(): String
    fun getWorldState(): WorldState

    // --- NUEVO ---
    fun getGameState(): GameState
    fun getLives(): Int
}

