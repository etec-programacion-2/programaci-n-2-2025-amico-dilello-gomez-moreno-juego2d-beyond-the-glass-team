package org.example.core

class MiJuego : GameLogicService {

    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()
    
    private var currentDimension: Dimension = Dimension.A
    private var switchKeyWasPressed = false

    override fun loadLevel(levelName: String) {
        val loader = LevelLoader()
        levelData = loader.loadLevel(levelName)
        player.position = levelData!!.playerStart.copy()
    }

    /**
     * El método 'update' ahora recibe un Set de acciones.
     */
    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        val currentLevel = levelData ?: return

        // --- LÓGICA DE MOVIMIENTO ---
        // Si no hay acciones de movimiento, la velocidad X es 0.
        player.velocity.x = 0f
        
        if (GameAction.MOVE_LEFT in actions) {
            player.velocity.x = -Player.MOVE_SPEED
        }
        if (GameAction.MOVE_RIGHT in actions) {
            player.velocity.x = Player.MOVE_SPEED
        }

        // --- LÓGICA DE SALTO ---
        if (GameAction.JUMP in actions && player.isOnGround) {
            player.velocity.y = Player.JUMP_STRENGTH
            player.isOnGround = false
        }
        
        // --- LÓGICA DE CAMBIO DE DIMENSIÓN (MEJORADA) ---
        // Comprueba si la acción de cambiar está en el conjunto
        if (GameAction.SWITCH_DIMENSION in actions) {
            if (!switchKeyWasPressed) {
                currentDimension = if (currentDimension == Dimension.A) Dimension.B else Dimension.A
                switchKeyWasPressed = true
            }
        } else {
            // Si la tecla de cambiar NO está en el conjunto (se soltó), libera el seguro.
            // Esto funciona incluso si otras teclas siguen presionadas.
            switchKeyWasPressed = false
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
        return "Player X: ${player.position.x.toInt()} | Y: ${player.position.y.toInt()} | OnGround: ${player.isOnGround}"
    }
}

