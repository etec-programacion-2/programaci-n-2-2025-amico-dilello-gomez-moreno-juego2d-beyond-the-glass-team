package org.example.core

class MiJuego : GameLogicService {

    // --- SOLUCIÓN: TAMAÑO 32x32 + ESTADO BTG-013 ---
    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 32f))
    
    private var levelData: LevelData? = null
    private val physicsService: PhysicsService = PhysicsService()
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()
    private val combatService: CombatService = CombatService()

    // --- LÓGICA DE BTG-013 (LA QUE FALTABA) ---
    private val collectionService: CollectionService = CollectionService()
    private val eventManager: Subject = Subject()
    private val abilityUnlocker: AbilityUnlocker
    private var jumpKeyWasPressed = false
    // --- Fin BTG-013 ---

    private var gameState: GameState = GameState.Playing
    private var lives: Int = 3
    private var invincibilidadTimer: Float = 0f
    
    private var currentDimension: Dimension = Dimension.A
    private var switchKeyWasPressed = false
    private var originalPlayerStart: Vector2D = Vector2D(0f, 0f)

    init {
        // --- LÓGICA DE BTG-013 (LA QUE FALTABA) ---
        abilityUnlocker = AbilityUnlocker(player)
        eventManager.addObserver(abilityUnlocker)
    }

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
        
        // --- LÓGICA DE BTG-013 (LA QUE FALTABA) ---
        levelData?.collectibles?.forEach { it.isCollected = false }
        player.energyFragments = 0
        player.canDoubleJump = false
        // --- Fin BTG-013 ---

        resetPlayerPosition()
    }

    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        val currentLevel = levelData ?: return

        if (gameState == GameState.GameOver) {
            if (GameAction.SWITCH_DIMENSION in actions) {
                restartGame()
            }
            return
        }

        // --- 1. ACTUALIZAR TIMERS ---
        player.attackTimer = (player.attackTimer - deltaTime).coerceAtLeast(0f)
        invincibilidadTimer = (invincibilidadTimer - deltaTime).coerceAtLeast(0f)
        // --- LÓGICA DE BTG-013 (LA QUE FALTABA) ---
        player.dimensionSwitchCooldown = (player.dimensionSwitchCooldown - deltaTime).coerceAtLeast(0f)

        // --- 2. MANEJAR ENTRADA (INPUT) ---
        handlePlayerActions(actions)
        handleDimensionSwitch(actions)

        // --- 3. ACTUALIZAR IA DE ENEMIGOS ---
        currentLevel.enemies.forEach { enemy ->
            val platformsForEnemy = currentLevel.platforms.filter { it.tangibleInDimension == enemy.dimension }
            enemy.updateAI(platformsForEnemy)
        }

        // --- 4. APLICAR FÍSICA ---
        physicsService.update(player, currentLevel.platforms, currentDimension, deltaTime)
        currentLevel.enemies.forEach { enemy ->
            val platformsForEnemy = currentLevel.platforms.filter { it.tangibleInDimension == enemy.dimension }
            enemyPhysicsService.update(enemy, platformsForEnemy, deltaTime)
        }

        // --- 5. LÓGICA DE COMBATE ---
        handleAttackLogic(actions) // 'deltaTime' no se usa aquí (Warning)
        handleCombatCollisions()
        
        // --- 6. LÓGICA DE COLECCIÓN (BTG-013) ---
        handleCollection()
    }

    // --- LÓGICA DE BTG-013 (LA QUE FALTABA) ---
    private fun handleCollection() {
        val collected = collectionService.checkCollection(player, levelData?.collectibles ?: emptyList())
        if (collected.isNotEmpty()) {
            collected.forEach { it.isCollected = true }
            player.energyFragments += collected.size
            // Notificar al observador
            eventManager.notify(PlayerEvent.FragmentCollected)
        }
    }

    private fun handlePlayerActions(actions: Set<GameAction>) {
        // Movimiento Horizontal
        player.velocity.x = when {
            GameAction.MOVE_LEFT in actions -> -Player.MOVE_SPEED
            GameAction.MOVE_RIGHT in actions -> Player.MOVE_SPEED
            else -> 0f
        }
        if (player.velocity.x != 0f) {
            player.facingDirection = if (player.velocity.x > 0) 1f else -1f
        }

        // --- LÓGICA DE SALTO (BTG-013: Doble Salto) ---
        val jumpPressed = GameAction.JUMP in actions
        
        // Salto normal (desde el suelo)
        if (jumpPressed && !jumpKeyWasPressed && player.isOnGround) {
            player.velocity.y = Player.JUMP_STRENGTH
            player.isOnGround = false
            player.hasDoubleJumped = false // Resetea el doble salto
        } 
        // Doble salto (en el aire)
        else if (jumpPressed && !jumpKeyWasPressed && !player.isOnGround && player.canDoubleJump && !player.hasDoubleJumped) {
            player.velocity.y = Player.JUMP_STRENGTH
            player.hasDoubleJumped = true
        }
        
        jumpKeyWasPressed = jumpPressed
    }

    private fun handleDimensionSwitch(actions: Set<GameAction>) {
        val switchPressed = GameAction.SWITCH_DIMENSION in actions

        // --- LÓGICA DE BTG-013 (COOLDOWN) ---
        if (switchPressed && !switchKeyWasPressed && player.dimensionSwitchCooldown <= 0) {
            currentDimension = if (currentDimension == Dimension.A) Dimension.B else Dimension.A
            player.dimensionSwitchCooldown = Player.DIMENSION_SWITCH_COOLDOWN
        }
        switchKeyWasPressed = switchPressed
    }

    // (Eliminé 'deltaTime' de los parámetros, que causaba el warning)
    private fun handleAttackLogic(actions: Set<GameAction>) {
        if (player.isAttacking && player.attackTimer <= (Player.ATTACK_COOLDOWN - Player.ATTACK_DURATION)) {
            player.isAttacking = false
        }

        if (GameAction.ATTACK in actions && player.attackTimer <= 0) {
            player.isAttacking = true
            player.attackTimer = Player.ATTACK_COOLDOWN
            player.hitEnemiesThisAttack.clear() // Limpiar registro de enemigos golpeados
        }
    }
    
    private fun handleCombatCollisions() {
        val currentLevel = levelData ?: return

        // 1. Jugador golpea a enemigo
        if (player.isAttacking) {
            val enemiesHit = combatService.checkPlayerAttack(player, currentLevel.enemies, currentDimension)
            enemiesHit.forEach { enemy ->
                // --- LÓGICA DE BTG-013 (GOLPE ÚNICO) ---
                if (enemy !in player.hitEnemiesThisAttack) {
                    enemy.isAlive = false
                    player.hitEnemiesThisAttack.add(enemy)
                }
            }
        }

        // 2. Enemigo golpea a jugador
        if (invincibilidadTimer <= 0) {
            val playerDamaged = combatService.checkEnemyDamage(player, currentLevel.enemies, currentDimension)
            if (playerDamaged) {
                handlePlayerDamage()
            }
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

    // --- MÉTODOS DE BTG-013 (LOS QUE FALTABAN) ---
    override fun getPlayerFragments(): Int = player.energyFragments
    override fun canPlayerDoubleJump(): Boolean = player.canDoubleJump
}