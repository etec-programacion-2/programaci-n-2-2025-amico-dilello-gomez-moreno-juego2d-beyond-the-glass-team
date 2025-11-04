package org.example.core

/**
 * Interfaz que define el "Contrato" del motor de juego (SOLID: Inversión de Dependencias).
 * El módulo 'desktop' solo interactúa con esta interfaz, no con 'MiJuego' directamente.
 * Esto permite que 'MiJuego' sea reemplazable.
 */
interface GameLogicService {
    /** Carga un nivel desde un archivo de recursos. */
    fun loadLevel(levelName: String)

    /** El bucle principal de actualización del juego. */
    fun update(actions: Set<GameAction>, deltaTime: Float)

    /** Obtiene la instancia del jugador (para referencia). */
    fun getPlayer(): Player

    /** Obtiene los datos del nivel (plataformas, etc.). */
    fun getLevelData(): LevelData?

    /** Obtiene un string de depuración. */
    fun getGameInfo(): String

    /** Obtiene el "snapshot" del estado del mundo para el renderizado. */
    fun getWorldState(): WorldState

    /** Obtiene el estado actual del juego (Playing, GameOver, etc.). */
    fun getGameState(): GameState

    /** Obtiene el número de vidas restantes. */
    fun getLives(): Int
}
