package org.example.core

// La implementación concreta de la lógica del juego.
class MiJuego : GameLogicService {
    private var gameState: String = "STOPPED"
    private var playerName: String = ""
    private var score: Int = 0

    // Estado del mundo del juego
    private var player: Player? = null
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()

    override fun startGame(playerName: String) {
        this.playerName = playerName
        this.gameState = "RUNNING"
        this.score = 0
    }

    // CORREGIDO: Se añade 'override'
    override fun loadLevel(data: LevelData) {
        this.levelData = data
        this.player = Player(
            position = data.playerStart.copy(),
            size = Vector2D(32f, 64f),
            currentDimension = Dimension.A
        )
    }
    
    // CORREGIDO: Se añade 'override'
    override fun update(inputService: InputService, deltaTime: Float) {
        if (gameState != "RUNNING" || player == null || levelData == null) return

        player?.let { p ->
            p.velocity.x = 0f
            if (inputService.isActionPressed(PlayerAction.MOVE_LEFT) || inputService.isActionPressed(PlayerAction.MOVE_LEFT_ALT)) {
                p.velocity.x = -Player.MOVE_SPEED
            }
            if (inputService.isActionPressed(PlayerAction.MOVE_RIGHT) || inputService.isActionPressed(PlayerAction.MOVE_RIGHT_ALT)) {
                p.velocity.x = Player.MOVE_SPEED
            }

            if (p.isOnGround && (inputService.isActionPressed(PlayerAction.JUMP) || inputService.isActionPressed(PlayerAction.JUMP_ALT))) {
                p.velocity.y = Player.JUMP_FORCE
                p.isOnGround = false
            }
        }

        player?.let { p ->
            levelData?.platforms?.let { platforms ->
                physicsService.update(p, platforms, deltaTime)
            }
        }
    }

    override fun updateScore(points: Int) {
        score += points
    }

    override fun getGameInfo(): String {
        return "Jugador: $playerName | Estado: $gameState | Puntuación: $score"
    }

    override fun stopGame() {
        gameState = "STOPPED"
    }

    override fun isRunning(): Boolean = gameState == "RUNNING"
    override fun getScore(): Int = score

    // CORREGIDO: Se añade 'override'
    override fun getPlayer(): Player? = player
    
    // CORREGIDO: Se añade 'override'
    override fun getLevelData(): LevelData? = levelData
}

