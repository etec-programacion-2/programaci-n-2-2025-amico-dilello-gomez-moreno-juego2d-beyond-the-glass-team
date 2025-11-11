package org.example.core

interface GameLogicService {
    fun loadLevel(levelName: String)
    fun update(actions: Set<GameAction>, deltaTime: Float)
    fun getPlayer(): Player
    fun getLevelData(): LevelData?
    fun getGameInfo(): String
    fun getWorldState(): WorldState

    fun getGameState(): GameState
    fun getLives(): Int

    // --- MÉTODOS DE BTG-013 (LOS QUE FALTABAN) ---
    fun getPlayerFragments(): Int
    fun canPlayerDoubleJump(): Boolean
}