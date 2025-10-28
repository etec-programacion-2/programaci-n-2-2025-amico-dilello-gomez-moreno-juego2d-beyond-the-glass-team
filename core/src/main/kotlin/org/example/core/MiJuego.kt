package org.example.core

class MiJuego : GameLogicService {

    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()
    
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()
    
    private var currentDimension: Dimension = Dimension.A
    private var switchKeyWasPressed = false

    override fun loadLevel(levelName: String) {
        val loader = LevelLoader()
        levelData = loader.loadLevel(levelName)
        player.position = levelData!!.playerStart.copy()
    }

    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        val currentLevel = levelData ?: return

        // --- Lógica del Jugador ---
        // (El código del jugador no cambia... se omite por brevedad)
        player.velocity.x = 0f
        if (GameAction.MOVE_LEFT in actions) player.velocity.x = -Player.MOVE_SPEED
        if (GameAction.MOVE_RIGHT in actions) player.velocity.x = Player.MOVE_SPEED
        if (GameAction.JUMP in actions && player.isOnGround) {
            player.velocity.y = Player.JUMP_STRENGTH
            player.isOnGround = false
        }
        if (GameAction.SWITCH_DIMENSION in actions) {
            if (!switchKeyWasPressed) {
                currentDimension = if (currentDimension == Dimension.A) Dimension.B else Dimension.A
                switchKeyWasPressed = true
            }
        } else {
            switchKeyWasPressed = false
        }
        // La física del JUGADOR usa la dimensión del JUGADOR
        physicsService.update(player, currentLevel.platforms, currentDimension, deltaTime)

        // --- INICIO LÓGICA BTG-011 (Req 3: Movimiento Independiente) ---
        // Actualiza todos los enemigos
        currentLevel.enemies.forEach { enemy ->
            // El enemigo ahora SIEMPRE se actualiza.
            
            // 1. Filtra las plataformas que son relevantes para la dimensión de este ENEMIGO
            val enemyTangiblePlatforms = currentLevel.platforms.filter { it.tangibleInDimension == enemy.dimension }

            // 2. Ejecuta la IA del enemigo (que ahora usa la detección de bordes)
            enemy.updateAI(enemyTangiblePlatforms)
            
            // 3. Ejecuta la física del enemigo (gravedad, colisiones)
            //    usando solo las plataformas de SU PROPIA dimensión.
            enemyPhysicsService.update(enemy, enemyTangiblePlatforms, deltaTime)
        }
        // --- FIN LÓGICA BTG-011 ---
    }

    override fun getWorldState(): WorldState {
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