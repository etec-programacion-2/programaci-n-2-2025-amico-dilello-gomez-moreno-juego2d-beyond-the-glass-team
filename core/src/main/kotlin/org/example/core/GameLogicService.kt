package org.example.core

interface GameLogicService {
    fun loadLevel(levelName: String)
    fun update(actions: Set<GameAction>, deltaTime: Float)
    fun getPlayer(): Player
    fun getLevelData(): LevelData?
    fun getGameInfo(): String

    /**
     * Devuelve el estado actual del mundo para ser renderizado.
     * Añadido para cumplir con el Principio de Inversión de Dependencias.
     */
    fun getWorldState(): WorldState
}

