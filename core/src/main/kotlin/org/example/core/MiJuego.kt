package org.example.core

import org.example.core.GameState 
import org.example.core.GameState.*
import org.example.core.GameLogicService 
import org.example.core.Player
import org.example.core.LevelData
/**
 * SRP/Facade: Orquestador central del juego. Implementa GameLogicService.
 * Delega responsabilidades de Input y Física.
 */
class MiJuego : GameLogicService { // IMPLEMENTA la interfaz GameLogicService
    
    // 1. EL ESTADO USA EL SEALED CLASS GameState.
    private var GameState: GameState = GAMEOVER 
    private var playerName: String = ""
    private var score: Int = 0
    
    // --- DEPENDENCIAS INTERNAS ---
    // Son instancias del core, no violan DIP.
    private lateinit var player: Player 
    private lateinit var currentLevel: LevelData
    private val physicsEngine = PhysicsEngine() 
    private val inputProcessor = PlayerInputProcessor() 
    // -----------------------------

    // Implementaciones de GameLogicService
    override fun startGame(playerName: String) {
        this.playerName = playerName
        this.GameState = PLAYING   
        this.score = 0
        println("Juego iniciado para: $playerName")
    }
    
    /**
     * Inicializa el jugador y el nivel con los datos cargados.
     */
    fun initializeLevel(levelData: LevelData) {
        this.currentLevel = levelData
        // Inicialización del Player con los datos de LevelData.
        this.player = Player(
            size = Vector2D(30f, 50f), 
            position = Vector2D(levelData.playerStart.x, levelData.playerStart.y) 
        )
        this.GameState = PLAYING
        println("Nivel cargado e inicializado.")
    }

    /**
     * MÉTODO CLAVE: Contiene la lógica principal del juego.
     */
    fun updateGame(deltaTime: Float, inputService: InputService) {
        if (GameState != PLAYING) return
        if (!::player.isInitialized || !::currentLevel.isInitialized) return

        // 1. INPUT
        inputProcessor.processInput(player, inputService) 

        // 2. FÍSICA
        physicsEngine.updatePlayerPhysics(player, currentLevel.platforms, deltaTime)
        
        // 3. Lógica de juego (coleccionables, enemigos, etc.)
    }
    
    override fun updateScore(points: Int) { score += points }
    
    override fun getGameInfo(): String {
        val playerInfo = if (::player.isInitialized) 
            "| Pos: (${player.position.x.toInt()}, ${player.position.y.toInt()}) | Suelo: ${if(player.isOnGround) "SÍ" else "NO"}" 
        else ""
        return "Jugador: $playerName | Estado: $GameState | Puntuación: $score $playerInfo"
    }
    
    override fun stopGame() {
        GameState = GAMEOVER
        println("Juego detenido. Puntuación final: $score")
    }
    
    override fun isRunning(): Boolean = GameState == PLAYING
    override fun getScore(): Int = score
    fun getPlayer(): Player? = if (::player.isInitialized) player else null
}