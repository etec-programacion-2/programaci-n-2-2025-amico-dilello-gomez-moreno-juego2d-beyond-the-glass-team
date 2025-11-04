package org.example.core

/**
 * Implementación de la lógica del juego (SOLID: Inversión de Dependencias).
 * Esta es la clase "Directora" que implementa 'GameLogicService'.
 * Coordina todos los servicios (Física, Combate, IA) y maneja el estado del juego.
 */
class MiJuego : GameLogicService {

    // --- ESTADO DEL JUEGO ---
    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null // Datos cargados del nivel (plataformas, enemigos)
    private var gameState: GameState = GameState.Playing // Estado actual (Playing, GameOver)
    private var lives: Int = 3
    private var invincibilidadTimer: Float = 0f // Temporizador para cuando el jugador es golpeado
    private var currentDimension: Dimension = Dimension.A // Dimensión actual
    private var switchKeyWasPressed = false // Controla el cooldown del cambio de dimensión
    private var originalPlayerStart: Vector2D = Vector2D(0f, 0f)

    // --- CAMBIO BTG-013: Control de "tecla recién presionada" para el salto ---
    private var jumpKeyWasPressed = false

    // --- SERVICIOS (SOLID: S) ---
    private val physicsService: PhysicsService = PhysicsService()
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()
    private val combatService: CombatService = CombatService()
    
    // --- CAMBIO BTG-013: Nuevos servicios ---
    private val collectionService: CollectionService = CollectionService()

    // --- CAMBIO BTG-013: Patrón Observador ---
    // El motor del juego (MiJuego) es el "Sujeto" (o tiene uno).
    private val eventManager: Subject = Subject()
    // El "Observador" que desbloquea habilidades.
    private lateinit var abilityUnlocker: AbilityUnlocker


    /**
     * Carga el nivel usando el LevelLoader y resetea la posición del jugador.
     */
    override fun loadLevel(levelName: String) {
        val loader = LevelLoader()
        levelData = loader.loadLevel(levelName)
        originalPlayerStart = levelData!!.playerStart.copy() // Guarda la posición de inicio
        
        // --- CAMBIO BTG-013: Configuración del Observador ---
        // Se crea el observador y se le pasa la referencia al jugador
        abilityUnlocker = AbilityUnlocker(player, fragmentThreshold = 3)
        // Se registra el observador en el "Subject" (eventManager)
        eventManager.addObserver(abilityUnlocker)

        resetPlayerPosition()
    }

    /**
     * Resetea la posición, velocidad y estado del jugador.
     */
    private fun resetPlayerPosition() {
        player.position = originalPlayerStart.copy()
        player.velocity = Vector2D(0f, 0f)
        player.isOnGround = false
    }

    /**
     * Reinicia el juego al estado inicial (vidas, estado, enemigos).
     */
    private fun restartGame() {
        lives = 3
        gameState = GameState.Playing
        // Revive a todos los enemigos
        levelData?.enemies?.forEach { it.isAlive = true }
        
        // --- CAMBIO BTG-013: Resetear estado del jugador ---
        // Resetea fragmentos y habilidades al reiniciar
        player.energyFragments = 0
        player.canDoubleJump = false
        player.hasDoubleJumped = false
        // Resetea coleccionables
        levelData?.collectibles?.forEach { it.isCollected = false }

        // Vuelve a registrar el observador (por si acaso, aunque no es estrictamente necesario aquí)
        eventManager.removeObserver(abilityUnlocker) // Limpia
        abilityUnlocker = AbilityUnlocker(player, fragmentThreshold = 3) // Crea uno nuevo
        eventManager.addObserver(abilityUnlocker) // Registra

        resetPlayerPosition()
    }

    /**
     * Este es el BUCLE PRINCIPAL DEL JUEGO. Se llama en cada fotograma.
     * Orquesta todas las actualizaciones.
     */
    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        val currentLevel = levelData ?: return // Si el nivel no está cargado, no hacer nada

        // Si el juego está en Game Over, solo escucha la acción de reiniciar (SWITCH_DIMENSION)
        if (gameState == GameState.GameOver) {
            if (GameAction.SWITCH_DIMENSION in actions) {
                restartGame()
            }
            return // No procesar nada más
        }

        // --- ACTUALIZACIÓN DE ESTADO (TIMERS) ---
        invincibilidadTimer = (invincibilidadTimer - deltaTime).coerceAtLeast(0f)
        player.attackTimer = (player.attackTimer - deltaTime).coerceAtLeast(0f)

        // --- LÓGICA DE DIMENSIÓN ---
        handleDimensionSwitch(actions)

        // --- LÓGICA DE ENTRADA (ACCIONES) ---
        handlePlayerActions(actions)
        handleAttackLogic(actions)

        // --- FÍSICA Y COLISIONES ---
        physicsService.update(player, currentLevel.platforms, currentDimension, deltaTime)

        // --- IA Y FÍSICA DE ENEMIGOS ---
        currentLevel.enemies.forEach { enemy ->
            if (enemy.isAlive) {
                enemy.updateAI(currentLevel.platforms)
                enemyPhysicsService.update(enemy, currentLevel.platforms, deltaTime)
            }
        }

