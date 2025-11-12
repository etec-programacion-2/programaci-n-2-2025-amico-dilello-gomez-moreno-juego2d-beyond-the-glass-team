package org.example.core

/**
 * Implementación de la lógica del juego (SOLID: Inversión de Dependencias).
 * Esta es la clase "Directora" que implementa 'GameLogicService'.
 * Coordina todos los servicios (Física, Combate, IA) y maneja el estado del juego.
 *
 * (MODIFICADO) Ahora actúa como una Máquina de Estados Finita (FSM)
 * controlando la lógica de 'Menu', 'Playing', 'GameOver' y 'GameWon'.
 *
 * ---
 * @see "Issue BTG-002: Implementa la interfaz 'GameLogicService'."
 * @see "Issue BTG-008: Gestiona el bucle de juego ('update') y el estado ('gameState')."
 * ---
 */
class MiJuego : GameLogicService {

    // --- ESTADO DEL JUEGO ---
    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    private var levelData: LevelData? = null // Datos cargados del nivel (plataformas, enemigos)
    
    /** (MODIFICADO) El estado inicial del juego ahora es el MENÚ. */
    private var gameState: GameState = GameState.Menu
    
    private var lives: Int = 3
    private var invincibilidadTimer: Float = 0f // Temporizador para cuando el jugador es golpeado
    private var currentDimension: Dimension = Dimension.A // Dimensión actual
    private var switchKeyWasPressed = false // Controla el cooldown del cambio de dimensión
    private var originalPlayerStart: Vector2D = Vector2D(0f, 0f)

    private var jumpKeyWasPressed = false

    // --- SERVICIOS (SOLID: S) ---
    private val physicsService: PhysicsService = PhysicsService()
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()
    private val combatService: CombatService = CombatService()
    private val collectionService: CollectionService = CollectionService()
    private val levelLoader: LevelLoader = LevelLoader()
    private val progressionService: ProgressionService = ProgressionService()
    
    // --- PATRÓN OBSERVADOR (BTG-013) ---
    private val subject: Subject = Subject()
    private var abilityUnlocker: AbilityUnlocker? = null // Ahora es 'nullable'

    /**
     * (MODIFICADO) 'loadLevel' ya NO se llama al inicio.
     * Esta función es llamada por 'startGame' o 'handleCollisions' (al pasar de nivel).
     * Ya NO resetea vidas ni al jugador, solo carga el nivel.
     *
     * @param levelName El nombre del archivo (ej. "level1.txt").
     */
    override fun loadLevel(levelName: String) {
        // Carga el nivel actual desde el servicio
        internalLoadLevel(progressionService.getCurrentLevelName())
    }
    
