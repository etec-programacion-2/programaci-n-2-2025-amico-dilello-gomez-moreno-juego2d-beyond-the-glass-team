package org.example.core

class MiJuego : GameLogicService {

    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()
    private var currentDimension: Dimension = Dimension.A

    override fun loadLevel(levelName: String) {
        val loader = LevelLoader()
        levelData = loader.loadLevel(levelName)
        player.position = levelData!!.playerStart.copy()
    }

    override fun update(action: GameAction, deltaTime: Float) {
        val currentLevel = levelData ?: return

        // El 'when' ahora cubre TODOS los casos del enum GameAction
        when (action) {
            GameAction.MOVE_LEFT -> player.velocity.x = -Player.MOVE_SPEED
            GameAction.MOVE_RIGHT -> player.velocity.x = Player.MOVE_SPEED
            GameAction.JUMP -> if (player.isOnGround) {
                player.velocity.y = Player.JUMP_STRENGTH
                player.isOnGround = false
            }
            GameAction.SWITCH_DIMENSION -> {
                // Futura lógica de cambio de dimensión
            }
            // --- CASO AÑADIDO ---
            // Le decimos al compilador que sabemos de QUIT, pero no hacemos nada aquí.
            GameAction.QUIT -> { /* La lógica de salida se maneja en la plataforma (cli/desktop) */ }
            GameAction.NONE -> player.velocity.x = 0f
        }
        
        physicsService.update(player, currentLevel.platforms, currentDimension, deltaTime)
    }

    fun getWorldState(): WorldState {
        return WorldState(
            player = this.player,
            platforms = levelData?.platforms ?: emptyList(),
            enemies = levelData?.enemies ?: emptyList(),
            collectibles = levelData?.collectibles ?: emptyList(),
            currentDimension = this.currentDimension
        )
    }

    override fun getPlayer(): Player = player
    override fun getLevelData(): LevelData? = levelData
    override fun getGameInfo(): String {
        return "Player X: ${player.position.x.toInt()} | Player Y: ${player.position.y.toInt()}"
    }
}