        // --- COMBATE ---
        if (player.isAttacking) {
            val hitEnemies = combatService.checkPlayerAttack(player, currentLevel.enemies, currentDimension)
            hitEnemies.forEach { enemy ->
                if (player.hitEnemiesThisAttack.add(enemy)) {
                    enemy.isAlive = false // Mata al enemigo
                }
            }
        }
        if (invincibilidadTimer <= 0) {
            if (combatService.checkEnemyDamage(player, currentLevel.enemies, currentDimension)) {
                handlePlayerDamage()
            }
        }
        
        // --- CAMBIO BTG-013: Lógica de Colección ---
        handleCollection(currentLevel.collectibles)
    }

    /**
     * Maneja la lógica de cambio de dimensión, incluyendo el "cooldown"
     * para evitar cambios rápidos.
     */
    private fun handleDimensionSwitch(actions: Set<GameAction>) {
        if (GameAction.SWITCH_DIMENSION in actions) {
            if (!switchKeyWasPressed) {
                currentDimension = if (currentDimension == Dimension.A) Dimension.B else Dimension.A
            }
            switchKeyWasPressed = true
        } else {
            switchKeyWasPressed = false
        }
    }

    /**
     * Traduce las acciones (JUMP, MOVE) en cambios de velocidad del jugador.
     */
    private fun handlePlayerActions(actions: Set<GameAction>) {
        // Movimiento Horizontal
        if (GameAction.MOVE_LEFT in actions) {
            player.velocity.x = -Player.MOVE_SPEED
            player.facingDirection = -1f // Mira a la izquierda
        } else if (GameAction.MOVE_RIGHT in actions) {
            player.velocity.x = Player.MOVE_SPEED
            player.facingDirection = 1f // Mira a la derecha
        } else {
            player.velocity.x = 0f // No hay acción, se detiene
        }

        // --- CAMBIO BTG-013: Lógica de Salto y Doble Salto ---
        val jumpPressed = GameAction.JUMP in actions
        
        // Comprueba si la tecla fue "recién presionada"
        if (jumpPressed && !jumpKeyWasPressed) { 
            if (player.isOnGround) {
                // 1. Salto normal desde el suelo
                player.velocity.y = Player.JUMP_STRENGTH
                player.isOnGround = false // Importante: evita re-salto
            } else if (player.canDoubleJump && !player.hasDoubleJumped) {
                // 2. Doble salto (si está en el aire, tiene la habilidad y no la ha usado)
                player.velocity.y = Player.JUMP_STRENGTH // (Se puede usar una fuerza diferente si se desea)
                player.hasDoubleJumped = true // Marca que ya usó el doble salto
            }
        }
        
        jumpKeyWasPressed = jumpPressed // Actualiza el estado de la tecla para el próximo frame
    }

    /**
     * Maneja la lógica de inicio y fin del estado de ataque.
     */
    private fun handleAttackLogic(actions: Set<GameAction>) {
        if (player.attackTimer <= (Player.ATTACK_COOLDOWN - Player.ATTACK_DURATION)) {
            player.isAttacking = false 
        }

        if (GameAction.ATTACK in actions && player.attackTimer <= 0) {
            player.isAttacking = true
            player.attackTimer = Player.ATTACK_COOLDOWN
            player.hitEnemiesThisAttack.clear()
        }
    }
    
    /**
     * --- NUEVO BTG-013 ---
     * Llama al CollectionService y actualiza el estado del jugador.
     * Notifica al "Subject" (eventManager) si se recoge un ítem.
     */
    private fun handleCollection(collectibles: List<Collectible>) {
        val collectedItems = collectionService.checkCollectibleCollisions(player, collectibles)
        
        for (item in collectedItems) {
            // Comprueba si ya fue recogido (doble chequeo por seguridad)
            if (!item.isCollected) { 
                item.isCollected = true
                player.energyFragments += item.value
                
                // --- PATRÓN OBSERVADOR: Notificar ---
                // "Publica" el evento. No sabe (ni le importa)
                // quién está escuchando.
                eventManager.notify(PlayerEvent.ENERGY_COLLECTED)
            }
        }
    }

    /**
     * Gestiona lo que ocurre cuando el jugador recibe daño.
     */
    private fun handlePlayerDamage() {
        lives -= 1
        invincibilidadTimer = 2.0f // 2 segundos de invencibilidad
        if (lives <= 0) {
            gameState = GameState.GameOver // Se acabó el juego
        } else {
            resetPlayerPosition() // Resetea al inicio del nivel
        }
    }

    /**
     * Crea el "snapshot" de solo lectura del estado del mundo.
     * Este es el objeto que se envía al RenderService.
     */
    override fun getWorldState(): WorldState {
        return WorldState(
            player = this.player,
            platforms = levelData?.platforms ?: emptyList(),
            enemies = levelData?.enemies?.filter { it.isAlive } ?: emptyList(),
            
            // --- CAMBIO BTG-013: Pasa la lista de coleccionables ---
            // El renderizador se encargará de filtrar los ya recogidos
            collectibles = levelData?.collectibles ?: emptyList(),
            
            currentDimension = this.currentDimension,
            playerInvincible = invincibilidadTimer > 0,
            isPlayerAttacking = player.isAttacking,
            playerFacingDirection = player.facingDirection
        )
    }
    
    // --- Getters públicos requeridos por la interfaz ---
    
    override fun getPlayer(): Player = player
    override fun getLevelData(): LevelData? = levelData
    override fun getGameInfo(): String {
        return "Player X: ${player.position.x.toInt()} | Y: ${player.position.y.toInt()} | OnGround: ${player.isOnGround}"
    }
    override fun getGameState(): GameState = gameState
    override fun getLives(): Int = lives
}