    /**
     * (NUEVO) Inicia el juego.
     * Esta función se llama cuando el usuario presiona "Jugar" en el menú.
     * Resetea todo el progreso del jugador y carga el nivel 1.
     */
    private fun startGame() {
        // (NUEVO) Resetea el servicio de progresión para empezar en este nivel
        progressionService.resetProgress("level1.txt")
        
        // Carga el nivel 1
        internalLoadLevel(progressionService.getCurrentLevelName())
        
        // (Relacionado con BTG-012)
        lives = 3
        
        // (NUEVO) Resetea el estado del jugador completamente
        player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))

        // --- Configuración del Patrón Observador (BTG-013) ---
        abilityUnlocker = AbilityUnlocker(player, 3)
        subject.addObserver(abilityUnlocker!!)
        
        // El estado más importante: cambia la pantalla
        gameState = GameState.Playing
    }
    
    /**
     * Lógica interna para cargar un nivel.
     * (POO: Encapsulamiento) Evita duplicar código.
     */
    private fun internalLoadLevel(levelName: String) {
        // (Relacionado con BTG-007)
        levelData = levelLoader.loadLevel(levelName)
        originalPlayerStart = levelData!!.playerStart.copy()
        resetPlayerPosition()
        
        // (MODIFICADO) No cambia el estado aquí, solo carga los datos
    }

    /**
     * Resetea la posición del jugador al inicio del nivel.
     * (POO: Encapsulamiento) Lógica interna reutilizada.
     */
    private fun resetPlayerPosition() {
        player.position = originalPlayerStart.copy()
        player.velocity = Vector2D(0f, 0f)
        // (Relacionado con BTG-012)
        invincibilidadTimer = 1.5f // Da 1.5s de inmunidad al reaparecer
    }

    /**
     * El bucle de actualización principal del juego.
     * (MODIFICADO) Ahora es una Máquina de Estados (FSM).
     *
     * ---
     * @see "Issue BTG-008: Bucle de Juego y Gestión de Estado."
     * ---
     *
     * @param actions El Set de acciones abstractas del usuario.
     * @param deltaTime El tiempo pasado desde el último fotograma.
     */
    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        
        // (NUEVO) El 'when' principal controla qué lógica ejecutar
        // basado en el estado actual del juego.
        when (gameState) {
            
            GameState.Menu -> {
                // En el menú, solo nos importa si el usuario quiere "Empezar"
                // La acción 'QUIT' (Salir de la App) es manejada por DesktopGame.
                if (GameAction.START_GAME in actions) {
                    startGame()
                }
            }
            
            GameState.Playing -> {
                // (MODIFICADO) La lógica de 'QUIT' (ESC) ahora
                // te devuelve al Menú Principal.
                if (GameAction.QUIT in actions) {
                    gameState = GameState.Menu
                    return // No procesar nada más este fotograma
                }
                
                // (Toda la lógica de juego existente va aquí dentro)
                updatePlayingState(actions, deltaTime)
            }

            GameState.GameOver -> {
                // (MODIFICADO) 'SHIFT' (Switch_Dimension) ahora
                // te devuelve al Menú Principal.
                if (GameAction.SWITCH_DIMENSION in actions) {
                    gameState = GameState.Menu
                }
                // 'QUIT' (ESC) también te devuelve al menú.
                if (GameAction.QUIT in actions) {
                    gameState = GameState.Menu
                }
            }
            
            GameState.GameWon -> {
                // (MODIFICADO) 'SHIFT' (Switch_Dimension) ahora
                // te devuelve al Menú Principal.
                if (GameAction.SWITCH_DIMENSION in actions) {
                    gameState = GameState.Menu
                }
                // 'QUIT' (ESC) también te devuelve al menú.
                if (GameAction.QUIT in actions) {
                    gameState = GameState.Menu
                }
            }
            
            GameState.Paused -> {
                // Lógica de pausa (aún no implementada)
            }
        }
    }
    
    /**
     * (NUEVO) Función de ayuda que contiene toda la lógica
     * de cuando el estado es 'Playing'.
     * (POO: Encapsulamiento) Extraído de 'update'.
     */
    private fun updatePlayingState(actions: Set<GameAction>, deltaTime: Float) {
        // 1. --- MANEJO DE ENTRADA (Input) ---
        handleInput(actions)

        // 2. --- ACTUALIZACIÓN DE TEMPORIZADORES ---
        updateTimers(deltaTime)

        // 3. --- LÓGICA DE FÍSICA (Jugador y Enemigos) ---
        val allPlatforms = levelData?.platforms ?: emptyList()
        physicsService.update(player, allPlatforms, currentDimension, deltaTime)
        
        levelData?.enemies?.filter { it.isAlive }?.forEach { enemy ->
            enemyPhysicsService.update(enemy, allPlatforms, deltaTime)
        }

        // 4. --- LÓGICA DE IA (Enemigos) ---
        levelData?.enemies?.filter { it.isAlive }?.forEach { enemy ->
            enemy.updateAI(allPlatforms)
        }

        // 5. --- LÓGICA DE COLISIONES (Combate, Coleccionables, Caídas, Salida) ---
        handleCollisions()
    }

    /**
     * Procesa las acciones del usuario (mientras se está jugando).
     * (POO: Encapsulamiento) Lógica extraída de 'update'.
     */
    private fun handleInput(actions: Set<GameAction>) {
        // --- Movimiento (BTG-006) ---
        player.velocity.x = 0f
        if (GameAction.MOVE_LEFT in actions) {
            player.velocity.x = -Player.MOVE_SPEED
            player.facingDirection = -1f
        }
        if (GameAction.MOVE_RIGHT in actions) {
            player.velocity.x = Player.MOVE_SPEED
            player.facingDirection = 1f
        }

        // --- Salto (BTG-006) y Doble Salto (BTG-013) ---
        val jumpPressed = GameAction.JUMP in actions
        if (jumpPressed && !jumpKeyWasPressed) { // Detectar "flanco de subida" (solo al presionar)
            if (player.isOnGround) {
                // Salto normal
                player.velocity.y = Player.JUMP_STRENGTH
                player.isOnGround = false
                player.hasDoubleJumped = false // Resetea el doble salto
            } else if (player.canDoubleJump && !player.hasDoubleJumped) {
                // Doble salto (BTG-013)
                player.velocity.y = Player.JUMP_STRENGTH * 0.9f // Un poco menos fuerte
                player.hasDoubleJumped = true
            }
        }
        jumpKeyWasPressed = jumpPressed // Guarda el estado para el próximo fotograma

        // --- Cambio de Dimensión (BTG-009) ---
        val switchPressed = GameAction.SWITCH_DIMENSION in actions
        if (switchPressed && !switchKeyWasPressed) { // Detectar "flanco de subida"
            currentDimension = if (currentDimension == Dimension.A) Dimension.B else Dimension.A
        }
        switchKeyWasPressed = switchPressed // Guarda el estado

        // --- Ataque (BTG-012) ---
        if (GameAction.ATTACK in actions && player.attackTimer <= 0f) { // Solo si el cooldown terminó
            player.isAttacking = true
            player.attackTimer = Player.ATTACK_COOLDOWN // Inicia el temporizador de cooldown
            player.hitEnemiesThisAttack.clear() // Limpia la lista de enemigos golpeados
        }
    }

    /**
     * Actualiza todos los temporizadores del juego (mientras se está jugando).
     * (POO: Encapsulamiento) Lógica extraída de 'update'.
     */
    private fun updateTimers(deltaTime: Float) {
        // --- Temporizador de Invencibilidad (BTG-012) ---
        if (invincibilidadTimer > 0f) {
            invincibilidadTimer -= deltaTime
        }

        // --- Temporizador de Ataque (BTG-012) ---
        if (player.attackTimer > 0f) {
            player.attackTimer -= deltaTime
            
            // Duración del hitbox activo
            if (player.isAttacking && player.attackTimer <= (Player.ATTACK_COOLDOWN - Player.ATTACK_DURATION)) {
                player.isAttacking = false // El hitbox desaparece
            }

            if (player.attackTimer <= 0f) {
                player.isAttacking = false // Asegura que se apague
            }
        }
    }

    /**
     * Maneja las colisiones de combate y recolección (mientras se está jugando).
     * (POO: Encapsulamiento) Lógica extraída de 'update'.
     */
    private fun handleCollisions() {
        val allEnemies = levelData?.enemies ?: emptyList()
        val allCollectibles = levelData?.collectibles ?: emptyList()

        // --- Lógica de Combate (BTG-012) ---
        
        // 1. Comprobar si el JUGADOR golpea a un ENEMIGO
        if (player.isAttacking) {
            val hitEnemies = combatService.checkPlayerAttack(player, allEnemies, currentDimension)
            for (enemy in hitEnemies) {
                if (!player.hitEnemiesThisAttack.contains(enemy)) {
                    enemy.isAlive = false // Enemigo muere
                    player.hitEnemiesThisAttack.add(enemy)
                }
            }
        }

        // 2. Comprobar si un ENEMIGO golpea al JUGADOR
        if (invincibilidadTimer <= 0f) {
            if (combatService.checkPlayerDamage(player, allEnemies, currentDimension)) {
                handlePlayerHit()
                return
            }
        }

        // --- Lógica de Coleccionables (BTG-013) ---
        val collectedItems = collectionService.checkCollectibleCollisions(player, allCollectibles)
        for (item in collectedItems) {
            item.isCollected = true // Marca como recogido
            player.energyFragments += item.value // Suma al contador
            
            // --- Notificar al Patrón Observador (BTG-013) ---
            subject.notify(PlayerEvent.ENERGY_COLLECTED)
        }
        
        // --- LÓGICA DE CAÍDA (MODIFICADA POR USUARIO) ---
        if (invincibilidadTimer <= 0f) {
            val deathPlaneY = -100f
            if (player.position.y < deathPlaneY) {
                println("¡Caída fuera del mundo! Recibiendo daño.")
                handlePlayerHit()
                return
            }
        }
        
        // --- LÓGICA DE PROGRESIÓN DE NIVEL ---
        val exit = levelData?.exitGate ?: return // Si no hay puerta, no hacer nada
        
        val isUnlocked = progressionService.isExitConditionMet(levelData)
        
        if (isUnlocked && isAABBColliding(player.position, player.size, exit.position, exit.size)) {
            if (progressionService.hasNextLevel()) {
                progressionService.advanceToNextLevel()
                internalLoadLevel(progressionService.getCurrentLevelName())
            } else {
                // ¡No hay más niveles! ¡Juego ganado!
                gameState = GameState.GameWon
            }
        }
    }
    
    /**
     * Helper de colisión AABB.
     * (POO: Encapsulamiento) Privado para uso interno.
     */
    private fun isAABBColliding(posA: Vector2D, sizeA: Vector2D, posB: Vector2D, sizeB: Vector2D): Boolean {
        return posA.x < posB.x + sizeB.x &&
               posA.x + sizeA.x > posB.x &&
               posA.y < posB.y + sizeB.y &&
               posA.y + sizeA.y > posB.y
    }

    /**
     * Maneja la lógica de cuando el jugador es golpeado.
     * (POO: Encapsulamiento)
     *
     * ---
     * @see "Issue BTG-012: Vidas y Game Over."
     * ---
     */
    private fun handlePlayerHit() {
        lives -= 1 // Pierde una vida
        
        if (lives <= 0) {
            gameState = GameState.GameOver // Se acabó el juego
        } else {
            resetPlayerPosition() // Resetea al inicio del nivel con invencibilidad
        }
    }

    /**
     * Crea el "snapshot" de solo lectura del estado del mundo.
     * (MODIFICADO) Devuelve un 'WorldState' vacío si no se está jugando.
     *
     * ---
     * @see "Issue BTG-002: Arquitectura (WorldState)."
     * @see "Issue BTG-008: Bucle de Juego."
     * ---
     */
    override fun getWorldState(): WorldState {
        // (MODIFICADO) Si no estamos jugando, no hay mundo que dibujar.
        // Pasamos un estado vacío para evitar 'NullPointerException'.
        if (gameState != GameState.Playing) {
             return WorldState(
                 player = Player(Vector2D(0f, 0f), Vector2D(0f, 0f)), // Jugador fantasma
                 platforms = emptyList(),
                 enemies = emptyList(),
                 collectibles = emptyList(),
                 currentDimension = Dimension.A,
                 levelExit = null,
                 isExitUnlocked = false
             )
        }
    
        // (NUEVO) Comprueba si la salida está desbloqueada
        val isExitUnlocked = progressionService.isExitConditionMet(levelData)
    
        return WorldState(
            player = this.player,
            platforms = levelData?.platforms ?: emptyList(),
            enemies = levelData?.enemies?.filter { it.isAlive } ?: emptyList(),
            collectibles = levelData?.collectibles ?: emptyList(),
            currentDimension = this.currentDimension,
            playerInvincible = invincibilidadTimer > 0,
            isPlayerAttacking = player.isAttacking,
            playerFacingDirection = player.facingDirection,
            levelExit = levelData?.exitGate,
            isExitUnlocked = isExitUnlocked
        )
    }
    
    // --- Getters públicos requeridos por la interfaz 'GameLogicService' ---
    
    override fun getPlayer(): Player = player
    override fun getLevelData(): LevelData? = levelData
    override fun getGameInfo(): String {
        return "Player X: ${player.position.x.toInt()} | Y: ${player.position.y.toInt()} | OnGround: ${player.isOnGround}"
    }
    override fun getGameState(): GameState = gameState
    override fun getLives(): Int = lives
}