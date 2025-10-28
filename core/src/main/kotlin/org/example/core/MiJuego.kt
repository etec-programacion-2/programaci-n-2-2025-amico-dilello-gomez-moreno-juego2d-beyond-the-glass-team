package org.example.core

class MiJuego : GameLogicService {

    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()

    // --- LÓGICA BTG-012 ---
    private val combatService: CombatService = CombatService()
    // CORRECCIÓN: Usar el objeto de la sealed class
    private var gameState: GameState = GameState.Playing 
    private var lives: Int = 3
    private var invincibilityTimer: Float = 0f
    // --------------------
    
    private var currentDimension: Dimension = Dimension.A
    private var switchKeyWasPressed = false
    private var originalPlayerStart: Vector2D = Vector2D(0f, 0f)

    override fun loadLevel(levelName: String) {
        val loader = LevelLoader()
        levelData = loader.loadLevel(levelName)
        originalPlayerStart = levelData!!.playerStart.copy()
        resetPlayerPosition()
    }

    private fun resetPlayerPosition() {
        player.position = originalPlayerStart.copy()
        player.velocity = Vector2D(0f, 0f)
        player.isOnGround = false
    }

    private fun restartGame() {
        lives = 3
        // CORRECCIÓN: Usar el objeto de la sealed class
        gameState = GameState.Playing 
        levelData?.enemies?.forEach { it.isAlive = true }
        resetPlayerPosition()
    }

    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        val currentLevel = levelData ?: return

        // CORRECCIÓN: Usar el objeto de la sealed class
        if (gameState == GameState.GameOver) {
            if (GameAction.SWITCH_DIMENSION in actions && !switchKeyWasPressed) {
                restartGame()
            }
            switchKeyWasPressed = GameAction.SWITCH_DIMENSION in actions
            return 
        }
        
        if (invincibilityTimer > 0) {
            invincibilityTimer -= deltaTime
        }

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
        physicsService.update(player, currentLevel.platforms, currentDimension, deltaTime)

        val activeEnemies = currentLevel.enemies.filter { it.isAlive }
        activeEnemies.forEach { enemy ->
            val enemyTangiblePlatforms = currentLevel.platforms.filter { it.tangibleInDimension == enemy.dimension }
            enemy.updateAI(enemyTangiblePlatforms)
            enemyPhysicsService.update(enemy, enemyTangiblePlatforms, deltaTime)
        }

        if (invincibilityTimer <= 0) {
            val combatResult = combatService.checkCombat(player, activeEnemies, currentDimension)
            
            when (combatResult) {
                CombatResult.ENEMY_KILLED -> {
                    player.velocity.y = Player.JUMP_STRENGTH * 0.7f
                }
                CombatResult.PLAYER_DAMAGED -> {
                    lives -= 1
                    invincibilityTimer = 2.0f
                    
                    if (lives <= 0) {
                        // CORRECCIÓN: Usar el objeto de la sealed class
                        gameState = GameState.GameOver 
                    } else {
                        resetPlayerPosition()
                    }
                }
                CombatResult.NONE -> { /* No pasa nada */ }
            }
        }
    }

    override fun getWorldState(): WorldState {
        return WorldState(
            player = this.player,
            platforms = levelData?.platforms ?: emptyList(),
            enemies = levelData?.enemies?.filter { it.isAlive } ?: emptyList(),
            collectibles = levelData?.collectibles ?: emptyList(),
            currentDimension = this.currentDimension,
            playerInvincible = invincibilityTimer > 0 
        )
    }

    override fun getPlayer(): Player = player
    override fun getLevelData(): LevelData? = levelData
    override fun getGameInfo(): String {
        return "Player X: ${player.position.x.toInt()} | Y: ${player.position.y.toInt()} | OnGround: ${player.isOnGround}"
    }
    
    override fun getGameState(): GameState = gameState
    override fun getLives(): Int = lives