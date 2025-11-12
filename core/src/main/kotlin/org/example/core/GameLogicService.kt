package org.example.core

/**
 * Interfaz que define el "Contrato" del motor de juego (SOLID: Inversión de Dependencias).
 * El módulo 'desktop' (DesktopGame) solo interactúa con esta interfaz,
 * no con 'MiJuego' directamente.
 * Esto permite que 'MiJuego' sea reemplazable.
 *
 * ---
 * @see "Issue BTG-002: Diseño de la arquitectura de servicios (DIP)."
 * ---
 */
interface GameLogicService {
    /** Carga un nivel desde un archivo de recursos. (BTG-007) */
    fun loadLevel(levelName: String)

    /** El bucle principal de actualización del juego. (BTG-008) */
    fun update(actions: Set<GameAction>, deltaTime: Float)

    /** Obtiene la instancia del jugador (para referencia). (BTG-006) */
    fun getPlayer(): Player

    /** Obtiene los datos del nivel (plataformas, etc.). (BTG-007) */
    fun getLevelData(): LevelData?

    /** Obtiene un string de depuración. */
    fun getGameInfo(): String

    /** Obtiene el "snapshot" del estado del mundo para el renderizado. (BTG-002, BTG-008) */
    fun getWorldState(): WorldState

    /** Obtiene el estado actual del juego (Playing, GameOver, etc.). (BTG-008, BTG-012) */
    fun getGameState(): GameState

    /** Obtiene el número de vidas restantes. (BTG-012) */
    fun getLives(): Int
}