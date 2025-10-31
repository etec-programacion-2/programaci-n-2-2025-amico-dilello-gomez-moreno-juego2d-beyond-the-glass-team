package org.example.core

class MiJuego : GameLogicService {

    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()
    private val combatService: CombatService = CombatService()
    
    private var gameState: GameState = GameState.Playing
    private var lives: Int = 3
    private var invincibilidadTimer: Float = 0f
    
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
        gameState = GameState.Playing
        levelData?.enemies?.forEach { it.isAlive = true }
        resetPlayerPosition()
    }

    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        val currentLevel = levelData ?: return

        if (gameState == GameState.GameOver) {
            if (GameAction.SWITCH_DIMENSION in actions && !switchKeyWasPressed) restartGame()
            switchKeyWasPressed = GameAction.SWITCH_DIMENSION in actions
            return 
        }
        
        if (invincibilidadTimer > 0) invincibilidadTimer -= deltaTime

        updatePlayerState(actions, deltaTime)
        physicsService.update(player, currentLevel.platforms, currentDimension, deltaTime)

        val allLivingEnemies = currentLevel.enemies.filter { it.isAlive }
        allLivingEnemies.forEach { enemy ->
            val enemyTangiblePlatforms = currentLevel.platforms.filter { it.tangibleInDimension == enemy.dimension }
            enemy.updateAI(enemyTangiblePlatforms)
            enemyPhysicsService.update(enemy, enemyTangiblePlatforms, deltaTime)
        }

        // --- LÓGICA DE COMBATE CORREGIDA ---
        
        // 1. Comprobar si el jugador golpea a un enemigo
        // CORRECCIÓN: Pasamos la dimensión actual para filtrar enemigos "fantasma"
        val hitEnemies = combatService.checkPlayerAttack(player, allLivingEnemies, currentDimension)
        hitEnemies.forEach { it.isAlive = false } 

        // 2. Comprobar si un enemigo golpea al jugador
        if (invincibilidadTimer <= 0) {
            // Pasamos allLivingEnemies, el servicio filtra por dimensión
            val playerDamaged = combatService.checkEnemyDamage(player, allLivingEnemies, currentDimension)
            if (playerDamaged) {
                handlePlayerDamage()
            }
        }
    }

    private fun updatePlayerState(actions: Set<GameAction>, deltaTime: Float) {
        player.velocity.x = 0f
        if (GameAction.MOVE_LEFT in actions) {
            player.velocity.x = -Player.MOVE_SPEED
            player.facingDirection = -1f
        }
        if (GameAction.MOVE_RIGHT in actions) {
            player.velocity.x = Player.MOVE_SPEED
            player.facingDirection = 1f
        }

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

        if (player.attackTimer > 0) {
            player.attackTimer -= deltaTime
        }

        // El hitbox solo está activo durante la duración del ataque
        if (player.isAttacking && player.attackTimer <= (Player.ATTACK_COOLDOWN - Player.ATTACK_DURATION)) {
            player.isAttacking = false 
        }

        if (GameAction.ATTACK in actions && player.attackTimer <= 0) {
            player.isAttacking = true
            player.attackTimer = Player.ATTACK_COOLDOWN // Inicia el cooldown total
        }
    }

    private fun handlePlayerDamage() {
        lives -= 1
        invincibilidadTimer = 2.0f
        if (lives <= 0) {
            gameState = GameState.GameOver
        } else {
            resetPlayerPosition()
        }
    }

    override fun getWorldState(): WorldState {
        // --- CORRECCIÓN: Pasamos el estado de ataque al WorldState ---
        return WorldState(
            player = this.player,
            platforms = levelData?.platforms ?: emptyList(),
            enemies = levelData?.enemies?.filter { it.isAlive } ?: emptyList(),
            collectibles = levelData?.collectibles ?: emptyList(),
            currentDimension = this.currentDimension,
            playerInvincible = invincibilidadTimer > 0,
            isPlayerAttacking = player.isAttacking,
            playerFacingDirection = player.facingDirection
        )
    }
    
    override fun getPlayer(): Player = player
    override fun getLevelData(): LevelData? = levelData
    override fun getGameInfo(): String {
        return "Player X: ${player.position.x.toInt()} | Y: ${player.position.y.toInt()} | OnGround: ${player.isOnGround}"
    }
    override fun getGameState(): GameState = gameState
    override fun getLives(): Int = lives
}