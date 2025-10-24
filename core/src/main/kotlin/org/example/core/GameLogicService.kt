package org.example.core

interface GameLogicService {
    fun loadLevel(levelName: String)
    /**
     * Actualiza la lógica del juego basándose en un conjunto de acciones.
     */
    fun update(actions: Set<GameAction>, deltaTime: Float)
    fun getPlayer(): Player
    fun getLevelData(): LevelData?
    fun getGameInfo(): String
}
